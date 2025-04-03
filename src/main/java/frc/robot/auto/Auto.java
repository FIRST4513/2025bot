package frc.robot.auto;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Robot;
import frc.robot.Robot.TeamAlliance;
import frc.robot.drivetrain.DrivetrainSubSys;
import frc.robot.subsystems.elevator.commands.ElevatorCmds;
import frc.robot.subsystems.finger.FingerCmds;
import frc.robot.subsystems.intake.commands.IntakeCmds;
import frc.util.FieldConstants;

public class Auto {
    public static final SendableChooser<String> actionChooser = new SendableChooser<>();
    public static final SendableChooser<String> positionChooser = new SendableChooser<>();

    public static String actionSelect;
    public static String positionSelect;
    private static Pose2d startPose;
    public static PathPlannerAuto autoCommand;
    
    static int oneEighty;
        // ----- Autonomous Subsystem Constructor -----
        public Auto() {
            configureAutoBuilder();
            registerNamedCommands();
            setupSelectors();                // Setup on screen slection menus
        }
    
        public static void setupSelectors() {
            // Selector for Robot Starting Position on field
            positionChooser.addOption("Left",        AutoConfig.kLeft);
            positionChooser.setDefaultOption(       "Center",      AutoConfig.kCenter);
            positionChooser.addOption(       "Right",       AutoConfig.kRight);
            // Selector for Autonomous Desired Action
            actionChooser.addOption(  "Do Nothing",          AutoConfig.kActionDoNothing);
            actionChooser.setDefaultOption(         "Crossline Only",      AutoConfig.kCrossOnlySelect);
            actionChooser.addOption(         "Line To Reef",            AutoConfig.kActionLineToReef);
            actionChooser.addOption("Level 4", AutoConfig.kActionLevel4);
        }
    
        // ------ Get operator selected responses from shuffleboard -----
        public static void getAutoSelections() {
            actionSelect =     actionChooser.getSelected();
            positionSelect =  positionChooser.getSelected();
            Robot.print("Action Select = " +     actionSelect);
            Robot.print("Position Select = " +     positionSelect);
        }
        
        public static Command getAutonomousCommand() {
            getAutoSelections();
            //setStartPose();                 // Initialize Robot Pose on Field
    

            // ------------------------------- Do Nothing ---------------------------
            if (doNothing()) {
                System.out.println("********* DO Nothing Selection *********");
                return AutoCmds.DoNothingCmd();
            }
            if (crossOnly()) { 
                if(Left()) {
                    return AutoCmds.followPath("CrossLeft");
                }
                if(Center()) {
                    return AutoCmds.followPath("CrossCenter");
                }
                if(Right()) {
                    return AutoCmds.followPath("CrossRight");
                }
                else {
                    return AutoCmds.DoNothingCmd();
                }
            }
            if (LineToReef()) {
                if(Left()) {
                    return new SequentialCommandGroup(
                        AutoCmds.followPath("LeftToFL"),
                        ElevatorCmds.elevatorSetLevelOne(),
                        new WaitCommand(.2),
                        IntakeCmds.intakeSetTreeCmd(),
                        new WaitCommand(1),
                        IntakeCmds.intakeSetStoppedCmd(),
                        ElevatorCmds.elevatorSetManual(),
                        new WaitCommand(.05),
                        ElevatorCmds.elevatorSetStopped()
                        );
                }
                if (Center()) {
                    return new SequentialCommandGroup( 
                        IntakeCmds.intakeSetHoldCmd(),
                        AutoCmds.followPath("CenterToFC").withTimeout(4.5),
                        ElevatorCmds.elevatorSetLevelOne(),
                        new WaitCommand(1.5),
                        IntakeCmds.intakeSetTreeCmd(),
                        new WaitCommand(0.7),
                        IntakeCmds.intakeSetStoppedCmd(),
                        ElevatorCmds.elevatorSetBottom(),
                        new WaitCommand(1),
                        ElevatorCmds.elevatorSetManual(),
                        new WaitCommand(0.03),
                        ElevatorCmds.elevatorSetStopped()
                    );
                }
                if(Right()) {
                    return new SequentialCommandGroup(
                    AutoCmds.followPath("RightToFR"),
                    ElevatorCmds.elevatorSetLevelOne(),
                    new WaitCommand(.2),
                    IntakeCmds.intakeSetTreeCmd(),
                    new WaitCommand(1),
                    IntakeCmds.intakeSetStoppedCmd(),
                    ElevatorCmds.elevatorSetManual(),
                    new WaitCommand(.05),
                    ElevatorCmds.elevatorSetStopped(),
                    AutoCmds.followPath("RightToIntake"),
                    ElevatorCmds.elevatorSetLevelTwo(),
                    IntakeCmds.intakeSetFeedCmd(),
                    new WaitCommand(3),
                    IntakeCmds.intakeSetHoldCmd(),
                    ElevatorCmds.elevatorSetBottom()
                    //new InstantCommand(()->Robot.swerve.setGyroHeading(oneEighty))
                    );
                }
                else {
                    return AutoCmds.DoNothingCmd();
                }
    
            }
            if (Level4()) {

                if (Center()) {
                    return new SequentialCommandGroup( 
                        IntakeCmds.intakeSetHoldCmd(),
                        AutoCmds.followPath("CenterToFC").withTimeout(4.5),
                        ElevatorCmds.elevatorSetLevelFour(),
                        new WaitCommand(1.5),
                        IntakeCmds.intakeSetTreeCmd(),
                        new WaitCommand(0.7),
                        IntakeCmds.intakeSetStoppedCmd(),
                        ElevatorCmds.elevatorSetBottom(),
                        new WaitCommand(1),
                        ElevatorCmds.elevatorSetManual(),
                        new WaitCommand(0.03),
                        ElevatorCmds.elevatorSetStopped()
                    );
                }
    
                if (Right()) {
                return new SequentialCommandGroup( 
                    IntakeCmds.intakeSetHoldCmd(),
                    new ParallelCommandGroup(
                        new SequentialCommandGroup(
                            new WaitCommand(1.25),
                            ElevatorCmds.elevatorSetLevelFour()
                        ),
                        AutoCmds.followPath("RightToFRS")
                    ),
                    new WaitCommand(0.25),
                    IntakeCmds.intakeSetTreeCmd(),
                    new WaitCommand(0.5),
                    IntakeCmds.intakeSetStoppedCmd(),
                    ElevatorCmds.elevatorSetBottom(),

                    new ParallelCommandGroup(
                        AutoCmds.followPath("RightToIntake"),
                        new SequentialCommandGroup(
                            new WaitCommand(2.5),
                            ElevatorCmds.elevatorSetIntake(),
                            IntakeCmds.intakeSetFeedCmd()
                        )
                    ),
                    new WaitUntilCommand(() -> Robot.intake.getGamepieceDetected()),
                    new WaitCommand(0.1),



                    new ParallelCommandGroup(
                        new SequentialCommandGroup(
                            new WaitCommand(2),
                            ElevatorCmds.elevatorSetLevelFour()
                        ),
                        AutoCmds.followPath("RIntakeToCR")
                    ),
                    new WaitCommand(0.3),
                    IntakeCmds.intakeSetTreeCmd(),
                    new WaitCommand(0.5),
                    IntakeCmds.intakeSetStoppedCmd(),
                    ElevatorCmds.elevatorSetIntake(),

                    new ParallelCommandGroup(
                    AutoCmds.followPath("CRToIntake"),
                    new SequentialCommandGroup(
                        new WaitCommand(0.5),
                        IntakeCmds.intakeSetFeedCmd()
                        )
                    ),
                    new WaitUntilCommand(() -> Robot.intake.getGamepieceDetected()),
                    new WaitCommand(0.1)
                );
            }
                if (Left()) {
                    return new SequentialCommandGroup( 
                    IntakeCmds.intakeSetHoldCmd(),
                    new ParallelCommandGroup(
                        new SequentialCommandGroup(
                            new WaitCommand(1.25),
                            ElevatorCmds.elevatorSetLevelFour()
                        ),
                        AutoCmds.followPath("LeftToFLS")
                    ),
                    new WaitCommand(0.25),
                    IntakeCmds.intakeSetTreeCmd(),
                    new WaitCommand(0.5),
                    IntakeCmds.intakeSetStoppedCmd(),
                    ElevatorCmds.elevatorSetBottom(),

                    new ParallelCommandGroup(
                        AutoCmds.followPath("LeftToIntake"),
                        new SequentialCommandGroup(
                            new WaitCommand(2.5),
                            ElevatorCmds.elevatorSetIntake(),
                            IntakeCmds.intakeSetFeedCmd()
                        )
                    ),
                    new WaitUntilCommand(() -> Robot.intake.getGamepieceDetected()),
                    new WaitCommand(0.1),



                    new ParallelCommandGroup(
                        new SequentialCommandGroup(
                            new WaitCommand(2),
                            ElevatorCmds.elevatorSetLevelFour()
                        ),
                        AutoCmds.followPath("LIntakeToCL")
                    ),
                    new WaitCommand(0.3),
                    IntakeCmds.intakeSetTreeCmd(),
                    new WaitCommand(0.5),
                    IntakeCmds.intakeSetStoppedCmd(),
                    ElevatorCmds.elevatorSetIntake(),

                    new ParallelCommandGroup(
                    AutoCmds.followPath("CLToIntake"),
                    new SequentialCommandGroup(
                        new WaitCommand(0.5),
                        IntakeCmds.intakeSetFeedCmd()
                        )
                    ),
                    new WaitUntilCommand(() -> Robot.intake.getGamepieceDetected()),
                    new WaitCommand(0.1)
                );

                }

        }
            if (TwoPiece()) {
                return new SequentialCommandGroup(
                    IntakeCmds.intakeSetHoldCmd(),
                    AutoCmds.followPath("CenterToFC"),
                    ElevatorCmds.elevatorSetLevelFour(),
                    new WaitCommand(1.5),
                    IntakeCmds.intakeSetTreeCmd(),
                    new WaitCommand(0.5),
                    IntakeCmds.intakeSetStoppedCmd(),
                    ElevatorCmds.elevatorSetBottom(),
                    new WaitCommand(1),
                    new ParallelCommandGroup(
                        new SequentialCommandGroup(
                            ElevatorCmds.elevatorSetManual(),
                            new WaitCommand(0.2),
                            ElevatorCmds.elevatorSetLevelTwo()
                        ),
                        AutoCmds.followPath("CenterToIntake")
                    ),
                    IntakeCmds.intakeSetFeedCmd(),
                    new WaitCommand(1),
                    IntakeCmds.intakeSetStoppedCmd(),
                    new ParallelCommandGroup(
                        new SequentialCommandGroup(
                            ElevatorCmds.elevatorSetManual(),
                            new WaitCommand(0.2),
                            ElevatorCmds.elevatorSetBottom()
                        ),
                        AutoCmds.followPath("RIntakeToCR")
                    ),
                    ElevatorCmds.elevatorSetLevelFour(),
                    new WaitCommand(1.5),
                    IntakeCmds.intakeSetTreeCmd(),
                    new WaitCommand(1),
                    IntakeCmds.intakeStopCmd(),
                    ElevatorCmds.elevatorSetManual(),
                    new WaitCommand(0.2),
                    ElevatorCmds.elevatorSetBottom()
                );
            }


        // should never get here
        return AutoCmds.DoNothingCmd();
    }

    // ----- Configuration and Setup Methods -----

    static RobotConfig config;{
    try{
      config = RobotConfig.fromGUISettings();
    } catch (Exception e) {
      // Handle exception as needed
      e.printStackTrace();
    }
}

    // Configures the auto builder to use to run paths in autonomous and in teleop
    public static void configureAutoBuilder() {
        // Configure the AutoBuilder settings
        AutoBuilder.configure(
            Robot.swerve::getPose,                // Supplier<Pose2d> ------------> Robot pose supplier
            Robot.swerve::resetPose,              // Consumer<Pose2d> ------------> Method to reset odometry (will be called if your auto has a starting pose)
            Robot.swerve::getChassisSpeeds,       // Supplier<ChassisSpeeds> -----> MUST BE ROBOT RELATIVE
            Robot.swerve::driveByChassisSpeeds,   // Consumer<ChassisSpeeds> -----> Set robot relative speeds (drive)
            new PPHolonomicDriveController(
                 new PIDConstants(3.5, 0.75, 0.7555), // Translation PID constants 1, 0.55, 0.7555
                 new PIDConstants(1.5, 0.5, 0.5) // Rotation PID constants
            ),    // HolonomicPathFollowerConfig -> config for configuring path commands
            config,
            ()->getAllianceFlip(),                // BooleanSupplier -------------> Should mirror/flip path
            //() -> false,                        // BooleanSupplier -------------> Should mirror/flip path
            Robot.swerve                          // Subsystem: ------------------> required subsystem (usually swerve)
        );
    }

    public static Boolean getAllianceFlip(){
        // Boolean supplier that controls when the path will be mirrored for the red alliance
        // This will flip the path being followed to the red side of the field.

        if ((DriverStation.isAutonomous()) && (Robot.alliance == TeamAlliance.RED) ) {
            // We're in Autonomous Mode and Alliance is Red, so invert field
            return true;
        } else {
        // Not Autonomous or Alliance is Blue so dont invert field
        return false;
        }
    }


    // Setup Named Commands for Events in PathPlanner To Call
    private static void registerNamedCommands() {
        // an example named command
        NamedCommands.registerCommand("MidPoint", Commands.print("Midpoint reached!!"));
    }

    // ------------------------------------------------------------------------
    //     Setup proper Arm/Elevator position to Place Cone/Cube
    // ------------------------------------------------------------------------

    public static void setStartPose() {
        // Set Robot position (Odometry) and Heading (Gyro) based on selected autonomous starting position
        //startPose = new Pose2d(new Translation2d(0,0), new Rotation2d(0)); // Should never use
        double gyroHeading = 0;
            Robot.print("1. We are Blue");
            if (Left())         {
                 startPose = FieldConstants.BLUE_CAGE_BLUE;  
                 gyroHeading = FieldConstants.BLUE_CAGE_BLUE_GYRO;
                 Robot.print("2. We are Blue Cage Blue"); }
            
             if (Center())          { 
                startPose = FieldConstants.CENTER_PILLAR_BLUE;   
                gyroHeading = FieldConstants.CENTER_PILLAR_BLUE_GYRO;
                Robot.print("2. We are Center Pillar Blue"); }
            
            if (Right())        {
                startPose = FieldConstants.RED_CAGE_BLUE; 
                gyroHeading = FieldConstants.RED_CAGE_BLUE_GYRO;
                Robot.print("2. We are Red Cage Blue"); }
        
        // Robot.swerve.resetOdometryAndGyroFromPose(startPose);
        Robot.swerve.resetOdometryAndGyroFromPose(startPose, gyroHeading);
            }


    // ------------------------------------------------------------------------
    //            Simple Checks to make above routines cleaner
    // ------------------------------------------------------------------------
    private static boolean doNothing() {
        if (actionSelect.equals(AutoConfig.kActionDoNothing)) { return true; }
        return false;
    }
    private static boolean LineToReef() {
        if (actionSelect.equals(AutoConfig.kActionLineToReef)) { return true; }
        return false;
    }
   
    private static boolean crossOnly() {
        if (actionSelect.equals(AutoConfig.kCrossOnlySelect)) { return true; }
        return false;
    }
    
    private static boolean Level4() {
        if (actionSelect.equals(AutoConfig.kActionLevel4)) { return true; }
        return false;
    }
    
    private static boolean TwoPiece() {
        if (actionSelect.equals(AutoConfig.kActionTwoPiece)) { return true; } 
        return false;
    }
    
    
    private static boolean red() {
        if (Robot.alliance == TeamAlliance.RED) { return true; }
        return false;
    }

    // private static boolean blue() {
    //     if (Robot.alliance == "Blue") { return true; }
    //     return false;
    // }

    private static boolean Left() {
        if (positionSelect.equals(AutoConfig.kLeft)) { return true; }
        return false;
    }
    private static boolean Center() {
        if (positionSelect.equals(AutoConfig.kCenter)) { return true; }
        return false;
    }
    private static boolean Right() {
        if (positionSelect.equals(AutoConfig.kRight)) { return true; }
        return false;
    }

    private static void onTrigger() {
        autoCommand = new PathPlannerAuto("VisionToFRS");
        autoCommand.event("raiseToL4").onTrue(ElevatorCmds.elevatorSetLevelFour());


    }

}
