package frc.robot.auto;

import java.util.List;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.XBoxCtrlrs.operator.commands.OperatorGamepadCmds;
import frc.robot.auto.Auto;

public class AutoCmds {

    // ------------------------------------ Do Nothing ---------------------------------------
    public static Command DoNothingCmd() {
        // Set position and Gyro Heading based on position
        return new SequentialCommandGroup(
            new InstantCommand(() -> System.out.println("do nothing cmd")),
            new InstantCommand(() -> Auto.setStartPose())
        );
    }

    // ----------------------------- Cross Line Commands -------------------------------------
    public static Command 
    CrossLineOnlyCmd( String pathName ) {
        return new SequentialCommandGroup(
                new InstantCommand( ()-> Robot.print( "Auto Shoot Left Speaker with path " + pathName)),
                initAndFollowPath( pathName )
                // lock wheels
        );
    }



    // ----------------------------------- Two Note ------------------------------------------
    /*public static Command TwoNoteCmd( String pos, String pathName, String pathNameBack ) {
        return new SequentialCommandGroup(  
            new InstantCommand( ()-> Robot.print( "Two Note Cmd ")),
            // Shoot pre-loaded note
            SpeakerShootCmd(),
            // Do the following two things at the same time: intake note, and follow paths
            new ParallelCommandGroup(
                // Intake note sequence
                new SequentialCommandGroup(
                    OperatorGamepadCmds.groundIntakeUntilGamepieceCmd(),
                    OperatorGamepadCmds.readyForBumperShotCmd()
                ),
                // First run to-note path, then run to-speaker path
                new SequentialCommandGroup(
                    initAndFollowPath(pathName),
                    followPath(pathNameBack)
                )
            ),
            OperatorGamepadCmds.noAutoPosSpeakerShot()
        );
    }*/

    // ----- PathPlanning-Interfacing Methods -----

    public static Command followPath(PathPlannerPath path) {
        return AutoBuilder.followPath(path);                                    // Return Cmd controller to run Path
    }

    // Get a Command that Follows a Path
    public static Command followPath(String name) {
        try{
        PathPlannerPath path = PathPlannerPath.fromPathFile("Crossline");

        // Create a path following command using AutoBuilder. This will also trigger event markers.
        return AutoBuilder.followPath(path);
        } catch (Exception e) {
        DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
        return DoNothingCmd();
    }
    }

    public static Command initAndFollowPath(String name) {
        // Init Robot pose from auto Selections. We can't pull initial Pose from path because we 
        // only build blue paths, and then rely on AutoBuilder to flip path at run time as needed for red.
        Robot.print("Loading path name: " + name);
        return new SequentialCommandGroup(
            new InstantCommand(() -> Auto.setStartPose()),      // Init Robot Pose on Field                 
            followPath(name)                                    // Run Path
        );
    }

    // Build a path from current position to a new position and then run the path.  If this is only run duringteleop we 
    // dont need the line for preventflippin true. this is automatically handled in the AutoBuilder.
    // If this is only used in teleop we should move this method to drivercmds not here in autocmds

}
