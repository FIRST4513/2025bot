package frc.robot.drivetrain;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import com.pathplanner.lib.path.PathPlannerPath;
//import com.pathplanner.lib.path.PathPlannerTrajectory;
import com.pathplanner.lib.path.PathPoint;
//import com.pathplanner.lib.path.PathPlannerTrajectory.State;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.swerve.Request;
import frc.robot.Robot;
import frc.util.DriveState;
import frc.util.PoseAndTimestamp;
import frc.robot.drivetrain.config.DrivetrainConfig;
//import frc.robot.vision.VisionSubSys;


public class DrivetrainSubSys extends SubsystemBase {

    public static SwerveModule   swerveMods[] = new SwerveModule[4];
    public static PigeonGyro gyro;
    protected OdometryThread odometry;
    private final RotationController rotationController;


    public SwerveModuleState frontLeftState;
    public SwerveModuleState frontRightState;
    public SwerveModuleState BackLeftState;
    public SwerveModuleState BackRightState;
    

ChassisSpeeds chassisSpeeds;

    // ----- Constructor -----
    public DrivetrainSubSys() {
        
        // Instantiate all the Swerve Drive Modules
        SwerveModule frontLeftModule = new SwerveModule(0);
        SwerveModule frontRightModule = new SwerveModule(0);
        SwerveModule backLeftModule = new SwerveModule(0);
        SwerveModule backRightModule = new SwerveModule(0);

        swerveMods[0] = frontLeftModule;
        swerveMods[1] = frontRightModule;
        swerveMods[2] = backLeftModule;
        swerveMods[3] = backRightModule;


        gyro = new PigeonGyro();            // Instantiate Gyro
        odometry = new OdometryThread( this );
        odometry.start();

        rotationController = new RotationController(this);


        /*double flVelocity = swerveMods[0].getModuleVelocityMPS();
        double flAngleDouble = swerveMods[0].getModuleAngleDegrees();
        Rotation2d flAngleAngle = Rotation2d.fromDegrees(flAngleDouble);
        var frontLeftState = new SwerveModuleState(flVelocity, flAngleAngle);

        double frVelocity = swerveMods[1].getModuleVelocityMPS();
        double frAngle = swerveMods[1].getModuleAngleDegrees();
        Rotation2d rotation = Rotation2d.fromDegrees(frAngle);
        var frontRightState = new SwerveModuleState(frVelocity, rotation);*/

        frontLeftState = new SwerveModuleState(swerveMods[0].getModuleVelocityMPS(), swerveMods[0].getSteerAngleRotation2d());
        frontRightState = new SwerveModuleState(swerveMods[1].getModuleVelocityMPS(), swerveMods[1].getSteerAngleRotation2d());
        BackLeftState = new SwerveModuleState(swerveMods[2].getModuleVelocityMPS(), swerveMods[2].getSteerAngleRotation2d());
        BackRightState = new SwerveModuleState(swerveMods[3].getModuleVelocityMPS(), swerveMods[3].getSteerAngleRotation2d());

        
  
    }

    

    // ----- Periodic Method -----
    @Override
    public void periodic() {
        // Log Chassi stuff
        Logger.recordOutput("Gyro/Angle", getGyroYawDegrees());                         // Log Gyro Heading
        Logger.recordOutput("Robot Pose", getPose());                                   // Log robot Pose
        Logger.recordOutput("Drive/ModuleStates", getModStates());                      // Log each Module's States (Vel.)

         // Get the rotation of the robot from the gyro.
            var gyroAngle = gyro.getYawRotation2d();
            // Update the pose
            DriveState.Pose = OdometryThread.m_odometry.update(gyroAngle,
            new SwerveModulePosition[] {
                swerveMods[0].getPosition(), swerveMods[1].getPosition(),
                swerveMods[2].getPosition(), swerveMods[4].getPosition()
            });
            
    }

    // ----- Driving Methods -----
    /**
     * Used to drive the swerve robot, should be called from commands that require swerve.
     *
     * @param fwdPositive Velocity of the robot fwd/rev, Forward Positive meters per second
     * @param leftPositive Velocity of the robot left/right, Left Positive meters per secound
     * @param omegaRadiansPerSecond Rotation Radians per second
     * @param fieldRelative If the robot should drive in field relative
     * @param isOpenLoop If the robot should drive in open loop
     * @param centerOfRotationMeters The center of rotation in meters
     */

    public void drive( double fwdPositive,
                       double leftPositive,
                       double omegaRadiansPerSecond,
                       boolean fieldRelative,
                       boolean isOpenLoop,
                       Translation2d centerOfRotationMeters) {
    
        // --------------- Step 1 Set Chassis Speeds Field or Robot Relative  -----------------
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
        // --------------- Step 2 Create Swerve Modules Desired States Array ---------------
        // Wheel Velocity (mps) and Wheel Angle (radians) for each of the 4 swerve modules
        SwerveModuleState[] swerveModuleDesiredStates =
                DrivetrainConfig.getKinematics().toSwerveModuleStates(speeds, new Translation2d());

        // -------------------------- Step 3 Desaturate Wheel speeds -----------------------
        // LOOK INTO THE OTHER CONSTRUCTOR FOR desaturateWheelSpeeds to see if it is better
        SwerveDriveKinematics.desaturateWheelSpeeds(
            swerveModuleDesiredStates, DrivetrainConfig.getMaxVelocity());

        // ------------------ Step 4 Send Desrired Module states to wheel modules ----------------
        for (SwerveModule mod : swerveMods) {
            mod.setDesiredState(swerveModuleDesiredStates[mod.modNumber], false);
        }
    }

    // ------------------- Lock Wheels at angle to prevent rolling -------------------
    public void setModulesLock() {
        double angles[] = {225, 135, 315, 45};
        for (int i = 0; i<4; i++) {
            swerveMods[i].setDesiredState(new SwerveModuleState(0, new Rotation2d(angles[i])), false);
        }
    }

    // ----------------------- Stop Motors ---------------
    public void stop() {
        for (SwerveModule mod : swerveMods) {
            mod.stopMotors();
        }
    }

    /**
     * Given a path, reset the odometry and Gyro to the starting pose of the path
     * 
     * @param path PathPlannerTrajectory robot will follow
     * and to take initial pose from
     */
    // public void resetOdometryAndGyroFromPath(PathPlannerPath path) {
    //     // Get Rotation and Location from Path
    //     Pose2d      startingRobotPose = path.getPreviewStartingHolonomicPose(); // Get Starting Pose
    //     Rotation2d  startRobotRotation = startingRobotPose.getRotation();       // Get Starting Rotation
    //     Pose2d newPose = new Pose2d(startingRobotPose.getTranslation(),startRobotRotation );
    //     resetOdometryAndGyroFromPose(newPose );
    // }

     public void resetOdometryAndGyroFromPose(Pose2d pose, double gyroHeading) {
        Robot.print("3. Resetting odometry to Pose: x(" + pose.getX() + ") y(" + pose.getY() + ") & r(" + pose.getRotation().getDegrees() + ").");
        /*zeroOdoemtry();
        Robot.print("4. Zero'd odometry");*/
        setGyroHeading(gyroHeading);    // Init gyroHeading
        Robot.print("5. Set Gyro heading, new heading: " + Robot.swerve.getGyroYawDegrees());
        resetPose(pose);                                    // Set Odometry Pose
        Robot.print("6. Reset Pose to: x(" + Robot.swerve.getPose().getX() + ") y(" + Robot.swerve.getPose().getY() + ") & r(" + Robot.swerve.getPose().getRotation().getDegrees() + ").");
     }


    // -------------- Odometry Getters/Setters ---------------------------------

    


    // DriveState Class methods
    public DriveState   getDriveState()             { return odometry.getDriveState(); } // Get All the odometry data
     
    public SwerveModuleState[]    getModStates()    { return getDriveState().ModuleStates; }
    public SwerveModulePosition[] getModPositions() { return getDriveState().ModulePositions; }
    public ChassisSpeeds         getChassisSpeeds() { 
        ChassisSpeeds chassisSpeeds = DrivetrainConfig.m_kinematics.toChassisSpeeds(
        frontLeftState, frontRightState, BackLeftState, BackRightState
        );

        return chassisSpeeds;
    }


    public Pose2d       getPose()                   { return getDriveState().Pose; }
    public double       getPoseHdgDegrees()         { return getDriveState().Pose.getRotation().getDegrees(); }
    public Rotation2d   getRotation()               { return getPose().getRotation(); }
    

    

    // Odometry Methods
    public void         resetPose(Pose2d pose)      { 
        
        odometry.m_odometry.resetPosition(getRotation(), getModPositions(), pose);

        //odometry.resetOdometryPose(pose);
     }
    public void         resetPose()                 { resetPose(new Pose2d()); }
        //public void         zeroOdoemtry()              { odometry.zeroEverything(); }
    //public void         reorientPose(double angle)  { odometry.reorientPose(angle); }
    public synchronized void updateOdometryVisionPose (Pose2d pose, double timestamp) { 
                                                    // odometry.addVisionMeasurement( pose, timestamp); }
                                                    resetPose(pose); }

    // Gyro Getters/Setters
    public double       getGyroYawDegrees()         { return gyro.getYawDegrees(); }
    public void         zeroGyroHeading()           { gyro.yawReset(); }
    public void         setGyroHeading(double newAngle) { gyro.yawReset(newAngle); }

    // Rotation Controller methods
    public void resetRotationController()           { rotationController.reset(); }
    public double calculateRotationController(DoubleSupplier targetRadians) {
                                                    return rotationController.calculate(targetRadians.getAsDouble()); }

    // Vision Getters
    // public Pose2d getVisionPose()                   {return Robot.vision.getVisionPoseEst().pose.toPose2d();}
    // public boolean isVisionPoseValid()              {return Robot.vision.getVisionPoseEst().isNew();}

    /***********************************************************************
    *                       Control - Request - Processing   
    ************************************************************************/
    
    // Use this to control the swerve drive, set motors, etc.
    public void setControlMode(Request mode) {
        setControl(mode);
    }

    // Returns a commmand that applies the given request to the drivetrain
    public Command applyRequest(Supplier<Request> requestSupplier) {
        return run(() -> setControlMode(requestSupplier.get()));
    }

    /**
     * Applies the specified control request to this swerve drivetrain.
     *
     * @param request Request to apply
     */
    public void setControl(Request request) {
        try {
            odometry.m_stateLock.writeLock().lock();
            odometry.m_requestToApply = request;
        } finally {
            odometry.m_stateLock.writeLock().unlock();
        }
    }

 }
