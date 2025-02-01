package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.RobotConfig.Motors;

public class ElevatorSubSys extends SubsystemBase{
    public enum ElevatorState {
        BOTTOM,
        LEVELONE,
        LEVELTWO,
        LEVELTHREE,
        LEVELFOUR
    }

    public static TalonFX elevatorMotor = new TalonFX(Motors.ElevatorMotorID);
    
    public static void method() {
        elevatorMotor.getPosition();
    }
}
