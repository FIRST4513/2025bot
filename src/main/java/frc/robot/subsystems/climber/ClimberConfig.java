package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

public class ClimberConfig {
    // IR Prox distance value for detection of a gamepiece
    protected static final double gamepieceDetectDistance = 1;

    // retract/eject speeds
    protected static final double GROUND = 1.0;
    protected static final double AMP = 0.4;  // TODO: Remove for week 3 code
    protected static final double TRAP = 0.4;  // TODO: Remove for week 3 code
    protected static final double FEED = 1.2;

    /* Neutral Modes */
    protected static final NeutralMode climberNeutralMode = NeutralMode.Brake;

    /* Inverts */
    protected static final boolean climberMotorInvert = false;

    // increase to reduce jitter
    protected static final int climberAllowableError = 0;

    /* climber Motor Current Limiting */
    protected static final int     climberContinuousCurrentLimit = 30;
    protected static final int     climberPeakCurrentLimit       = 30;
    protected static final int     climberPeakCurrentDuration    = 100;
    protected static final boolean climberEnableCurrentLimit     = true;

    /* Ramp Rate */
    protected static final double openLoopRamp = 0;
    protected static final double closedLoopRamp = 0;

    // --------------- Constuctor Setting Up Motor Config values -------------
    protected static TalonSRXConfiguration getConfig() {
        /* climber Motor Configurations */
        TalonSRXConfiguration climberSRXConfig = new TalonSRXConfiguration();

        climberSRXConfig.slot0.kP = 0;
        climberSRXConfig.slot0.kI = 0;
        climberSRXConfig.slot0.kD = 0;
        climberSRXConfig.slot0.kF = 0;
        climberSRXConfig.slot0.allowableClosedloopError = climberAllowableError;
        climberSRXConfig.openloopRamp                   = openLoopRamp;
        climberSRXConfig.closedloopRamp                 = closedLoopRamp;
        climberSRXConfig.continuousCurrentLimit         = climberContinuousCurrentLimit;
        climberSRXConfig.peakCurrentLimit               = climberPeakCurrentLimit;         
        climberSRXConfig.peakCurrentDuration            = climberPeakCurrentDuration;

        return climberSRXConfig;
    }
}
