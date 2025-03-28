package frc.robot.XBoxCtrlrs.operator;

import edu.wpi.first.math.geometry.Translation2d;

/** Constants used by the Pilot Gamepad */
public class OperatorGamepadConfig {
    public static enum MaxSpeeds {FAST, MEDIUM, SLOW}


    // 
    public static final double climberSpeedExp      = 20;
    public static final double climberSpeedScaler   = -1.0;
    public static final double climberSpeedDeadband = 0.05;
    public static final double climberSpeedOffset   = 0;

    // Selectable speeds
    //----Fast-----//
    public static final double FastfowardVelocity       = -4.9;
    public static final double FastsidewaysVelocity     = -4.9;
    public static final double FastrotationVelocity     = -6.0;
    public static final double FastForwardExp           = 30;
    public static final double FastSidewaysExp          = 30;
    public static final double FastRotationExp          = 20;

    //---Medium--//
    public static final double MediumForwardVelocity   = -2.5;
    public static final double MediumSidewaysVelocity  = -2.5;
    public static final double MediumRotationVelocity  = 0.25;
    public static final double MedSlowForwardExp        = 30;
    public static final double MedSlowSidewaysExp       = 30;
    public static final double MedSlowRotationExp       = 20;

    //---Slow--//
    public static final double SlowforwardVelocity      = -1.25;
    public static final double SlowsidewaysVelocity     = -1.25;
    public static final double SlowrotationVelocity     = 0.25;
    public static final double SlowForwardExp           = 30;
    public static final double SlowSidewaysExp          = 30;
    public static final double SlowRotationExp          = 20;

    // forward speed//
    public static final double forwardSpeedExp =       35.0;
    public static final double forwardSpeedScaler =    -1.5;
    public static final double forwardSpeedDeadband =  0.15;
    public static final double forwardSpeedOffset =    0.0;
    // sideways speed
    public static final double sidewaysSpeedExp =       forwardSpeedExp;
    public static final double sidewaysSpeedScaler =    -1.5; //-4.8;
    public static final double sidewaysSpeedDeadband =  forwardSpeedDeadband;
    public static final double sidewaysSpeedOffset =    0.0;
    // rotation speed
    public static final double rotationSpeedExp =       forwardSpeedExp;
    public static final double rotationSpeedScaler =    -(Math.PI);  // -pi to +pi? for radians
    public static final double rotationSpeedDeadband =  forwardSpeedDeadband;
    public static final double rotationSpeedOffset =    0.0;

    public static final Translation2d intakeCoRmeters = new Translation2d(0, 0);
}
