package frc.robot.XBoxCtrlrs.operator;

import frc.lib.gamepads.Gamepad;
import frc.lib.gamepads.mapping.ExpCurve;
import frc.robot.Robot;
import frc.robot.RobotConfig;
import frc.robot.XBoxCtrlrs.operator.commands.OperatorGamepadCmds;
import frc.robot.subsystems.elevator.commands.ElevatorCmds;
import frc.robot.subsystems.intake.IntakeConfig;
import frc.robot.subsystems.intake.IntakeSubSys;
public class OperatorGamepad extends Gamepad {
    public static ExpCurve intakeThrottleCurve = new ExpCurve(
        OperatorGamepadConfig.intakeSpeedExp,
        OperatorGamepadConfig.intakeSpeedOffset,
        OperatorGamepadConfig.intakeSpeedScaler,
        OperatorGamepadConfig.intakeSpeedDeadband);

    public static ExpCurve elevThrottleCurve = new ExpCurve(
        OperatorGamepadConfig.elevSpeedExp,
        OperatorGamepadConfig.elevSpeedOffset,
        OperatorGamepadConfig.elevSpeedScaler,
        OperatorGamepadConfig.elevSpeedDeadband);

    public static ExpCurve armThrottleCurve = new ExpCurve(
        OperatorGamepadConfig.armSpeedExp,
        OperatorGamepadConfig.armSpeedOffset,
        OperatorGamepadConfig.armSpeedScaler,
        OperatorGamepadConfig.armSpeedDeadband);

    /* Contstructor */
    public OperatorGamepad() {
        super("Operator", RobotConfig.Gamepads.operatorGamepadPort);
    }
    
    // ----- Gamepad specific methods for button assignments -----
    public void setupTeleopButtons() {
        /* ----- Overrides ----- */
        gamepad.leftBumper.onTrue(OperatorGamepadCmds.stopAllCmd());
        gamepad.rightBumper.onTrue(OperatorGamepadCmds.manualAllCmd()).onFalse(OperatorGamepadCmds.stopAllCmd());

        gamepad.aButton.onTrue(ElevatorCmds.elevatorSetLevelOne());

        gamepad.bButton.onTrue(ElevatorCmds.elevatorSetLevelTwo());

        gamepad.xButton.onTrue(ElevatorCmds.elevatorSetLevelThree());

        gamepad.yButton.onTrue(ElevatorCmds.elevatorSetLevelFour());

        gamepad.Dpad.Up.whileTrue(ElevatorCmds.elevatorSetManual());
        gamepad.Dpad.Up.onFalse(ElevatorCmds.elevatorSetStopped());

        gamepad.Dpad.Down.onTrue(ElevatorCmds.elevatorSetBottom());
        
    }

    @Override
    public void setupTestButtons() {}

    public void setupDisabledButtons() {}

    // ---- value getters -----
    public double getTriggerTwist() {
        return intakeThrottleCurve.calculateMappedVal(gamepad.triggers.getTwist());
    }

    public double getPivotAdjust() {
        return gamepad.rightStick.getY();
    }

    // ---- rumble method -----

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }

    /*   "the sparkle" -madi   */
}
