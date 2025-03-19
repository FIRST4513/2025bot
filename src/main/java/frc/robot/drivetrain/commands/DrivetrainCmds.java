package frc.robot.drivetrain.commands;

import frc.robot.Robot;
import frc.robot.XBoxCtrlrs.pilot.commands.PilotGamepadCmds;

public class DrivetrainCmds {
    public static void setupDefaultCommand() {
        Robot.swerve.setDefaultCommand(PilotGamepadCmds.FpvPilotSwerveCmd());
    }

    // ----------------------- Gyro & Odometry Commands  ------------------------
//    public static Command ZeroGyroHeadingCmd() {
//        return new InstantCommand( () -> Robot.swerve.zeroGyroHeading() );
//    }
//
//    public static Command SetGyroYawCmd(double deg){
//        return new InstantCommand( () -> DrivetrainSubSys.setGyroHeading(deg) );
//    }
}
