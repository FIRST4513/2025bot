package frc.robot.subsystems.orchestra;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class orchestraCmds {
    
    public static void playSong() {
        orchestraSubSys.m_orchestra.play();
    }
    public static Command playTetris() {
        return new InstantCommand(()->orchestraSubSys.m_orchestra.play());
    }
    public static Command stop() {
        return new InstantCommand(()->orchestraSubSys.m_orchestra.stop());
    }
}
