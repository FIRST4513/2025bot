package frc.robot.auto;


import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.auto.AutoBuilder;

import frc.robot.drivetrain.config.DrivetrainConfig;

public class AutoConfig {

    // Auto menu Position selectors
    public static final String kLeft         = "Left";
    public static final String kCenter         = "Center";
    public static final String kRight           = "Right";

    // Auto menu Action selectors
    public static final String kActionDoNothing         = "Do Nothing";
    public static final String kCrossOnlySelect         = "Crossline";
    public static final String kActionIntakeToCL        = "Intake To CL";

    // variables for tuning
    public static final double translationkP = 5.0;  // PID value (P)roportional
    public static final double translationkI = 0.0;  // PID value (I)ntegral
    public static final double translationkD = 0.0;  // PID value (D)erivative

    public static final double rotationkP = 3.0;  // PID value (P)roportional
    public static final double rotationkI = 0.0;  // PID value (I)ntegral
    public static final double rotationkD = 0.0;  // PID value (D)erivative

    /* Swerve Conroller Constants */
    public static final double kMaxSpeed = 2.7;     // note: doesn't seem to do much
    public static final double kMaxAccel = 2.4;     // 2 worked but took too long
    public static final double kGenPathMaxSpeed = 3.0;
    public static final double kGenPathMaxAceel = 3.0;
    public static double driveBaseRadius = Math.hypot((DrivetrainConfig.trackWidth / 2.0), (DrivetrainConfig.wheelBase/2.0));


}
