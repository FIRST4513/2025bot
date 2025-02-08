package frc.robot.subsystems.elevator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Robot;
import frc.robot.subsystems.elevator.ElevatorSubSys;
import frc.robot.subsystems.elevator.ElevatorSubSys.ElevatorState;

public class ElevatorCmds {
    public static final double TIMEOUT = 10;

    public static void setupDefaultCommand() {
    //    Robot.elevator.setDefaultCommand(elevatorStopCmd());
    }

    /* ----- elevator Stop Command ----- */
    public static Command elevatorStopCmd() {
        return new InstantCommand( () -> Robot.elevator.stopMotors(), Robot.elevator);
    }

    /* ----- elevator Set State Commands ----- */
    public static Command elevatorSetState(ElevatorState newState) {
        return new InstantCommand(() -> Robot.elevator.setNewState(newState));
    }

    /* ----- Intake Set State Command Shortcuts ----- */
    public static Command elevatorSetLevelOne() {
        return elevatorSetState(ElevatorState.LEVELONE); 
    }
    public static Command elevatorSetLevelTwo() {
        return elevatorSetState(ElevatorState.LEVELTWO); 
    }
    public static Command elevatorSetLevelThree() {
        return elevatorSetState(ElevatorState.LEVELTHREE); 
    }
    public static Command elevatorSetLevelFour() {
        return elevatorSetState(ElevatorState.LEVELFOUR); 
    }
    public static Command elevatorSetBottom() {
        return elevatorSetState(ElevatorState.BOTTOM);
    }
    public static Command elevatorSetIntake() {
        return elevatorSetState(ElevatorState.INTAKE);
    }
    public static Command elevatorSetManual() {
        return elevatorSetState(ElevatorState.MANUAL);
    }
    public static Command elevatorSetManual2() {
        return elevatorSetState(ElevatorState.MANUAL2);
    }
    public static Command elevatorSetStopped() {
        return elevatorSetState(ElevatorState.STOPPED);
    }

    
}
