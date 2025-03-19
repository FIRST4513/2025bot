package frc.robot.subsystems.climber;

import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.climber.commands.ClimberCmds;

public class ClimberSubSys extends SubsystemBase {
    public enum ClimberState {
        EXTEND,
        STOW,
        TWOROTATIONS,
        STARTUP,
        STOPPED,
    }

    private ClimberState state = ClimberState.STOPPED;
    
    
    // Devices
    public static TalonFX climberMotor = new TalonFX(Constants.Motors.ClimberMotorID, "CANFD");
    public static Servo WinchLock = new Servo(Constants.PWMPorts.winchLockID);
    final MotionMagicVoltage mr = new MotionMagicVoltage(0);

    
    /* ----- Constructor ----- */
    public ClimberSubSys() { 
        WinchLock.setSpeed(.75);
        WinchLock.setAngle(90); //113 is UNLOCKED
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
            case TWOROTATIONS: climberMotor.setControl(mr.withPosition(ClimberConfig.TWOROTATIONS));
                               break;
            case STARTUP: ClimberCmds.unlockWinch();
                          climberMotor.setPosition(ClimberConfig.STARTUP);
                          ClimberCmds.lockWinch();
            
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
