package frc.robot.subsystems.orchestra;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.AudioConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.elevator.ElevatorSubSys;

public class orchestraSubSys {
    public static Orchestra m_orchestra;
    public orchestraSubSys() {
        Orchestra m_orchestra = new Orchestra();

        // Add a single device to the orchestra
        m_orchestra.addInstrument(ElevatorSubSys.elevatorMotor);

        // Attempt to load the chrp
        var status = m_orchestra.loadMusic("output.chrp");
        withAllowMusicDurDisable();
        
        
    }

    public AudioConfigs withAllowMusicDurDisable() {
         //TalonFXConfiguration config = new TalonFXConfiguration();
         AudioConfigs config = new AudioConfigs();
         config.AllowMusicDurDisable = true;
         return config;
    }

    public static void playSong() {
        m_orchestra.play();
    }
}
