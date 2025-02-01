package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Threads;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.XBoxCtrlrs.operator.OperatorGamepad;
import frc.robot.XBoxCtrlrs.pilot.PilotGamepad;
import frc.robot.XBoxCtrlrs.pilot.commands.PilotGamepadCmds;
import frc.robot.drivetrain.DrivetrainSubSys;
import frc.robot.drivetrain.commands.DrivetrainCmds;
import frc.robot.drivetrain.commands.SwerveDriveCmd;
import frc.robot.subsystems.climber.ClimberSubSys;
import frc.robot.subsystems.climber.commands.ClimberCmds;
import frc.robot.subsystems.intake.IntakeSubSys;
// import frc.robot.mechanisms.leds.LEDs;
// import frc.robot.mechanisms.leds.LEDsCommands;
import edu.wpi.first.wpilibj.util.Color;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;


// ------------------- Constructor -----------------
public class Robot extends LoggedRobot  {
    public static Timer sysTimer = new Timer();

    public enum RobotIdentity {
        ROBOT_2023_SWERVE,
        KACHOW_2024_COMPETITION,
        KACHOW_2024_SECOND,
        UNKNOWN;
    }
    public static final String ROBOT_2023_SWERVE_MAC = "00-80-2F-35-B9-60";
    public static final String KACHOW_2024_COMPETITION_MAC = "10-50-FD-C6-35-0D";
    public static final String KACHOW_2024_SECOND_MAC = "92-9B-20-68-07-62";

    public enum TeamAlliance {
        RED,
        BLUE,
        NONE;
    }
    public static TeamAlliance alliance;

    // Base Robot
    public static DrivetrainSubSys  swerve;
    public static PilotGamepad      pilotGamepad;
    public static OperatorGamepad operatorGamepad;
    public static IntakeSubSys intake;
    // Automation and Assists
    // public static VisionSubSys      vision;

    // Game Piece Manipulation


    // Misc
    public static ClimberSubSys climber;
    // public static RotarySwitchSubSys rotarySwitch;

    public static RobotTelemetry    telemetry;          // Telemetry (MUST BE LAST)

    public static String MAC = "";

    // -----------------  Robot General Methods ------------------
    @Override
    public void robotInit() {
        sysTimer.reset();			// System timer for Competition run
        sysTimer.start();
        //updateAlliance();           // Get current Alliance Color and init teleop positions
        Timer.delay( 2.0 );         // Delay for 2 seconds for robot to come fully up
        // getIdentity();          // Look up mac address and set robot enum
        //MAC = Network.getMACaddress();
       
        intializeSubsystems();
        
        DataLogManager.start();
        DriverStation.startDataLog(DataLogManager.getLog());
        initAdvantageKitLogger();   // This logger replaces the WPI Data logger methods
    }

    @Override
    public void robotPeriodic() {
        // Ensures that the main thread is the highest priority thread
        Threads.setCurrentThreadPriority(true, 99);
        CommandScheduler.getInstance().run();       // Make sure scheduled commands get run
        Threads.setCurrentThreadPriority(true, 10); // Set the main thread back to normal priority
    }

    private void intializeSubsystems() {
        Timer.delay(1);
        // Automation and Assists
        // vision =   new VisionSubSys();
        
        // Base Robot
        swerve = new DrivetrainSubSys();

        pilotGamepad = new PilotGamepad();
        operatorGamepad = new OperatorGamepad();
        // rotarySwitch = new RotarySwitchSubSys(); 
        climber = new ClimberSubSys();
        intake = new IntakeSubSys();

        // Telemetry (MUST BE LAST)
        telemetry = new RobotTelemetry();
        // Set Default Commands, this method should exist for each subsystem that has commands
        DrivetrainCmds.setupDefaultCommand();
        ClimberCmds.setupDefaultCommand();
        PilotGamepadCmds.setupDefaultCommand();
        // ShooterCmds.setupDefaultCommand();
        // LEDsCommands.setupDefaultCommand();
        // Auto.setupSelectors();
    }
    

    // -----------------  Robot Disabled Mode Methods ------------------
    @Override
    public void disabledInit() {
        resetCommandsAndButtons();
    }

    @Override
    public void disabledPeriodic()  { 
        // Run LED Lights based on switch
        // int m_rotarySwitch = rotarySwitch.GetRotaryPos();
    
        // if      ( m_rotarySwitch == 1) leds.solid( 0.75, Color.kRed,    2);
        // else if ( m_rotarySwitch == 2) leds.solid( 0.75, Color.kGreen,  2);
        // else if ( m_rotarySwitch == 3) leds.solid( 0.75, Color.kBlue,   2);
        // else if ( m_rotarySwitch == 4) leds.solid( 0.75, Color.kOrange, 2);
        // else if ( m_rotarySwitch == 5) leds.solid( 0.75, Color.kPurple, 2);
        // else if ( m_rotarySwitch == 6) leds.solid( 0.75, Color.kAqua,   2);
        // // Otherwise turn off leds
        // else                           leds.solid( 0.75, Color.kBlack,  2);
        /*
        if (alliance == TeamAlliance.BLUE) {
            leds.solid(Section.all, Color.kBlue);
        } else if (alliance == TeamAlliance.RED) {
            leds.solid(Section.all, Color.kRed);
        } else {
            leds.wave(Section.all, Color.kBlue, Color.kRed, LEDsConfig.length, LEDsConfig.waveSlowDuration);
        }
        */ 
        updateAlliance();
    }

    @Override
    public void disabledExit() {}


    // -----------------  Autonomous Mode Methods ------------------
    @Override
    public void autonomousInit() {
        updateAlliance();           // Get current Alliance Color and init teleop positions       
        sysTimer.reset();			// System timer for Competition run
    	sysTimer.start();
        System.out.println("Starting Auto Init");
        resetCommandsAndButtons();

        // swerve.setLastAngleToCurrentAngle();

        // Set Climbers to go to bottom no matter what
        
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void autonomousExit() {}


    // -----------------  TeleOp Mode Methods ------------------
    @Override
    public void teleopInit() {
        updateAlliance();           // Get current Alliance Color and init teleop positions
        pilotGamepad.setMaxSpeeds(pilotGamepad.getSelectedSpeed());
        pilotGamepad.setupTeleopButtons();
        resetCommandsAndButtons();
        
 
        
        // Set Climbers to go to bottom no matter what
    }

    @Override
    public void teleopPeriodic() {

    }

    @Override
    public void teleopExit() {}


    // -----------------  Test Mode Methods ------------------
    @Override
    public void testInit() {
        updateAlliance();           // Get current Alliance Color and init teleop positions
        resetCommandsAndButtons();
    }

    @Override
    public void testPeriodic() {}


    // -----------------  Simulation Mode Methods ------------------
    public void simulationInit() {}

    public void simulationPeriodic() {}

    // ------------------------  Misc Methods ---------------------
    public static void resetCommandsAndButtons() {

        CommandScheduler.getInstance().cancelAll();  // Disable any currently running commands
        CommandScheduler.getInstance().getActiveButtonLoop().clear();
        pilotGamepad.resetConfig();  // Reset Config for all gamepads and other button bindings
    }


    /** This method is called once at the end of RobotInit to begin logging */
    private void initAdvantageKitLogger(){
        /* Set up data receivers & replay source */
        Logger.addDataReceiver(new NT4Publisher()); // Running a physics simulator, log to NT
        if (!Robot.isSimulation()) {
            Logger.addDataReceiver(
                    new WPILOGWriter("/U")); // Running on a real robot, log to a USB stick
        }
        Logger.recordMetadata("ProjectName", "Kachow2024Ver04");    // Set a metadata value
        //logger.recordMetadata("Robot Name", RobotIdentity.getIdentity().toString());
        //logger.recordMetadata("Robot MAC Address", MacAddressUtil.getMACAddress());
        Logger.start();         // Start AdvantageKit logger


        setUseTiming(isReal());
        Logger.recordMetadata("ProjectName", "Kachow2024Ver04");    // Set a metadata value
        //logger.recordMetadata("Robot Name", RobotIdentity.getIdentity().toString());
        //logger.recordMetadata("Robot MAC Address", MacAddressUtil.getMACAddress());
        if (isReal()) {
            Logger.addDataReceiver(new WPILOGWriter()); // Log to a USB stick ("/U/logs")
            Logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
            //new PowerDistribution(1, ModuleType.kRev); // Enables power distribution logging
        } else {
            setUseTiming(false); // Run as fast as possible
            String logPath = LogFileUtil.findReplayLog(); // Pull the replay log from AdvantageScope (or prompt the user)
            Logger.setReplaySource(new WPILOGReader(logPath)); // Read replay log
            Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim"))); // Save outputs to a new log
        }
        Logger.start(); // Start logging! No more data receivers, replay sources, or metadata values may be added.
        // ----------------------------------------------------------------------------------------------------------
    }

    public void updateAlliance(){
        // Get current Alliance Color and init teleop positions
        Optional<Alliance> ally = DriverStation.getAlliance();
        if (ally.isPresent()) {
            if (ally.get() == Alliance.Red)  { alliance = TeamAlliance.RED; }
            if (ally.get() == Alliance.Blue) { alliance = TeamAlliance.BLUE; }
        } else {
            alliance = TeamAlliance.NONE;
        }
    }

    public static void print(String toPrint) {
        System.out.println( "-----------  " + toPrint + "  ---------------  " + Robot.sysTimer.get());
    }

}
