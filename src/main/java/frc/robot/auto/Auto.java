package frc.robot.auto;

import java.lang.reflect.Field;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Robot;
import frc.robot.Robot.TeamAlliance;
import frc.robot.auto.AutoCmds;
import frc.util.FieldConstants;

public class Auto {
    public static final SendableChooser<String> actionChooser = new SendableChooser<>();
    public static final SendableChooser<String> positionChooser = new SendableChooser<>();

    public static String actionSelect;
    public static String positionSelect;
    private static Pose2d startPose;
    
    // ----- Autonomous Subsystem Constructor -----
    public Auto() {
        configureAutoBuilder();
        registerNamedCommands();
        setupSelectors();                // Setup on screen slection menus
    }

    public static void setupSelectors() {
        // Selector for Robot Starting Position on field
        positionChooser.setDefaultOption("Left",        AutoConfig.kLeft);
        positionChooser.addOption(       "Center",      AutoConfig.kCenter);
        positionChooser.addOption(       "Right",       AutoConfig.kRight);
        SmartDashboard.putData(positionChooser);
        // Selector for Autonomous Desired Action
        actionChooser.setDefaultOption(  "Do Nothing",          AutoConfig.kActionDoNothing);
        actionChooser.addOption(         "Crossline Only",      AutoConfig.kCrossOnlySelect);
        actionChooser.addOption(         "Intake To CL",            AutoConfig.kActionIntakeToCL);
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

        // ------------------------------- Do Nothing ---------------------------
        if (doNothing()) {
            System.out.println("********* DO Nothing Selection *********");
            return AutoCmds.DoNothingCmd();
        }
        if (crossOnly()) { 
            return AutoCmds.initAndFollowPath("Crossline"); //AutoCmds.CrossLineOnlyCmd("Crossline");
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
                 new PIDConstants(1.0, 0.0, 0.0), // Translation PID constants
                 new PIDConstants(1.0, 0.0, 0.0) // Rotation PID constants
            ),    // HolonomicPathFollowerConfig -> config for configuring path commands
            config,
            // ()->getAllianceFlip(),                // BooleanSupplier -------------> Should mirror/flip path
            () -> false,                        // BooleanSupplier -------------> Should mirror/flip path
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
                startPose = FieldConstants.RED_SPEAKER_LEFT;
                gyroHeading = FieldConstants.RED_SPEAKER_LEFT_GYRO; 
                Robot.print("2. We are Speaker Left"); }
            
            if (Center())          { 
                startPose = FieldConstants.RED_SPEAKER_CTR;  
                gyroHeading = FieldConstants.RED_SPEAKER_CTR_GYRO; 
                Robot.print("2. We are Speaker Center"); }
            
            if (Right())        { 
                startPose = FieldConstants.RED_SPEAKER_RIGHT; 
                gyroHeading = FieldConstants.RED_SPEAKER_RIGHT_GYRO;
                 Robot.print("2. We are Speaker Right"); }

        } else {
            Robot.print("1. We are Blue");
            if (Left())         {
                 startPose = FieldConstants.BLUE_CAGE_BLUE;  
                 gyroHeading = FieldConstants.BLUE_CAGE_BLUE_GYRO;
                 Robot.print("2. We are Speaker Left"); }
            
             if (Center())          { 
                startPose = FieldConstants.BLUE_SPEAKER_CTR;   
                gyroHeading = FieldConstants.BLUE_SPEAKER_CTR_GYRO;
                Robot.print("2. We are Speaker Center"); }
            
            if (Right())        { 
                startPose = FieldConstants.BLUE_SPEAKER_RIGHT; 
                gyroHeading = FieldConstants.BLUE_SPEAKER_RIGHT_GYRO;
                Robot.print("2. We are Speaker Right"); }
        }
        // Robot.swerve.resetOdometryAndGyroFromPose(startPose);
        Robot.swerve.resetOdometryAndGyroFromPose(startPose, gyroHeading);
    }


    // ------------------------------------------------------------------------
    //            Simple Checks to make above routines cleaner
    // ------------------------------------------------------------------------
    private static boolean doNothing() {
        //if (actionSelect.equals(AutoConfig.kActionDoNothing)) { return true; }
        return false;
    }
    private static boolean intakeToCL() {
        //if (actionSelect.equals(AutoConfig.kActionIntakeToCL)) { return true; }
        return false;
    }
   
    private static boolean crossOnly() {
        //if (actionSelect.equals(AutoConfig.kCrossOnlySelect)) { return true; }
        return true;
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
