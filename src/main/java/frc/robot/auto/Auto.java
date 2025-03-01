package frc.robot.auto;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
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
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.Robot.TeamAlliance;
import frc.robot.subsystems.elevator.commands.ElevatorCmds;
import frc.robot.subsystems.intake.commands.IntakeCmds;
import frc.util.FieldConstants;

public class Auto {
    public static final SendableChooser<String> actionChooser = new SendableChooser<>();
    public static final SendableChooser<String> positionChooser = new SendableChooser<>();

    public static String actionSelect;
    public static String positionSelect;
    private static Pose2d startPose;
    
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
            SmartDashboard.putData(positionChooser);
            // Selector for Autonomous Desired Action
            actionChooser.addOption(  "Do Nothing",          AutoConfig.kActionDoNothing);
            actionChooser.addOption(         "Crossline Only",      AutoConfig.kCrossOnlySelect);
            actionChooser.setDefaultOption(         "Line To Reef",            AutoConfig.kActionLineToReef);
            actionChooser.addOption("Right to Score", AutoConfig.kActionRightToScore);
            SmartDashboard.putData(actionChooser);
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
            setStartPose();                 // Initialize Robot Pose on Field
    
            if (getAllianceFlip()) {
                oneEighty = 180;
            }
            if (!getAllianceFlip()) {
                oneEighty = 0;
            }


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
                if(Center()) {
                    return new SequentialCommandGroup(
                        AutoCmds.followPath("CenterToFC"),  
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
                if(Right()) {
                    return new SequentialCommandGroup(
                    AutoCmds.followPath("RightToFRS"),
                    ElevatorCmds.elevatorSetLevelOne(),
                    new WaitCommand(.2),
                    IntakeCmds.intakeSetTreeCmd(),
                    new WaitCommand(1),
                    IntakeCmds.intakeSetStoppedCmd(),
                    ElevatorCmds.elevatorSetManual(),
                    new WaitCommand(.05),
                    ElevatorCmds.elevatorSetStopped(),
                    AutoCmds.followPath("RightToIntake"),
                    new InstantCommand(()->Robot.swerve.setGyroHeading(oneEighty))
                    );
                }
                else {
                    return AutoCmds.DoNothingCmd();
                }
    
            }
            if (RightToScore()) {

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
                    AutoCmds.followPath("RightToFRS").withTimeout(4.5),
                    ElevatorCmds.elevatorSetLevelFour(),
                    new WaitCommand(1.5),
                    IntakeCmds.intakeSetTreeCmd(),
                    new WaitCommand(0.7),
                    IntakeCmds.intakeSetStoppedCmd(),
                    ElevatorCmds.elevatorSetBottom(),
                    new WaitCommand(1),
                    ElevatorCmds.elevatorSetManual(),
                    new WaitCommand(0.03),
                    ElevatorCmds.elevatorSetStopped(),
                    AutoCmds.followPath("RightToIntake"),
                    new InstantCommand(()->Robot.swerve.setGyroHeading(oneEighty))

                );
            }

        }




        // ------------------------------ Three Note  -------------------------------
        /*if (threeNote()) {
            System.out.println("********* Three Selection *********");
            if (red()) {
                Robot.print("REEDDDDDD");
                if ( spkrLeft() )           { return AutoCmds.ShootAndCrossCmd("Left", "RedSpkrLeft"); }
                if ( spkrCtr() )            { return AutoCmds.ThreeNoteCmd("Ctr", "RedSpkrCtr2ndNote", "RedSpkrCtr2ndNoteReturn", "RedSpkrCtr", "RedSpkrCtrReturn"); }
                if ( spkrRight() )          { return AutoCmds.ShootAndCrossCmd("Right", "RedSpkrRight"); }
            } else {
                Robot.print("BLUEEEEE");
                if ( spkrLeft() )           { return AutoCmds.ShootAndCrossCmd("Left", "BlueSpkrLeft" ); }
                if ( spkrCtr() )            { return AutoCmds.ThreeNoteCmd("Ctr", "BlueSpkrCtr2ndNote", "BlueSpkrCtr2ndNoteReturn", "BlueSpkrCtr", "BlueSpkrCtrReturn"); }
                if ( spkrRight() )          { return AutoCmds.ShootAndCrossCmd("Right", "BlueSpkrRight"); }
            }
        }*/

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
                 new PIDConstants(1, 0.55, 0.7555), // Translation PID constants
                 new PIDConstants(1.0, 0.0, 0.0) // Rotation PID constants
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
            // Were in Autonomous Mode and Alliance is Red, so invert field
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
        if (red()) {
            
            Robot.print("1. We are red");
            if (Left())         { 
                startPose = FieldConstants.RED_CAGE_RED;
                gyroHeading = FieldConstants.RED_CAGE_RED_GYRO; 
                Robot.print("2. We are Red Cage Red"); }
            
            if (Center())          { 
                startPose = FieldConstants.CENTER_PILLAR_RED;  
                gyroHeading = FieldConstants.CENTER_PILLAR_RED_GYRO; 
                Robot.print("2. We are Center Pillar Red"); }
            
            if (Right())        {
                startPose = FieldConstants.BLUE_CAGE_RED; 
                gyroHeading = FieldConstants.BLUE_CAGE_RED_GYRO;
                 Robot.print("2. We are Blue Cage Red"); }

        } else {
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
        }
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
    
    private static boolean RightToScore() {
        if (actionSelect.equals(AutoConfig.kActionRightToScore)) { return true; }
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


}
