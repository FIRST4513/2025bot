package frc.robot.subsystems.intake;

import java.time.chrono.MinguoEra;

import com.thethriftybot.ThriftyNova;
import com.thethriftybot.ThriftyNova.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotConfig.AnalogPorts;
import frc.robot.RobotConfig.Motors;


public class IntakeSubSys extends SubsystemBase {
    public enum IntakeState {
        SHOOTER_FEED,
        STOPPED,
        MANUAL,
        TROPH, 
        MIDDLE,
        TOP,
        HOLD
    }

    private static IntakeState state = IntakeState.STOPPED;
    
    // Devices
    public ThriftyNova intakeBottomMotor = new ThriftyNova(Motors.IntakeBottomMotorID, MotorType.MINION);
    public ThriftyNova intakeTopMotor = new ThriftyNova(Motors.IntakeTopMotorID, MotorType.MINION);



    /* ----- Constructor ----- */
    public IntakeSubSys() { 
        configureNovaControllers();
        stopMotors();
        setBrakeMode(true);
    } 

    /* ----- Periodic ----- */
    @Override
    public void periodic() {
        // drive motor based on the current state
        switch (state) {
            case SHOOTER_FEED: intakeBottomMotor.set(IntakeConfig.FEED);
                               intakeTopMotor.set(IntakeConfig.FEED);
                               break;
            case MANUAL: intakeBottomMotor.set(-Robot.operatorGamepad.getTriggerTwist());
                         intakeTopMotor.set(-Robot.operatorGamepad.getTriggerTwist());
                         break;
            case TROPH: intakeBottomMotor.set(IntakeConfig.TROPH);
                        intakeTopMotor.set(IntakeConfig.TROPH);
                       break;
            case MIDDLE: intakeBottomMotor.set(IntakeConfig.MIDDLE);
                         intakeTopMotor.set(IntakeConfig.MIDDLE);
                      break;
            case TOP: intakeBottomMotor.set(IntakeConfig.TOP);
                      intakeTopMotor.set(IntakeConfig.TOP);
                    break;
            case HOLD: intakeBottomMotor.set(IntakeConfig.HOLD);
                       intakeTopMotor.set(IntakeConfig.HOLD);
            // stopped included:
            default: intakeBottomMotor.set(0);
                     intakeTopMotor.set(0);
        }
    }

    // --------------------------------------------------------
    // ---------------- Intake Motor Methods ------------------
    // --------------------------------------------------------

    /* ----- Setters ----- */

    public void setNewState(IntakeState newState) {
       state = newState;
    }

    public void stopMotors() {
        setBrakeMode(true);
        intakeBottomMotor.stopMotor();
        intakeTopMotor.stopMotor();
        state = IntakeState.STOPPED;
    }
    
    // ------ Set Brake Modes ---------
    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            intakeBottomMotor.setBrakeMode(true);
            intakeTopMotor.setBrakeMode(true);
        } else {
            intakeBottomMotor.setBrakeMode(false);
            intakeTopMotor.setBrakeMode(false);
        }
    }

    /* ----- Getters ---- */

    //public double getMotorSpeed() { return intakeMotor.get(); }
    public static IntakeState getState() { return state; }

    public String getStateString() {
        switch (state) {
            case SHOOTER_FEED: return "SHOOTER FEED";
            case MANUAL:       return "MANUAL";
            case TROPH:        return "TROPH";
            case MIDDLE:       return "MIDDLE";
            case TOP:          return "TOP";
            case HOLD:         return "HOLD";

            default:           return "STOPPED";
        }
    }


    // ----------------------------------------------------------
    // ---------------- Configure Intake Motor ------------------
    // ----------------------------------------------------------
    public void configureNovaControllers(){
        intakeTopMotor.setInverted(true);
        
    }
}
