package frc.robot;

import java.util.Map;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.lib.telemetry.Alert;
import frc.lib.telemetry.Alert.AlertType;
import frc.lib.telemetry.TelemetrySubsystem;
import frc.lib.util.Util;
import frc.robot.XBoxCtrlrs.pilot.PilotGamepadTelemetry;
import frc.robot.auto.Auto;
import frc.robot.drivetrain.DrivetrainTelemetry;
import frc.robot.subsystems.elevator.ElevatorTelemetry;

public class RobotTelemetry extends TelemetrySubsystem {
    private static boolean disablePrints = false;

    // Telemetry for Basic Robot Functionality    
    public static DrivetrainTelemetry       m_SwerveTelemetry;
    // public static RotarySwitchTelemetry    m_RotarySwitchTelemetry;
    // public static VisionTelemetry           m_VisionTelemetry;
    public static PilotGamepadTelemetry     m_PilotTelemetry;
    public static ElevatorTelemetry         m_ElevatorTelemtry;
    // public static OperatorGamepadTelemetry m_OperatorTelemetry;
    
    // Telemetry for Gamepiece Manipulation and Climbing

    // Alerts
    private static Alert batteryAlert = new Alert("Low Battery < 12v", AlertType.WARNING);
    private static Alert FMSConnectedAlert = new Alert("FMS Connected", AlertType.INFO);

    // testing sensors
    // private DigitalInput climberLowerSw = new DigitalInput(RobotConfig.LimitSwitches.climberLowerLimitSw);
    // private DigitalInput climberMidSw = new DigitalInput(RobotConfig.LimitSwitches.climberMidLimitSw);
    // private DigitalInput elevatorSw = new DigitalInput(RobotConfig.LimitSwitches.elevatorLowerLimitSw);
    // private AnalogInput  intakeProx = new AnalogInput(RobotConfig.AnalogPorts.intakeGamepieceSensor);

    private String IPaddress = "UNKNOWN";

    // ------------- Constructor --------------
    public RobotTelemetry() {
        // Call Super constuctor first - 
        super("Robot");     // Main tab will be called "Robot"

        // Robot-General Tab Setup
        layoutRobotTelemetryTab();   // Fill This Tab with data

        // Subsystem-Specific Tabs Setup
        m_SwerveTelemetry =         new DrivetrainTelemetry(Robot.swerve);
        m_PilotTelemetry =          new PilotGamepadTelemetry(Robot.pilotGamepad);
        m_ElevatorTelemtry =        new ElevatorTelemetry(Robot.elevator);
        // m_OperatorTelemetry =       new OperatorGamepadTelemetry(Robot.operatorGamepad);
        // m_VisionTelemetry =         new VisionTelemetry(Robot.vision);

        // Misc
        // m_RotarySwitchTelemetry =   new RotarySwitchTelemetry(Robot.rotarySwitch);
    }

    @Override
    public void periodic() {
        checkFMSalert();
        checkBatteryWhenDisabledalert();
    }

    public void layoutRobotTelemetryTab(){
        /* 
        Auto.setupSelectors();


        // Teleop Speed
        tab.add("Speed Selection",    Robot.pilotGamepad.speedChooser)  .withPosition(0, 0).withSize(3, 2);*/

        // Auto Info
        tab.add("Action Selection",     Auto.actionChooser)             .withPosition(0, 2).withSize(3, 2);
        tab.add("Position Selection",   Auto.positionChooser)           .withPosition(0, 4).withSize(3, 2);

        /*

    
        // Match Time
        tab.addNumber("Match Time", () -> Timer.getMatchTime())         .withPosition(3, 0)
                                                                        .withSize(3, 3)
                                                                        .withWidget("Simple Dial")
                                                                .withProperties(Map.of("Min", 0, "Max", 135));
        */
        // Swerve Stuff
                // CAN ABS Value
        tab.addNumber("FL º", () -> Robot.swerve.swerveMods[0].getSteerAngle()).withPosition(6, 0).withSize(1, 1);
        tab.addNumber("FL v", () -> Robot.swerve.swerveMods[0].getModuleVelocityMPS()).withPosition(7, 0).withSize(1, 1);

        tab.addNumber("FR º", () -> Robot.swerve.swerveMods[1].getSteerAngle()).withPosition(6, 1).withSize(1, 1);
        tab.addNumber("FR v", () -> Robot.swerve.swerveMods[1].getModuleVelocityMPS()).withPosition(7, 1).withSize(1, 1);

        tab.addNumber("BL º", () -> Robot.swerve.swerveMods[2].getSteerAngle()).withPosition(8, 0).withSize(1, 1);
        tab.addNumber("BL v", () -> Robot.swerve.swerveMods[3].getModuleVelocityMPS()).withPosition(9, 0).withSize(1, 1);

        tab.addNumber("BR º", () -> Robot.swerve.swerveMods[3].getSteerAngle()).withPosition(8, 1).withSize(1, 1);
        tab.addNumber("BR v", () -> Robot.swerve.swerveMods[3].getModuleVelocityMPS()).withPosition(9, 1).withSize(1, 1);

       // tab.addDouble("Distance", () -> Robot.getDistanceMM()/25.4).withWidget("Simple Dial").withProperties(Map.of("min", 2, "max", 20));
        // tab.addBoolean("Climber Lower", () -> !climberLowerSw.get()).withPosition(7, 0);
        // tab.addBoolean("Climber Mid", () -> !climberMidSw.get()).withPosition(7, 2);
        // tab.addBoolean("Elevator Bottom", () -> !elevatorSw.get()).withPosition(7, 4);
        // tab.addNumber("Intake Prox", () -> intakeProx.getAverageVoltage()).withPosition(7, 6).withSize(2, 1);

        // // Robot Pose
        // tab.addString("Robot Pose", () -> "X: " + Rmath.mRound(Robot.swerve.getPose().getX(), 2) + 
        //                                   ", Y: " + Rmath.mRound(Robot.swerve.getPose().getY(), 2))
        //                                                             .withPosition(3, 5)
        //                                                             .withSize(3, 2);

    }

    public void checkFMSalert() {
        FMSConnectedAlert.set(DriverStation.isFMSAttached());
    }

    public void checkBatteryWhenDisabledalert() {
        batteryAlert.set(DriverStation.isDisabled() && Util.checkBattery(12.0));
    }

    public boolean flash() {
        return (int) Timer.getFPGATimestamp() % 2 == 0;
    }

    //public static void print(String output) {
    //    if (!disablePrints) {
    //        System.out.println(output);
    //    }
    //}
}
