package frc.robot.XBoxCtrlrs.pilot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.Robot;
import frc.robot.RobotConfig;
import frc.robot.XBoxCtrlrs.pilot.PilotGamepadConfig.MaxSpeeds;
import frc.robot.XBoxCtrlrs.pilot.commands.PilotGamepadCmds;
import frc.robot.drivetrain.commands.DrivetrainCmds;
import frc.robot.subsystems.climber.ClimberSubSys.ClimberState;
import frc.robot.subsystems.climber.commands.ClimberCmds;
import frc.robot.subsystems.vision.Vision;

//figure out later
//import frc.util.FieldConstants;
//import frc.robot.Robot.TeamAlliance;


/** Used to add buttons to the pilot gamepad and configure the joysticks */
public class PilotGamepad extends Gamepad {
    //public final PilotGamepadTelemetry telemetry;

    public static ExpCurve climberThrottleCurve = new ExpCurve(
        PilotGamepadConfig.climberSpeedExp,
        PilotGamepadConfig.climberSpeedOffset,
        PilotGamepadConfig.climberSpeedScaler,
        PilotGamepadConfig.climberSpeedDeadband);
    public static ExpCurve forwardSpeedCurve =
            new ExpCurve(

                    PilotGamepadConfig.forwardSpeedExp,

                    PilotGamepadConfig.forwardSpeedOffset,

                    PilotGamepadConfig.forwardSpeedScaler,

                    PilotGamepadConfig.forwardSpeedDeadband);
    public static ExpCurve sidewaysSpeedCurve =
            new ExpCurve(
                    PilotGamepadConfig.sidewaysSpeedExp,
                    PilotGamepadConfig.sidewaysSpeedOffset,
                    PilotGamepadConfig.sidewaysSpeedScaler,
                    PilotGamepadConfig.sidewaysSpeedDeadband);
    public static ExpCurve rotationCurve =
            new ExpCurve(
                    PilotGamepadConfig.rotationSpeedExp,
                    PilotGamepadConfig.rotationSpeedOffset,
                    PilotGamepadConfig.rotationSpeedScaler,
                    PilotGamepadConfig.rotationSpeedDeadband);
    public SendableChooser<String> speedChooser = new SendableChooser<String>();

    // ----- Constructor -----
    public PilotGamepad() {
        super("Pilot", RobotConfig.Gamepads.pilotGamepadPort);
        setupSpeedMenu();
    }

    // ----- `Gamepad` Lib Required Methods for Button Assignment -----
    public void setupTeleopButtons() {
        
        /* ----- Competition Button Assignments ----- */
        // "Start" Button - Reset Gyro to 0
        gamepad.selectButton.onTrue(new InstantCommand(() -> Robot.swerve.zeroGyroHeading()));
        
        // "Select" Button - Reset Odometry to (0, 0) & 0º [FOR TESTING, DON'T USE IN COMP]
        // gamepad.selectButton.onTrue(new InstantCommand(() -> Robot.swerve.resetPose()));

        // "Select" Button - Reset Gyro to 180
        gamepad.startButton.onTrue(new InstantCommand(() -> Robot.swerve.setGyroHeading(180)));

        /*gamepad.Dpad.Up.whileTrue(ClimberCmds.climberSetExtend());
        gamepad.Dpad.Up.onFalse(ClimberCmds.climberSetState(ClimberState.STOPPED));
        
        gamepad.Dpad.Down.whileTrue(ClimberCmds.climberSetStow());
        gamepad.Dpad.Down.onFalse(ClimberCmds.climberSetState(ClimberState.STOPPED));
        
        gamepad.Dpad.Left.whileTrue(ClimberCmds.lockWinch());
        gamepad.Dpad.Right.whileTrue(ClimberCmds.unlockWinch());

        gamepad.Dpad.Left.onTrue(ClimberCmds.climberSetStartup());*/

        gamepad.Dpad.Down.whileTrue(ClimberCmds.climberSetStow());
        gamepad.Dpad.Down.onFalse(ClimberCmds.climberSetState(ClimberState.STOPPED));

        gamepad.Dpad.Up.whileTrue(ClimberCmds.climberSetExtend());
        gamepad.Dpad.Up.onFalse(ClimberCmds.climberSetState(ClimberState.STOPPED));

        gamepad.xButton.onTrue(ClimberCmds.unlockWinch());
        gamepad.bButton.onTrue(ClimberCmds.lockWinch());

        gamepad.Dpad.Left.onTrue(ClimberCmds.climberSetState(ClimberState.TWOROTATIONS));

        gamepad.Dpad.Right.onTrue(ClimberCmds.climberSetStartup());

        gamepad.aButton.whileTrue(new InstantCommand(()-> robotVision()));
        gamepad.aButton.onFalse(new InstantCommand(()-> robotNoVision()));



        /* ----- Example Ways to use Buttons in different ways ---- */

        // example combo button functionality:
        // gamepad.rightBumper.and(gamepad.aButton).whileTrue(new RunCommand(() -> Robot.print("Going to Toggling Angle")));

        // example go-while-held button functionality:
        
        // or:
        // gamepad.Dpad.Left.onTrue(IntakeCmds.intakeSetAmpCmd());
    }

    public void setupDisabledButtons() {}

    public void setupTestButtons() {}

    // ----- Custom Methods for Getting Gamepad Values and Inputs -----


    public void robotVision() {
        Robot.swerve.setDefaultCommand(PilotGamepadCmds.FpvWithVision());
    }
    
    public void robotNoVision() {
        Robot.swerve.setDefaultCommand(PilotGamepadCmds.FpvPilotSwerveCmd());
    }

    // forward/backward down the field
    public double getDriveFwdPositive() {
        return forwardSpeedCurve.calculateMappedVal(this.gamepad.leftStick.getY());
    }


    // side-to-side across the field
    public double getDriveLeftPositive() {
        return sidewaysSpeedCurve.calculateMappedVal(this.gamepad.leftStick.getX());
    }

    //Positive is counter-clockwise, left Trigger is positive
    public double getDriveRotationCCWPositive() {
		double value = this.gamepad.triggers.getTwist();
		value = rotationCurve.calculateMappedVal(value);
		return value;        
    }

    public double getClimberAdjustInput() {
        return gamepad.rightStick.getY() / 1.5;
    }

    // ----- Getters and Setters for Speed Selections -----

    public MaxSpeeds getSelectedSpeed(){
        String speed = speedChooser.getSelected();;
        if ( speed == "Fast")    return MaxSpeeds.FAST;
        if ( speed == "Medium") return MaxSpeeds.MEDIUM;
        return MaxSpeeds.SLOW;
    }

    public void setupSpeedMenu(){
            // Setup Speed Selector
            speedChooser.addOption        ("1. Slow",      "Slow");
            speedChooser.addOption        ("2. Medium", "Medium");
            speedChooser.setDefaultOption ("4. Fast", 	   "Fast");
            SmartDashboard.putData(speedChooser);
    }
    
    public void setMaxSpeeds(MaxSpeeds speed){
        switch (speed) { 
            case FAST:
                System.out.println("Driver Speeds set to FAST !!!");
                forwardSpeedCurve.setScalar(PilotGamepadConfig.FastfowardVelocity);
                sidewaysSpeedCurve.setScalar(PilotGamepadConfig.FastsidewaysVelocity);
                rotationCurve.setScalar(PilotGamepadConfig.FastrotationVelocity);
                // forwardSpeedCurve.setExpVal(PilotGamepadConfig.FastForwardExp);
                // sidewaysSpeedCurve.setExpVal(PilotGamepadConfig.FastSidewaysExp);
                // rotationCurve.setExpVal(PilotGamepadConfig.FastRotationExp);
                break;
            case MEDIUM:
                System.out.println("Driver Speeds set to MEDIUM !!!");
                forwardSpeedCurve.setScalar(PilotGamepadConfig.MediumForwardVelocity);
                sidewaysSpeedCurve.setScalar(PilotGamepadConfig.MediumSidewaysVelocity);
                rotationCurve.setScalar(PilotGamepadConfig.MediumSidewaysVelocity);
                // forwardSpeedCurve.setExpVal(PilotGamepadConfig.MedSlowForwardExp);
                // sidewaysSpeedCurve.setExpVal(PilotGamepadConfig.MedSlowSidewaysExp);
                // rotationCurve.setExpVal(PilotGamepadConfig.FastRotationExp);
                break;
            default:
                System.out.println("Driver Speeds set to SLOW !!!");
                forwardSpeedCurve.setScalar(PilotGamepadConfig.SlowforwardVelocity);
                sidewaysSpeedCurve.setScalar(PilotGamepadConfig.SlowsidewaysVelocity);
                rotationCurve.setScalar(PilotGamepadConfig.SlowsidewaysVelocity);
                // forwardSpeedCurve.setExpVal(PilotGamepadConfig.SlowForwardExp);
                // sidewaysSpeedCurve.setExpVal(PilotGamepadConfig.SlowSidewaysExp);
                // rotationCurve.setExpVal(PilotGamepadConfig.SlowRotationExp);
                break;
        }
    }

    // ----- Misc. Gamepad Methods -----

    

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }

    // use as example
    /*public void setupFieldPoses(){
        if (Robot.alliance == TeamAlliance.BLUE) {
            spkrLeftPose = FieldConstants.BLUE_SPEAKER_LEFT;
            spkrCtrPose = FieldConstants.BLUE_SPEAKER_CTR;
            spkrRightPose =FieldConstants.BLUE_SPEAKER_RIGHT;
            ampPose =FieldConstants.BLUE_AMP;
            HPLeft = FieldConstants.BLUE_HP_LEFT;
            HPCtr = FieldConstants.BLUE_HP_CTR;
            HPRight =FieldConstants.BLUE_HP_RIGHT;
        } else {
            spkrLeftPose = FieldConstants.RED_SPEAKER_LEFT;
            spkrCtrPose = FieldConstants.RED_SPEAKER_CTR;
            spkrRightPose =FieldConstants.RED_SPEAKER_RIGHT;
            ampPose =FieldConstants.RED_AMP;
            HPLeft = FieldConstants.RED_HP_LEFT;
            HPCtr = FieldConstants.RED_HP_CTR;
            HPRight =FieldConstants.RED_HP_RIGHT;
        } 
    }*/
    

}
