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
import frc.robot.subsystems.climber.commands.ClimberCmds;

public class ClimberSubSys extends SubsystemBase {
    public enum ClimberState {
        EXTEND,
        STOW,
        STOPPED,
    }

    private ClimberState state = ClimberState.STOPPED;
    
    
    // Devices
    public static WPI_TalonSRX climberMotor = new WPI_TalonSRX(Motors.ClimberMotorID);
    public static Servo WinchLock = new Servo(PWMPorts.winchLockID);
    
    
    /* ----- Constructor ----- */
    public ClimberSubSys() { 
        WinchLock.setSpeed(.75);
        WinchLock.setAngle(90); //124.08 is UNLOCKED
        configureTalonSRXControllers();
        stopMotors();
        setBrakeMode(true);
    } 

    /* ----- Periodic ----- */
    @Override
    public void periodic() {
         // drive motor based on the current state
         switch (state) {
            case EXTEND: ClimberCmds.unlockWinch();
                         climberMotor.set(ClimberConfig.EXTEND);
                         break;
            case STOW:   climberMotor.set(ClimberConfig.STOW);
            // stopped included:

            default: climberMotor.set(0);
            ClimberCmds.lockWinch();
        }
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
            case EXTEND:       return "EXTEND";
            case STOW:       return "STOW";
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
