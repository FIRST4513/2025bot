package frc.robot.XBoxCtrlrs.operator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
//import frc.robot.auto.Auto;
//import frc.robot.auto.comands.AutoCmds;
import frc.robot.drivetrain.commands.SwerveDriveCmd;
import frc.robot.subsystems.intake.commands.IntakeCmds;
import frc.robot.subsystems.orchestra.orchestraCmds;



/** Add your docs here. */
public class OperatorGamepadCmds {


    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
    }


    public static Command stopAllCmd() {
        return new SequentialCommandGroup(
            IntakeCmds.intakeStopCmd(),
            //ElevatorCmds.elevatorStopCmd(),
            orchestraCmds.stop()
        );
    }



    // ------------- Drive by TeleOp Commands ---------------

    /** Field Oriented Drive */
    public static Command FpvPilotSwerveCmd() {
        return new SwerveDriveCmd(
                        () -> Robot.pilotGamepad.getDriveFwdPositive(),
                        () -> Robot.pilotGamepad.getDriveLeftPositive(),
                        () -> Robot.pilotGamepad.getDriveRotationCCWPositive(),
                        true,
                        false)
                .withName("FpvPilotSwerveCmd");
    }

    /** Robot Oriented Drive */
    public static Command RpvPilotSwerveCmd() {
        return new SwerveDriveCmd(
                        () -> Robot.pilotGamepad.getDriveFwdPositive(),
                        () -> Robot.pilotGamepad.getDriveLeftPositive(),
                        () -> Robot.pilotGamepad.getDriveRotationCCWPositive(),
                        // () -> Robot.pilotGamepad.getDriveFwdPositiveSlow(),
                        // () -> Robot.pilotGamepad.getDriveLeftPositiveSlow(),
                        // () -> Robot.pilotGamepad.getDriveRotationCCWPositiveSlow(),
                        false,
                        false)
                .withName("RpvPilotSwerveCmd");
    }





    
    // -------------------- Rumble Controller -------------

    public static Command RumblePilotCmd(double intensity) {
        return new RunCommand(() -> Robot.pilotGamepad.rumble(intensity), Robot.pilotGamepad);
    }
}
