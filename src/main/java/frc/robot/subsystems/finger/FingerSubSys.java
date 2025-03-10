package frc.robot.subsystems.finger;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.intake.IntakeSubSys.IntakeState;

public class FingerSubSys extends SubsystemBase{
    public enum FingerState {
        ON,
        STOPPED
    }

    private static FingerState state = FingerState.STOPPED;

    public SparkMax FingerMotor = new SparkMax(23, MotorType.kBrushless);

    public FingerSubSys() {
        stopMotors();
    }

    @Override
    public void periodic() {
        switch (state) {
            case ON: FingerMotor.set(0.25);
                     break;
            default:
                FingerMotor.set(0);
                break;
        }
    }


    public void setNewState(FingerState newState) {
       state = newState;
    }

    public void stopMotors() {
        FingerMotor.stopMotor();
        state = FingerState.STOPPED;
    }
}
