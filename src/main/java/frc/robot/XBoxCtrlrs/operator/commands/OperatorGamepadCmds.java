package frc.robot.XBoxCtrlrs.operator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Robot;
import frc.robot.subsystems.intake.IntakeSubSys;
import frc.robot.subsystems.intake.IntakeSubSys.IntakeState;
import frc.robot.subsystems.elevator.throwaway;

/*
import frc.robot.subsystems.intake.commands.IntakeCmds;
import frc.robot.subsystems.passthrough.commands.PassthroughCmds;
import frc.robot.subsystems.pivot.commands.PivotCmds;
import frc.robot.subsystems.shooter.commands.ShooterCmds;
 */

public class OperatorGamepadCmds {
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
    //    Robot.pilotGamepad.setDefaultCommand(ProcessAndSetRumbleCmd());
    }

    /* ----- Overrides ----- */
    public static Command manualAllCmd() {
        return new SequentialCommandGroup(


            //EXAMPLE:
            //IntakeCmds.intakeSetManualCmd(),
            //PassthroughCmds.setManualCmd(),
            //ShooterCmds.setManualCmd(),
            //PivotCmds.setManualCmd()
        );
    }

    public static Command stopAllCmd() {
        return new SequentialCommandGroup(


        /*
            EXAMPLE:
            IntakeCmds.intakeStopCmd(),
            PassthroughCmds.stopPassthroughCmd(),
            ShooterCmds.stopCmd(),
            PivotCmds.stopCmd()
            */
        );
    }

    /* ----- Combo Commands ----- */
    public static Command hpIntakeUntilGamepiece() {
        return new SequentialCommandGroup(

            /* EXAMPLE
            PassthroughCmds.setHPIntakeCmd(),  
            PivotCmds.setHPIntakeCmd(),
            new WaitUntilCommand(() -> Robot.pivot.isAtTarget()),
            ShooterCmds.setHPIntakeCmd(),
            new WaitUntilCommand(() -> Robot.intake.getGamepieceDetected()),
            stopAllCmd()
             */
        );
    }

    public static Command spit() {
        return new InstantCommand(() -> Robot.intake.intakeBottomMotor.setPercent(throwaway.speed));
    }



    public static Command readyForBumperShotCmd() {
        return new ParallelCommandGroup(

        /*
            EXAMPLE:
            PivotCmds.setLowAndRunCmd(),
            ShooterCmds.setSpeakerSpeedCmd()
             */
        );
    }
    

    // -------------------- Rumble Controller  ---------------
    public static Command RumbleOperatorCmd(double intensity) {
        return new RunCommand(() -> Robot.operatorGamepad.rumble(intensity), Robot.operatorGamepad);
    }
}
