package frc.robot.subsystems.orchestra;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.AudioConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.drivetrain.DrivetrainSubSys;
import frc.robot.drivetrain.config.DrivetrainConfig;
import frc.robot.subsystems.climber.ClimberSubSys;
import frc.robot.subsystems.elevator.ElevatorSubSys;

public class orchestraSubSys {
    public static Orchestra m_orchestra = new Orchestra();
    public orchestraSubSys() {
        m_orchestra.addInstrument(ElevatorSubSys.elevatorMotor);
        //m_orchestra.addInstrument(ClimberSubSys.climberMotor, 1);

        var status = m_orchestra.loadMusic("output.chrp");
        //withAllowMusicDurDisable();
        
        
    }


    // Add a single device to the orchestra


    // Attempt to load the chrp
    

    public AudioConfigs withAllowMusicDurDisable() {
         //TalonFXConfiguration config = new TalonFXConfiguration();
         AudioConfigs config = new AudioConfigs();
         config.AllowMusicDurDisable = true;
         return config;
    }

}
