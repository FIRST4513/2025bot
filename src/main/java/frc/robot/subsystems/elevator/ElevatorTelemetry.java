package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ElevatorTelemetry {
    protected ShuffleboardTab tab;

    public ElevatorTelemetry( ElevatorSubSys elevator) {
        tab = Shuffleboard.getTab("Intake");
        tab.addNumber("Motor Speed:",         () -> elevator.getMotorSpeed())       .withPosition(0, 1).withSize(2, 2);
        tab.addString("State",                () -> elevator.getStateString())      .withPosition(0, 4).withSize(2, 1);
    }
}
