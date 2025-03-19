package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElevatorSubSys extends SubsystemBase {
    public enum ElevatorState {
        LEVELONE,
        LEVELTWO,
        LEVELTHREE,
        LEVELFOUR,
        HIGHALGAE,
        BOTTOM,
        INTAKE,
        MANUAL,
        MANUAL2,
        STOPPED

    }
    private ElevatorState state = ElevatorState.STOPPED;
    final MotionMagicVoltage mr = new MotionMagicVoltage(0);
    
    // Devices
    public static TalonFX elevatorMotor = new TalonFX(Constants.Motors.ElevatorMotorID, "CANFD");
    
    
    /* ----- Constructor ----- */
    public ElevatorSubSys() { 
        configureTalonFXControllers();
        stopMotors();
    } 

    /* ----- Periodic ----- */
    @Override
    public void periodic() {
         // drive motor based on the current state
         switch (state) {
            case LEVELONE:
            //Robot.print(Double.toString(getRotations()));
                         elevatorMotor.setControl(mr.withPosition(ElevatorConfig.LEVELONE));
                         break;
            case LEVELTWO:
                         //Robot.print(Double.toString(getRotations()));
                         elevatorMotor.setControl(mr.withPosition(ElevatorConfig.LEVELTWO));
                         break;
            case LEVELTHREE:
            //Robot.print(Double.toString(getRotations()));
                         elevatorMotor.setControl(mr.withPosition(ElevatorConfig.LEVELTHREE));
                         break;            
            case LEVELFOUR:
            //Robot.print(Double.toString(getRotations()));
                         elevatorMotor.setControl(mr.withPosition(ElevatorConfig.LEVELFOUR));
                         break;
            case HIGHALGAE: 
                         elevatorMotor.setControl(mr.withPosition(ElevatorConfig.HIGHALGAE));
                         break;
            case BOTTOM:
                         elevatorMotor.setControl(mr.withPosition(ElevatorConfig.BOTTOM));
                         break;
            case INTAKE:
                         elevatorMotor.setControl(mr.withPosition(ElevatorConfig.INTAKE));
                         break;
            case MANUAL: elevatorMotor.set(ElevatorConfig.MANUAL);
                         break;
            case MANUAL2: elevatorMotor.set(ElevatorConfig.MANUAL2);
                          break;

            
            // stopped included:

            default: elevatorMotor.set(0.0);
        }
    }

    // --------------------------------------------------------
    // ---------------- elevator Motor Methods ------------------
    // --------------------------------------------------------

    /* ----- Setters ----- */
    public static Command setElevatorSpeed(double speed) {
        return new InstantCommand(() -> elevatorMotor.set(speed));
    }

    public void setNewState(ElevatorState newState) {
        state = newState;
    }

    public void stopMotors() {
        elevatorMotor.stopMotor();
        state = ElevatorState.BOTTOM;
    }
    

    /* ----- Getters ---- */

    public double getMotorSpeed() { return elevatorMotor.get(); }
    public double getRotations() { return elevatorMotor.getPosition().getValueAsDouble(); }
    
    public ElevatorState getState() { return state; }

    public String getStateString() {
        switch (state) {
            case LEVELONE:       return "LEVELONE";
            case LEVELTWO:       return "LEVELTWO";
            case LEVELTHREE:       return "LEVELTHREE";
            case LEVELFOUR:       return "LEVELFOUR";
            case BOTTOM:         return "BOTTOM";
            case MANUAL:         return "MANUAL";

            default:           return "STOPPED";
        }
    }

    // ----------------------------------------------------------
    // ---------------- Configure elevator Motor ------------------
    // ----------------------------------------------------------
    public void configureTalonFXControllers(){
        // Config the only Talon FX motor
        elevatorMotor.getConfigurator().apply(ElevatorConfig.getConfig());
        elevatorMotor.setInverted(true);
    }
}
