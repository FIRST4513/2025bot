package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotConfig.AnalogPorts;
import frc.robot.RobotConfig.Motors;
import frc.robot.RobotConfig.PWMPorts;

public class ClimberSubSys extends SubsystemBase {
    public enum ClimberState {
        SHOOTER_FEED,
        STOPPED,
        GROUND,
        MANUAL,
        TRAP,
        AMP,
    }

    private ClimberState state = ClimberState.STOPPED;
    
    // Devices
    public WPI_TalonSRX climberMotor = new WPI_TalonSRX(Motors.ClimberMotorID);
    public Servo WenchLock = new Servo(PWMPorts.wenchLockID);
    
    /* ----- Constructor ----- */
    public ClimberSubSys() { 
        configureTalonSRXControllers();
        stopMotors();
        setBrakeMode(true);
    } 

    /* ----- Periodic ----- */
    @Override
    public void periodic() {
    }

    // --------------------------------------------------------
    // ---------------- Climber Motor Methods ------------------
    // --------------------------------------------------------

    /* ----- Setters ----- */

    public void setNewState(ClimberState newState) {
        state = newState;
    }

    public void stopMotors() {
        setBrakeMode(true);
        climberMotor.stopMotor();
        state = ClimberState.STOPPED;
    }
    
    // ------ Set Brake Modes ---------
    public void setBrakeMode(Boolean enabled) {
        if (enabled) {
            climberMotor.setNeutralMode(NeutralMode.Brake);
        } else {
            climberMotor.setNeutralMode(NeutralMode.Coast);
        }
    }

    /* ----- Getters ---- */

    public double getMotorSpeed() { return climberMotor.get(); }
    public ClimberState getState() { return state; }

    public String getStateString() {
        switch (state) {
            case SHOOTER_FEED: return "SHOOTER FEED";
            case GROUND:       return "GROUND";
            case MANUAL:       return "MANUAL";
            case TRAP:         return "TRAP";
            case AMP:          return "AMP";
            default:           return "STOPPED";
        }
    }

    // ----------------------------------------------------------------
    // ---------------- Climber Detect Methods -------------------------
    // ----------------------------------------------------------------

    // ---------- General Gamepiece Detects ----------


    // ----------------------------------------------------------
    // ---------------- Configure Climber Motor ------------------
    // ----------------------------------------------------------
    public void configureTalonSRXControllers(){
        // Config the only Talon SRX motor
        climberMotor.configFactoryDefault();
        climberMotor.configAllSettings(ClimberConfig.getConfig());
        climberMotor.setInverted(ClimberConfig.climberMotorInvert);
        climberMotor.setNeutralMode(ClimberConfig.climberNeutralMode);
        climberMotor.setSelectedSensorPosition(0);
    }
}
