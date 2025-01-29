package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class ClimberTelemetry {
    protected ShuffleboardTab tab;

    public ClimberTelemetry( ClimberSubSys climber) {
        tab = Shuffleboard.getTab("Intake");
        tab.addNumber("Motor Speed:",         () -> climber.getMotorSpeed())       .withPosition(0, 1).withSize(2, 2);
        tab.addString("State",                () -> climber.getStateString())      .withPosition(0, 4).withSize(2, 1);
    }
}
