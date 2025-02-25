package frc.robot.subsystems.orchestra;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.AudioConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.drivetrain.DrivetrainSubSys;
import frc.robot.drivetrain.config.DrivetrainConfig;
import frc.robot.subsystems.climber.ClimberSubSys;
import frc.robot.subsystems.elevator.ElevatorSubSys;

public class orchestraSubSys {
        
    public static Orchestra m_orchestra = new Orchestra();
    
    public orchestraSubSys() {
        m_orchestra.addInstrument(ElevatorSubSys.elevatorMotor, 0);
        //m_orchestra.addInstrument(ClimberSubSys.climberMotor, 0);

        m_orchestra.addInstrument(DrivetrainSubSys.swerveMods[0].getTalonDrive(), 0);
        m_orchestra.addInstrument(DrivetrainSubSys.swerveMods[0].getTalonAngle(), 0);

        m_orchestra.addInstrument(DrivetrainSubSys.swerveMods[1].getTalonDrive(), 0);
        m_orchestra.addInstrument(DrivetrainSubSys.swerveMods[1].getTalonAngle(), 0);

        m_orchestra.addInstrument(DrivetrainSubSys.swerveMods[2].getTalonDrive(), 0);
        m_orchestra.addInstrument(DrivetrainSubSys.swerveMods[2].getTalonAngle(), 0);

        m_orchestra.addInstrument(DrivetrainSubSys.swerveMods[3].getTalonDrive(), 0);
        m_orchestra.addInstrument(DrivetrainSubSys.swerveMods[3].getTalonAngle(), 0);
    }

        static String[] songs = new String[] {
            "FurElise.chrp",
            "Doom.chrp",
            "Rolled.chrp",
            "tetris.chrp",
            "BeatIt.chrp",
            "CruelAngelsThesis.chrp",
            "GangstasParadise.chrp",
            "Viva.chrp",
            "LavenderTown.chrp",
            "GreenHillZone.chrp",
            
            "Creep.chrp"
        };

    static int songSelection = 0;

    public static Command loadSong() {
        return new InstantCommand(()->m_orchestra.loadMusic(songs[songSelection]));
    }
    public static Command playCmd() {
        return new InstantCommand(()-> m_orchestra.play());
    }

    public static Command playsong() {
        return new SequentialCommandGroup(loadSong(), playCmd());
    }
        
        static void iterateUp() {songSelection = songSelection+1;}
        static void iterateDown() {songSelection = songSelection-1;}
        
        public static Command songUp() {
            return new InstantCommand(()->iterateUp());
        }
        
        public static Command songDown() {
            return new InstantCommand(()->iterateDown());
        }



    public AudioConfigs withAllowMusicDurDisable() {
         //TalonFXConfiguration config = new TalonFXConfiguration();
         AudioConfigs config = new AudioConfigs();
         config.AllowMusicDurDisable = true;
         return config;
    }

}
