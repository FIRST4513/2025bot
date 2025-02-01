package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class IntakeTelemetry {
    protected ShuffleboardTab tab;

    public IntakeTelemetry( IntakeSubSys intake) {
        tab = Shuffleboard.getTab("Intake");
        //tab.addNumber("Motor Speed:",         () -> intake.getMotorSpeed())       .withPosition(0, 1).withSize(2, 2);
        //tab.addString("State",                () -> intake.getStateString())      .withPosition(0, 4).withSize(2, 1);
    }
}
