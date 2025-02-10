package frc.robot.subsystems.intake;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

public class IntakeConfig {
    // IR Prox distance value for detection of a gamepiece
    protected static final double gamepieceDetectDistance = 1;

    // retract/eject speeds
    protected static final double FEED = 0.4;
    public static final double TROPH = 0;
    protected static final double TREE = -0.4;
    protected static final double HOLD = 0;



    /* Neutral Modes */
    protected static final NeutralMode intakeNeutralMode = NeutralMode.Brake;

    /* Inverts */
    //protected static final boolean intakeMotorInvert = false;

    // increase to reduce jitter
    protected static final int intakeAllowableError = 0;

    /* Intake Motor Current Limiting */
    protected static final int     intakeContinuousCurrentLimit = 30;
    protected static final int     intakePeakCurrentLimit       = 30;
    protected static final int     intakePeakCurrentDuration    = 100;
    protected static final boolean intakeEnableCurrentLimit     = true;

    /* Ramp Rate */
    protected static final double openLoopRamp = 0;
    protected static final double closedLoopRamp = 0;

    // --------------- Constuctor Setting Up Motor Config values -------------

}
