package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Robot;
import frc.robot.subsystems.intake.IntakeSubSys.IntakeState;

public class IntakeCmds {
    public static final double TIMEOUT = 10;

    public static void setupDefaultCommand() {
        //Robot.intake.setDefaultCommand(intakeStopCmd());
    }

    /* ----- Intake Stop Command ----- */
    public static Command intakeStopCmd() {
        return new InstantCommand( () -> Robot.intake.stopMotors(), Robot.intake);
    }

    /* ----- Intake Set State Commands ----- */
    public static Command intakeSetState(IntakeState newState) {
        return new InstantCommand(() -> Robot.intake.setNewState(newState));
    }

    /* ----- Intake Set State Command Shortcuts ----- */
    public static Command intakeSetFeedCmd()   { return intakeSetState(IntakeState.SHOOTER_FEED); }
    public static Command intakeSetManualCmd() { return intakeSetState(IntakeState.MANUAL); }
    public static Command intakeSetTrophCmd() { return intakeSetState(IntakeState.TROPH); }
    public static Command intakeSetTreeCmd() { return intakeSetState(IntakeState.TREE); }
    public static Command intakeSetHoldCmd() { return intakeSetState(IntakeState.HOLD); }
    public static Command intakeSetStoppedCmd() { return intakeSetState(IntakeState.STOPPED); }


    /* ----- Intake Command with Until Conditions */
    public static boolean holdOn = false;

    public static void holdOnSet() { holdOn = !holdOn;}
    public static Command holdOnCmd() {
        return new InstantCommand(() -> holdOnSet());
    }
    public static boolean getHoldOn() {return holdOn;}



    public static Command intakeGroundUntilGamepieceCmd() {
        return new SequentialCommandGroup(
            intakeSetFeedCmd(),
            new WaitUntilCommand(() -> Robot.intake.getGamepieceDetected()).withTimeout(2),
            //intakeStopCmd(),
            //holdOnCmd(),
            intakeSetHoldCmd()
        );
        
    }

    /**
     * Run the intake at the shooter feed gamepiece speed until the gamepiece has left the intake, plus a given amount of time.
     * <p>
     * Will timeout after 10 seconds, plus your given time.
     * @param secondsAfterGamepieceDeparture Time to keep running after the gamepiece has left the intake's sensor
     * @return A SequentialCommandGroup
     */
    public static Command intakeFeedCmd(double secondsAfterGamepieceDeparture) {
        return new SequentialCommandGroup(
            intakeSetFeedCmd(),
            // new WaitCommand(TIMEOUT).until(() -> Robot.intake.not()),  // possible change to WaitUntilCommand
            new WaitCommand(secondsAfterGamepieceDeparture),
            intakeSetHoldCmd(),
            intakeStopCmd()
        );
    }
}
