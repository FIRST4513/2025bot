package frc.robot.drivetrain;

// WPILib Core
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// WPILib Math
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;

// Third Party
import org.littletonrobotics.junction.Logger;

// Custom Code
import frc.robot.drivetrain.config.DrivetrainConfig;

public class DrivetrainSubSys extends SubsystemBase {

    public static SwerveModule[] swerveMods = new SwerveModule[4];
    public static PigeonGyro gyro = new PigeonGyro();

    public SwerveModuleState frontLeftState;
    public SwerveModuleState frontRightState;
    public SwerveModuleState BackLeftState;
    public SwerveModuleState BackRightState;

    private final SwerveDrivePoseEstimator poseEstimator;

    private final SwerveModulePosition[] swerveModulePositions;

    // ----- Constructor -----
    public DrivetrainSubSys() {
        // Initialize swerve modules first
        for (int i = 0; i < 4; i++) {
            swerveMods[i] = new SwerveModule(i);
        }

        // Initialize gyro
        gyro = new PigeonGyro();

        // Now we can safely get positions since modules are created
        swerveModulePositions = new SwerveModulePosition[4];
        swerveModulePositions[0] = swerveMods[0].getPosition();
        swerveModulePositions[1] = swerveMods[1].getPosition();
        swerveModulePositions[2] = swerveMods[2].getPosition();
        swerveModulePositions[3] = swerveMods[3].getPosition();

        // Define the standard deviations for the pose estimator
        var stateStdDevs = VecBuilder.fill(0.1, 0.1, 0.1);
        var visionStdDevs = VecBuilder.fill(1, 1, 1);
        SwerveDriveKinematics kinematics = DrivetrainConfig.getKinematics();
        poseEstimator = new SwerveDrivePoseEstimator(
                kinematics,
                Rotation2d.fromDegrees(
                        getGyroYawDegrees()),
                swerveModulePositions,
                new Pose2d(),
                stateStdDevs,
                visionStdDevs);

        frontLeftState = new SwerveModuleState(swerveMods[0].getModuleVelocityMPS(),
                swerveMods[0].getSteerAngleRotation2d());
        frontRightState = new SwerveModuleState(swerveMods[1].getModuleVelocityMPS(),
                swerveMods[1].getSteerAngleRotation2d());
        BackLeftState = new SwerveModuleState(swerveMods[2].getModuleVelocityMPS(),
                swerveMods[2].getSteerAngleRotation2d());
        BackRightState = new SwerveModuleState(swerveMods[3].getModuleVelocityMPS(),
                swerveMods[3].getSteerAngleRotation2d());
    }

    @Override
    public void periodic() {
        Logger.recordOutput("Gyro/Angle", getGyroYawDegrees()); // Log Gyro Heading
        Logger.recordOutput("Robot Pose", poseEstimator.getEstimatedPosition()); // Log robot Pose

        swerveModulePositions[0] = swerveMods[0].getPosition();
        swerveModulePositions[1] = swerveMods[1].getPosition();
        swerveModulePositions[2] = swerveMods[2].getPosition();
        swerveModulePositions[3] = swerveMods[3].getPosition();

        poseEstimator.update(gyro.getYawRotation2d(), swerveModulePositions);
    }

    // ----- Driving Methods -----

    /**
     * Used to drive the swerve robot, should be called from commands that require
     * swerve.
     *
     * @param fwdPositive            Velocity of the robot fwd/rev, Forward Positive
     *                               meters per second
     * @param leftPositive           Velocity of the robot left/right, Left Positive
     *                               meters per secound
     * @param omegaRadiansPerSecond  Rotation Radians per second
     * @param fieldRelative          If the robot should drive in field relative
     * @param isOpenLoop             If the robot should drive in open loop
     * @param centerOfRotationMeters The center of rotation in meters
     */

    public void drive(double fwdPositive,
            double leftPositive,
            double omegaRadiansPerSecond,
            boolean fieldRelative,
            boolean isOpenLoop,
            Translation2d centerOfRotationMeters) {

        // --------------- Step 1 Set Chassis Speeds Field or Robot Relative
        // -----------------
        // mps (Meters Per Second) and rps (Radians Per Second)
        ChassisSpeeds speeds;
        if (fieldRelative) {
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                    fwdPositive, leftPositive, omegaRadiansPerSecond, gyro.getYawRotation2d());
        } else {
            speeds = new ChassisSpeeds(fwdPositive, leftPositive, omegaRadiansPerSecond);
        }
        // Now Apply speeds to swerve motors
        driveByChassisSpeeds(speeds);
    }

    public void driveByChassisSpeeds(ChassisSpeeds speeds) {
        // --------------- Step 2 Create Swerve Modules Desired States Array
        // ---------------
        // Wheel Velocity (mps) and Wheel Angle (radians) for each of the 4 swerve
        // modules
        SwerveModuleState[] swerveModuleDesiredStates = DrivetrainConfig.getKinematics().toSwerveModuleStates(speeds,
                new Translation2d());

        // -------------------------- Step 3 Desaturate Wheel speeds
        // -----------------------
        // LOOK INTO THE OTHER CONSTRUCTOR FOR desaturateWheelSpeeds to see if it is
        // better
        SwerveDriveKinematics.desaturateWheelSpeeds(
                swerveModuleDesiredStates, DrivetrainConfig.getMaxVelocity());

        // ------------------ Step 4 Send Desrired Module states to wheel modules
        // ----------------
        for (SwerveModule mod : swerveMods) {
            mod.setDesiredState(swerveModuleDesiredStates[mod.modNumber], false);
        }
    }

    // ------------------- Lock Wheels at angle to prevent rolling
    // -------------------
    public void setModulesLock() {
        double[] angles = { 225, 135, 315, 45 };
        for (int i = 0; i < 4; i++) {
            swerveMods[i].setDesiredState(new SwerveModuleState(0, new Rotation2d(angles[i])), false);
        }
    }

    // ----------------------- Stop Motors ---------------
    public void stop() {
        for (SwerveModule mod : swerveMods) {
            mod.stopMotors();
        }
    }

    public void resetOdometryAndGyroFromPose(Pose2d pose, double gyroHeading) {
        poseEstimator.resetPosition(
                Rotation2d.fromDegrees(gyroHeading),
                swerveModulePositions,
                pose);
    }

    // -------------- Odometry Getters/Setters ---------------------------------

    public ChassisSpeeds getChassisSpeeds() {
        return DrivetrainConfig.m_kinematics.toChassisSpeeds(
                frontLeftState, frontRightState, BackLeftState, BackRightState);
    }

    public Pose2d getPose() {
        return poseEstimator.getEstimatedPosition();
    }

    // Odometry Methods
    public void resetPose(Pose2d pose) {
        poseEstimator.resetPosition(
                DrivetrainSubSys.gyro.getYawRotation2d(),
                swerveModulePositions,
                pose);
    }

    public void resetPose() {
        resetPose(new Pose2d());
    }

    public static double getGyroYawDegrees() {
        return gyro.getYawDegrees();
    }

    public void zeroGyroHeading() {
        gyro.yawReset();
    }

    public static void setGyroHeading(double newAngle) {
        gyro.yawReset(newAngle);
    }

    /***********************************************************************
     * Control - Request - Processing
     ************************************************************************/

    public void addVisionMeasurement(Pose2d visionMeasurement, double timestampSeconds) {
        System.out.println("Adding vision measurement: " + visionMeasurement + " at timestamp: " + timestampSeconds);
        poseEstimator.addVisionMeasurement(visionMeasurement, timestampSeconds);
    }

    public void addVisionMeasurement(
            Pose2d visionMeasurement, double timestampSeconds, Matrix<N3, N1> stdDevs) {
        System.out.println("Adding vision measurement: " + visionMeasurement + " at timestamp: " + timestampSeconds
                + " with stdDevs: " + stdDevs);
        poseEstimator.addVisionMeasurement(visionMeasurement, timestampSeconds, stdDevs);
    }

}