package frc.robot;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RuntimeType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.XBoxCtrlrs.operator.OperatorGamepad;
import frc.robot.XBoxCtrlrs.operator.commands.OperatorGamepadCmds;
import frc.robot.XBoxCtrlrs.pilot.PilotGamepad;
import frc.robot.XBoxCtrlrs.pilot.commands.PilotGamepadCmds;
import frc.robot.auto.Auto;
import frc.robot.drivetrain.DrivetrainSubSys;
import frc.robot.drivetrain.commands.DrivetrainCmds;
import frc.robot.subsystems.climber.ClimberSubSys;
import frc.robot.subsystems.climber.commands.ClimberCmds;
import frc.robot.subsystems.elevator.ElevatorSubSys;
import frc.robot.subsystems.finger.FingerSubSys;
import frc.robot.subsystems.intake.IntakeSubSys;
import org.littletonrobotics.junction.LoggedRobot;


import java.util.Optional;


// ------------------- Constructor -----------------
public class Robot extends LoggedRobot {

    private Vision vision;

    public static Timer sysTimer = new Timer();

    // Base Robot
    public static DrivetrainSubSys swerve;
    public static PilotGamepad pilotGamepad;
    public static OperatorGamepad operatorGamepad;
    public static IntakeSubSys intake;
    public static FingerSubSys finger;
    public static ClimberSubSys climber;
    public static ElevatorSubSys elevator;
    public static Auto auto;

    // -----------------  Robot General Methods ------------------
    @Override
    public void robotInit() {


        Timer.delay(2.0);         // Delay for 2 seconds for robot to come fully up
        vision = new Vision();
        swerve = new DrivetrainSubSys();

        pilotGamepad = new PilotGamepad();
        operatorGamepad = new OperatorGamepad();
        climber = new ClimberSubSys();
        intake = new IntakeSubSys();
        finger = new FingerSubSys();
        elevator = new ElevatorSubSys();

        DrivetrainCmds.setupDefaultCommand();
        ClimberCmds.setupDefaultCommand();
        PilotGamepadCmds.setupDefaultCommand();
        OperatorGamepadCmds.setupDefaultCommand();

        DataLogManager.start();
        DriverStation.startDataLog(DataLogManager.getLog());
        //initAdvantageKitLogger();   // This logger replaces the WPI Data logger methods
    }

    /**
     * Get if the robot is a simulation.
     *
     * @return If the robot is running in simulation.
     */
    public static boolean isSimulation() {
        return getRuntimeType() == RuntimeType.kSimulation;
    }

    /**
     * Get if the robot is real.
     *
     * @return If the robot is running in the real world.
     */
    public static boolean isReal() {
        RuntimeType runtimeType = getRuntimeType();
        return runtimeType == RuntimeType.kRoboRIO || runtimeType == RuntimeType.kRoboRIO2;
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();       // Make sure scheduled commands get run
        var visionEst = vision.getEstimatedGlobalPose();
        visionEst.ifPresent(
                est -> {
                    // Change our trust in the measurement based on the tags we can see
                    var estStdDevs = vision.getEstimationStdDevs();

                    swerve.addVisionMeasurement(
                            est.estimatedPose.toPose2d(), est.timestampSeconds, estStdDevs);
                });
    }

    // -----------------  Robot Disabled Mode Methods ------------------
    @Override
    public void disabledInit() {
        CommandScheduler.getInstance().cancelAll();  // Disable any currently running commands
        CommandScheduler.getInstance().getActiveButtonLoop().clear();
        pilotGamepad.resetConfig();  // Reset Config for all gamepads and other button bindings
        operatorGamepad.resetConfig();
    }

    // -----------------  Autonomous Mode Methods ------------------
    @Override
    public void autonomousInit() {

        CommandScheduler.getInstance().cancelAll();  // Disable any currently running commands
        CommandScheduler.getInstance().getActiveButtonLoop().clear();
        pilotGamepad.resetConfig();  // Reset Config for all gamepads and other button bindings
        operatorGamepad.resetConfig();
        Command autoCommand = Auto.getAutonomousCommand();
        if (autoCommand != null) {
            System.out.println("Auto Command Not null");
            autoCommand.schedule();
        } else {
            System.out.println("********** Auto Command NULL ************");
        }
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void autonomousExit() {
    }


    // -----------------  TeleOp Mode Methods ------------------
    @Override
    public void teleopInit() {
        CommandScheduler.getInstance().cancelAll();  // Disable any currently running commands
        CommandScheduler.getInstance().getActiveButtonLoop().clear();
        pilotGamepad.resetConfig();  // Reset Config for all gamepads and other button bindings
        operatorGamepad.resetConfig();
        pilotGamepad.setMaxSpeeds(pilotGamepad.getSelectedSpeed());
        pilotGamepad.setupTeleopButtons();
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void teleopExit() {
    }

    // -----------------  Simulation Mode Methods ------------------
    public void simulationInit() {
    }

    public void simulationPeriodic() {
    }

    public Optional<Alliance> getAlliance() {
        return DriverStation.getAlliance();
    }

    public static void print(String toPrint) {
        System.out.println("-----------  " + toPrint + "  ---------------  " + Robot.sysTimer.get());
    }

}
