package frc.robot.subsystems.finger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.subsystems.finger.FingerSubSys.FingerState;

public class FingerCmds {
   
   
    public static Command fingerStopCmd() {
        return new InstantCommand(() -> Robot.finger.stopMotors());
    }

    public static Command fingerSetState(FingerState newState) {
        return new InstantCommand(() -> Robot.finger.setNewState(newState));
    }

    public static Command fingerSetOn() { return fingerSetState(FingerState.ON); }
    public static Command fingerSetStopped() { return fingerSetState(FingerState.STOPPED); }

    public static Command fingerSetTemp() { 
        return new SequentialCommandGroup(
            fingerSetOn(), 
            new WaitCommand(0.3), 
            fingerSetStopped()
            );
        }

}
