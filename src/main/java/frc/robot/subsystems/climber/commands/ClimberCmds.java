package frc.robot.subsystems.climber.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Robot;
import frc.robot.subsystems.climber.ClimberSubSys.ClimberState;

public class ClimberCmds {
    public static final double TIMEOUT = 10;

    public static void setupDefaultCommand() {
        Robot.climber.setDefaultCommand(intakeStopCmd());
    }

    /* ----- climber Stop Command ----- */
    public static Command intakeStopCmd() {
        return new InstantCommand( () -> Robot.climber.stopMotors(), Robot.climber);
    }

    /* ----- climber Set State Commands ----- */
    public static Command climberSetState(ClimberState newState) {
        return new InstantCommand(() -> Robot.climber.setNewState(newState));
    }

    /* ----- Intake Set State Command Shortcuts ----- */
    public static Command climberSetGroundCmd() { return climberSetState(ClimberState.GROUND); }
    public static Command climberSetFeedCmd()   { return climberSetState(ClimberState.SHOOTER_FEED); }
    public static Command climberSetTrapCmd()   { return climberSetState(ClimberState.TRAP); }
    public static Command climberSetAmpCmd()    { return climberSetState(ClimberState.AMP); }
    public static Command climberSetManualCmd() { return climberSetState(ClimberState.MANUAL); }

}
