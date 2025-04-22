package frc.robot;

import java.util.Formatter;
import java.util.Optional;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Threads;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.XBoxCtrlrs.operator.OperatorGamepad;
import frc.robot.XBoxCtrlrs.operator.commands.OperatorGamepadCmds;
import frc.robot.XBoxCtrlrs.pilot.PilotGamepad;
import frc.robot.XBoxCtrlrs.pilot.commands.PilotGamepadCmds;
import frc.robot.auto.Auto;
import frc.robot.auto.AutoTelemetry;
import frc.robot.drivetrain.DrivetrainSubSys;
import frc.robot.drivetrain.commands.DrivetrainCmds;
import frc.robot.subsystems.climber.ClimberSubSys;
import frc.robot.subsystems.climber.commands.ClimberCmds;
import frc.robot.subsystems.elevator.ElevatorSubSys;
import frc.robot.subsystems.finger.FingerSubSys;
import frc.robot.subsystems.intake.IntakeSubSys;
import frc.robot.subsystems.orchestra.orchestraSubSys;
import frc.robot.subsystems.vision.Vision;

import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
import au.grapplerobotics.CanBridge;
import au.grapplerobotics.LaserCan;


// ------------------- Constructor -----------------
public class Robot extends LoggedRobot  {

    static double targetYaw;
            
    
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
        public static LaserCan.Measurement measurement;
    
        // Base Robot
        public static DrivetrainSubSys  swerve;
        public static PilotGamepad      pilotGamepad;
        public static OperatorGamepad   operatorGamepad;
        public static IntakeSubSys intake;
        public static FingerSubSys finger;
        public static ClimberSubSys climber;
        public static ElevatorSubSys elevator;
        public static Auto auto;
        public static Vision vision;
        public static AutoTelemetry autoTelemetry;
        
            //public static orchestraSubSys orchestra;
            // Automation and Assists
            // public static VisionSubSys      vision;
        
            // Game Piece Manipulation
        
        
            // Misc
            
            // public static RotarySwitchSubSys rotarySwitch;
        
            public static RobotTelemetry    telemetry;          // Telemetry (MUST BE LAST)
        
            public static String MAC = "";
        
            // -----------------  Robot General Methods ------------------
            @Override
            public void robotInit() {
                
                //Robot.lc = new LaserCan(25);
            
            CanBridge.runTCP();
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
            autoTelemetry = new AutoTelemetry();
        }
    
        @Override
        public void robotPeriodic() {
            // Ensures that the main thread is the highest priority thread
            Threads.setCurrentThreadPriority(true, 99);
            CommandScheduler.getInstance().run();       // Make sure scheduled commands get run
            Threads.setCurrentThreadPriority(true, 10); // Set the main thread back to normal priority
            //Robot.measurement = lc.getMeasurement();
    
            //Robot.print(Double.toString(ElevatorSubSys.getRotations()));


            var visionEst = vision.getEstimatedGlobalPose(Vision.camera);
            var topVisionEst = vision.getEstimatedGlobalPose(Vision.topcamera);
    
            visionEst.ifPresent(
                    est -> {
                        //Robot.print("BOTTOM POSE PRESENT");
                        // Change our trust in the measurement based on the tags we can see
                        var estStdDevs = vision.getEstimationStdDevs();
    
                        frc.robot.drivetrain.OdometryThread.printAndReset(
                            est.estimatedPose.toPose2d(), est.timestampSeconds, estStdDevs
                        );
                        //frc.robot.drivetrain.OdometryThread.m_odometry.addVisionMeasurement(
                                //est.estimatedPose.toPose2d(), est.timestampSeconds, estStdDevs);
                    });
            if (visionEst.isEmpty()) {
                //Robot.print("hello");
            }
            topVisionEst.ifPresent(
                est -> {
                    
                    Robot.print("TOP POSE PRESENT");
                    // Change our trust in the measurement based on the tags we can see
                    var estStdDevs = vision.getEstimationStdDevs();

                    frc.robot.drivetrain.OdometryThread.printAndReset(
                        est.estimatedPose.toPose2d(), est.timestampSeconds, estStdDevs
                    );
                    //frc.robot.drivetrain.OdometryThread.m_odometry.addVisionMeasurement(
                            //est.estimatedPose.toPose2d(), est.timestampSeconds, estStdDevs);
                });
            if (visionEst.isEmpty()) {
                //Robot.print("hello");
            }
            autoTelemetry.update();
        }
            
    
        private void intializeSubsystems() {
            Timer.delay(1);
            // Automation and Assists
            // vision =   new VisionSubSys();
            
            // Base Robot
            swerve = new DrivetrainSubSys();
    
            pilotGamepad = new PilotGamepad();
            operatorGamepad = new OperatorGamepad();
            auto = new Auto();
            // rotarySwitch = new RotarySwitchSubSys(); 
            climber = new ClimberSubSys();
            intake = new IntakeSubSys();
            finger = new FingerSubSys();
            elevator = new ElevatorSubSys();
            vision = new Vision();
    
            //orchestra = new orchestraSubSys();
            // Telemetry (MUST BE LAST)
            telemetry = new RobotTelemetry();
            // Set Default Commands, this method should exist for each subsystem that has commands
            DrivetrainCmds.setupDefaultCommand();
            ClimberCmds.setupDefaultCommand();
            PilotGamepadCmds.setupDefaultCommand();
            OperatorGamepadCmds.setupDefaultCommand();
            // ShooterCmds.setupDefaultCommand();
            // LEDsCommands.setupDefaultCommand();
            //Auto.setupSelectors();
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
            Command autoCommand = Auto.getAutonomousCommand();
            if (autoCommand != null) {
                System.out.println("Auto Command Not null");
                autoCommand.schedule();
            } else {
                System.out.println("********** Auto Command NULL ************");
            }
    
            //swerve.setLastAngleToCurrentAngle();
    
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
    
            // boolean targetVisible = false;
            // targetYaw = 0.0;
            // var results = Vision.camera.getAllUnreadResults();
            // if (!results.isEmpty()) {
            //     // Camera processed a new frame since last
            //     // Get the last one in the list.
            //     var result = results.get(results.size() - 1);
            //     if (result.hasTargets()) {
            //         // At least one AprilTag was seen by the camera
            //         for (var target : result.getTargets()) {
            //             if (target.getFiducialId() == 21) {
            //                 Robot.print("SEEING TAG 21");
            //                 // Found Tag 7, record its information
            //                 targetYaw = target.getYaw();
            //                 if (Math.abs(targetYaw) < 0.6) {
            //                     targetYaw=0;
            //                 }
            //                 //Robot.print(Double.toString(targetYaw));
            //                 targetVisible = true;
            //             }
            //         }
            //     }
            // }
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
            operatorGamepad.resetConfig();
        }
    
        public static int getDistanceMM() {
            return measurement.distance_mm;
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
    
        public static double getTargetYaw() {
            return targetYaw;
    }

}
