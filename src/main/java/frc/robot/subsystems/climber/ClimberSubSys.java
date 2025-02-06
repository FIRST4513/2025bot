package frc.robot.subsystems.climber;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;
import com.pathplanner.lib.config.RobotConfig;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotConfig.AnalogPorts;
import frc.robot.RobotConfig.Motors;
import frc.robot.RobotConfig.PWMPorts;
import frc.robot.canbus.canfd;
import frc.robot.subsystems.climber.commands.ClimberCmds;

public class ClimberSubSys extends SubsystemBase {
    public enum ClimberState {
        EXTEND,
        STOW,
        STOPPED,
    }

    private ClimberState state = ClimberState.STOPPED;
    
    
    // Devices
    public static TalonFX climberMotor = new TalonFX(Motors.ClimberMotorID, "CANFD");
    public static Servo WinchLock = new Servo(PWMPorts.winchLockID);
    
    
    /* ----- Constructor ----- */
    public ClimberSubSys() { 
        WinchLock.setSpeed(.75);
        WinchLock.setAngle(113); //113 is UNLOCKED
        configureTalonFXControllers();
        //stopMotors();
    } 

    /* ----- Periodic ----- */
    @Override
    public void periodic() {
         // drive motor based on the current state
         switch (state) {
            case EXTEND: ClimberCmds.unlockWinch();
                         climberMotor.set(ClimberConfig.EXTEND);
                         break;
            case STOW:   ClimberCmds.lockWinch();
                         climberMotor.set(ClimberConfig.STOW);
                         break;
            
            // stopped included:

            default: climberMotor.set(0.0);
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
        climberMotor.stopMotor();
        state = ClimberState.STOPPED;
    }
    

    /* ----- Getters ---- */

    public double getMotorSpeed() { return climberMotor.get(); }
    public ClimberState getState() { return state; }

    public String getStateString() {
        switch (state) {
            case EXTEND:       return "EXTEND";
            case STOW:         return "STOW";
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
    public void configureTalonFXControllers(){
        // Config the only Talon SRX motor
        climberMotor.getConfigurator().apply(ClimberConfig.getConfig());
    }
}
