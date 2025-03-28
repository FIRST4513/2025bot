package frc.robot.XBoxCtrlrs.pilot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.XBoxCtrlrs.pilot.PilotGamepad;
//import frc.robot.auto.Auto;
//import frc.robot.auto.comands.AutoCmds;
import frc.robot.drivetrain.commands.SwerveDriveCmd;



/** Add your docs here. */
public class PilotGamepadCmds {



    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
    }

    // ------------- Drive by TeleOp Commands ---------------

    /** Field Oriented Drive */
    public static Command FpvPilotSwerveCmd() {
        return new SwerveDriveCmd(
                        () ->    -Robot.pilotGamepad.getDriveFwdPositive(),
                        () -> -Robot.pilotGamepad.getDriveLeftPositive(),
                        () -> Robot.pilotGamepad.getDriveRotationCCWPositive(),
                        true,
                        false)
                .withName("FpvPilotSwerveCmd");
    }
    public static Command FpvWithVision() {
        Robot.print(Double.toString(Robot.getTargetYaw()));
        return new SwerveDriveCmd(
                        () ->    -Robot.pilotGamepad.getDriveFwdPositive(),
                        () -> -Robot.pilotGamepad.getDriveLeftPositive(),
                        () -> PilotGamepad.rotationCurve.calculateMappedVal(Robot.getTargetYaw()/5),
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
