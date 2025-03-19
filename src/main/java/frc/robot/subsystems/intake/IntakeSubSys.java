package frc.robot.subsystems.intake;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConfig.AnalogPorts;
import frc.robot.RobotConfig.Motors;


public class IntakeSubSys extends SubsystemBase {
    public enum IntakeState {
        SHOOTER_FEED,
        STOPPED,
        MANUAL,
        TROPH, 
        TREE,
        HOLD
    }

    private static IntakeState state = IntakeState.STOPPED;
    
    // Devices
    //public ThriftyNova intakeBottomMotor = new ThriftyNova(Motors.IntakeBottomMotorID, MotorType.MINION);
    //public ThriftyNova intakeTopMotor = new ThriftyNova(Motors.IntakeTopMotorID, MotorType.MINION);
    public SparkMax intakeBottomMotor = new SparkMax(Motors.IntakeBottomMotorID, MotorType.kBrushless);
    public SparkMax intakeTopMotor = new SparkMax(Motors.IntakeTopMotorID, MotorType.kBrushless);

    public AnalogInput gamepieceDetectSensor = new AnalogInput(AnalogPorts.intakeGamepieceSensor);

    /* ----- Constructor ----- */
    public IntakeSubSys() { 
        stopMotors();
        //setBrakeMode(false);
    } 

    /* ----- Periodic ----- */
    @Override
    public void periodic() {
        // drive motor based on the current state
        switch (state) {
            case SHOOTER_FEED:  intakeBottomMotor.set(IntakeConfig.FEED + 0.1);
                                intakeTopMotor.set(IntakeConfig.FEED);
                                break;
            case MANUAL: //intakeBottomMotor.set(-Robot.operatorGamepad.getTriggerTwist());
                         //intakeTopMotor.set(-Robot.operatorGamepad.getTriggerTwist());
                         break;
            case TROPH: intakeTopMotor.set(IntakeConfig.TROPH - 0.25); //- 0.25
                        intakeBottomMotor.set(IntakeConfig.TROPH - 0.03); //- 0.125
                         break;
            case TREE:  intakeBottomMotor.set(IntakeConfig.TREE);
                        intakeTopMotor.set(IntakeConfig.TREE);
                         break;
            case HOLD:  intakeBottomMotor.set(IntakeConfig.HOLD);
                        intakeTopMotor.set(IntakeConfig.HOLD);
                         break;
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
        //setBrakeMode(true);
        intakeBottomMotor.stopMotor();
        intakeTopMotor.stopMotor();
        state = IntakeState.STOPPED;
    }
    
    // ------ Set Brake Modes ---------
    /*public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            intakeBottomMotor.setBrakeMode(true);
            intakeTopMotor.setBrakeMode(true);
        } else {
            intakeBottomMotor.setBrakeMode(false);
            intakeTopMotor.setBrakeMode(false);
        }
    }*/

    /* ----- Getters ---- */

    //public double getMotorSpeed() { return intakeMotor.get(); }
    public static IntakeState getState() { return state; }

    public String getStateString() {
        switch (state) {
            case SHOOTER_FEED: return "SHOOTER FEED";
            case MANUAL:       return "MANUAL";
            case TROPH:        return "TROPH";
            case HOLD:         return "HOLD";

            default:           return "STOPPED";
        }
    }

    public double getSensorVal() {
        return gamepieceDetectSensor.getAverageVoltage();    }

    public boolean getGamepieceDetected() {
        return getSensorVal() > IntakeConfig.gamepieceDetectDistance;
    }


    // ----------------------------------------------------------
    // ---------------- Configure Intake Motor ------------------
    // ----------------------------------------------------------
    public void configureNovaControllers(){
        //intakeTopMotor.setInverted(true);
        
    }
}
