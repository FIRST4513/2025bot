package frc.robot.drivetrain;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.lib.swerve.CTREModuleState;
import frc.robot.drivetrain.config.AngleFalconConfig;
import frc.robot.drivetrain.config.CanCoderConfig;
import frc.robot.drivetrain.config.DriveFalconConfig;
import frc.robot.drivetrain.config.DrivetrainConfig;

public class SwerveModule {
    // Module's Identification
    public final String modName;
    public final int modNumber;

    // Module's Devices
    private final TalonFX mAngleMotor;
    private final TalonFX mDriveMotor;
    private final CANcoder mAngleEncoder;

    // Module's Control Requests
    private VelocityVoltage mDrivePIDReq;   // drive by PID (closedloop)
    private VoltageOut mDriveVoltageReq;    // drive by Voltage out (open loop)
    private PositionVoltage mAnglePIDReq;   // angle/turn

    // ----- Constructor -----

    /**
     * Construct a Swerve Module object, a piece of a broader Swerve Drive Subsystem.
     * <p>
     * Given a module number/ID, this instantiates the devices associated with this swerve module, and configures the motors to be run.
     *
     * @param moduleNumber the integer ID number of the Swerve Module to be made. This is used to get information about CAN IDs for devices
     */
    public SwerveModule(int moduleNumber) {
        modNumber = moduleNumber;
        modName = DrivetrainConfig.getModName(moduleNumber);
        mAngleMotor = new TalonFX(DrivetrainConfig.getModAngleCanID(moduleNumber), "CANFD");
        mDriveMotor = new TalonFX(DrivetrainConfig.getModDriveCanID(moduleNumber), "CANFD");
        mAngleEncoder = new CANcoder(DrivetrainConfig.getModCanCoderID(moduleNumber), "CANFD");
        configureDevices(moduleNumber);

        setupControlReqObjects();
    }


    // ----- Drive Methods -----

    /**
     * Stop the motors!
     */
    public void stopMotors() {
        mDriveMotor.stopMotor();
        mAngleMotor.stopMotor();
    }

    /**
     * Tell the steer and drive motors to go to the appropriate positions and velocities.
     *
     * @param desiredState the SwerveModuleState object that represents what the swerve module should be doing
     * @param isOpenloop   whether or not to run in Openloop. True = DON'T use PID; False = DO use PID
     */
    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenloop) {
        desiredState = CTREModuleState.optimize(desiredState, getSteerAngleRotation2d());  // get can coder degrees
        setAngle(desiredState);
        setSpeed(desiredState, isOpenloop);
    }

    /**
     * Set the angle of the steer motor.
     *
     * @param desiredState the SwerveModuleState object that represents what the swerve module should be doing
     */
    public void setAngle(SwerveModuleState desiredState) {
        // Tell Steer motor to go to the required rotation angle from desired state
        double angleRotation = desiredState.angle.getRotations();

        if ((Math.abs(desiredState.speedMetersPerSecond) < (DrivetrainConfig.maxVelocity * 0.005))) {
            // Prevent rotating module if speed is less than 1% of max speed. (Jitter prevention)
            angleRotation = getSteerAngle();
        }

        mAngleMotor.setControl(mAnglePIDReq.withPosition(angleRotation).withSlot(0));
    }

    /**
     * Set the velocity of the drive motor.
     *
     * @param desiredState the SwerveModuleState object that represents what the swerve module should be doing
     */
    public void setSpeed(SwerveModuleState desiredState, boolean isOpenloop) {
        // Tell Drive motor to go to the required velocity MPS from desired state
        double velocityMPS = desiredState.speedMetersPerSecond;

        if (isOpenloop) {
            // Openloop, straight voltage
            // divide target speed by max speed at 12v, giving us a percentage of top speed [0, 1]
            double targetSpeedPercent = velocityMPS / DrivetrainConfig.getMaxVelocity(); // Percent
            double targetVoltage = targetSpeedPercent * 12;     // convert to volts from percent

            // tell motor to drive at that voltage, no PID or anything
            mDriveMotor.setControl(mDriveVoltageReq.withOutput(targetVoltage));
        } else {
            // Closedloop, use PID to go to velocity
            // calculate velocity by multiplying target speed by the ratio the wheel and gear ratios imply on the motor
            double velocityToSet = velocityMPS * DriveFalconConfig.driveRotationsPerMeter;

            // tell motor to drive at that velocity, using PID
            mDriveMotor.setControl(mDrivePIDReq.withVelocity(velocityToSet).withSlot(0));
        }
    }

    // ----- Falcon and CAN Coder Rotation Getters -----

    /**
     * Get the steer angle of the wheel, taken from the CAN Coder with its method `getAbsolutePosition().getValueAsDouble()`
     *
     * @return A Rotation2d representing the angle in Degrees, Radians, and Rotations at the same time 1 rotation only
     */
    public Rotation2d getSteerAngleRotation2d() {
        return Rotation2d.fromRotations(getSteerAngle());
    }

    /**
     * Get the steer angle of the wheel, taken from the CAN Coder
     *
     * @return A double representing the angle in rotations Truncated to 1 rotation Not accumulative
     */
    public double getSteerAngle() {
        return mAngleEncoder.getAbsolutePosition().getValueAsDouble();
    }

    // ----- Odometry and Swerve Module State/Position Getters -----

    /**
     * --------------------------------Position (Meters , Roation2d) ----------------------------
     * Get the position of the swerve module, including the position (distance in meters)
     * and the rotation of the wheel in the steer direction
     *
     * @return A SwerveModulePosition object representing the position of the module [double, Rotation2d]
     */
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(
                getModulePositionMeters(),
                getSteerAngleRotation2d()
        );
    }

    public double getModulePositionMeters() {
        double driveRotations = mDriveMotor.getPosition().getValueAsDouble();
        return (driveRotations / DriveFalconConfig.driveRotationsPerMeter);
    }

    /**
     * ------------------------ State (MetersPerSec, Rotation2d) ----------------------------
     * Get the state of the swerve module, including it's velocity on the field
     * and the rotation of the wheel in the steer direction
     *
     * @return A SwerveModuleState object representing the state of the module [double, Rotation2d]
     */
    public SwerveModuleState getState() {
        return new SwerveModuleState(getModuleVelocityMPS(), getSteerAngleRotation2d());
    }

    /**
     * @return A Double representing speed in meters per second
     */
    public double getModuleVelocityMPS() {
        // Get falcon rotations per second
        double rps = mDriveMotor.getVelocity().getValueAsDouble();
        return (rps / DriveFalconConfig.driveRotationsPerMeter);       // Convert to MPS
    }

    // ----- Misc Getters -----

    public TalonFX getTalonDrive() {
        return this.mDriveMotor;
    }

    public TalonFX getTalonAngle() {
        return this.mAngleMotor;
    }

    public double getModuleAngleDegrees() {
        return getState().angle.getDegrees();
    }

    public double getCanCoderOffset() {
        return DrivetrainConfig.getModAngleOffset(modNumber);
    }


    // ----- Misc setters -----
    public void resetModulePostion() {
        mDriveMotor.setPosition(0.0);
    }

    public void resetModulePostion(double pos) {
        mDriveMotor.setPosition(pos);
    }

    // ------- Other Methods --------------

    public void configureDevices(int moduleNumber) {
        // Configure Devices (Falcons, CAN Coder) and print responses
        StatusCode response = mAngleMotor.getConfigurator().apply(AngleFalconConfig.getConfig(moduleNumber));
        printResponse(response, modName + " angle motor");

        response = mDriveMotor.getConfigurator().apply(DriveFalconConfig.getConfig(moduleNumber));
        printResponse(response, modName + " drive motor");

        response = mAngleEncoder.getConfigurator().apply(CanCoderConfig.getConfig(moduleNumber));
        printResponse(response, modName + " CAN Coder");
    }

    private void printResponse(StatusCode response, String motorName) {
        if (!response.isOK()) {
            System.out.println(
                    "Motor Name "
                            + motorName
                            + " failed config with error "
                            + response);
        }
    }

    public void setupControlReqObjects() {
        mDrivePIDReq = new VelocityVoltage(0);
        mDriveVoltageReq = new VoltageOut(0);
        mAnglePIDReq = new PositionVoltage(0);

        mDrivePIDReq.UpdateFreqHz = 0;
        mDriveVoltageReq.UpdateFreqHz = 0;
        mAnglePIDReq.UpdateFreqHz = 0;
    }
}
