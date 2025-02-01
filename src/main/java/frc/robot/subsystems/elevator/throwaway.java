package frc.robot.subsystems.elevator;

import org.ironmaple.simulation.IntakeSimulation.IntakeSide;

import frc.robot.subsystems.intake.IntakeConfig;
import frc.robot.subsystems.intake.IntakeSubSys;
import frc.robot.subsystems.intake.IntakeSubSys.IntakeState;

public class throwaway {
    public static double level = 1;
    public static double speed = 0;
    
    public static double setlevel1() {
        level = 1;
        speed = IntakeConfig.TROPH;
        return speed;
    }
}
