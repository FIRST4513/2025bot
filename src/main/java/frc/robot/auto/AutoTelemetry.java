package frc.robot.auto;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.networktables.GenericEntry;
import java.util.Map;

public class AutoTelemetry {
    private ShuffleboardTab tab;
    private SimpleWidget translationPWidget;
    private SimpleWidget translationIWidget;
    private SimpleWidget translationDWidget;
    private SimpleWidget rotationPWidget;
    private SimpleWidget rotationIWidget;
    private SimpleWidget rotationDWidget;

    public AutoTelemetry() {
        tab = Shuffleboard.getTab("Auto PID");
        
        // Create widgets for translation PID constants
        translationPWidget = tab.add("Translation P", 0.1)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0.0, "max", 5.0, "block increment", 0.1))
            .withPosition(0, 0);
            
        translationIWidget = tab.add("Translation I", 0.0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0.0, "max", 5.0, "block increment", 0.1))
            .withPosition(0, 1);
            
        translationDWidget = tab.add("Translation D", 0.0)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0.0, "max", 5.0, "block increment", 0.1))
            .withPosition(0, 2);

        // Create widgets for rotation PID constants
        rotationPWidget = tab.add("Rotation P", 1.5)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0.0, "max", 5.0, "block increment", 0.1))
            .withPosition(1, 0);
            
        rotationIWidget = tab.add("Rotation I", 0.5)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0.0, "max", 5.0, "block increment", 0.1))
            .withPosition(1, 1);
            
        rotationDWidget = tab.add("Rotation D", 0.5)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", 0.0, "max", 5.0, "block increment", 0.1))
            .withPosition(1, 2);
    }

    public void update() {
        // First, check if Shuffleboard values have changed and update PID controllers if needed
        double translationP = translationPWidget.getEntry().getDouble(0.1);
        double translationI = translationIWidget.getEntry().getDouble(0.0);
        double translationD = translationDWidget.getEntry().getDouble(0.0);
        double rotationP = rotationPWidget.getEntry().getDouble(1.5);
        double rotationI = rotationIWidget.getEntry().getDouble(0.5);
        double rotationD = rotationDWidget.getEntry().getDouble(0.5);

        // Update the PID constants in Auto.java if they've changed in Shuffleboard
        if (translationP != Auto.translationPID.getP() ||
            translationI != Auto.translationPID.getI() ||
            translationD != Auto.translationPID.getD() ||
            rotationP != Auto.rotationPID.getP() ||
            rotationI != Auto.rotationPID.getI() ||
            rotationD != Auto.rotationPID.getD()) {
            
            Auto.translationPID.setPID(translationP, translationI, translationD);
            Auto.rotationPID.setPID(rotationP, rotationI, rotationD);
        }

        // Then, check if PID values have changed outside of Shuffleboard and update widgets if needed
        if (translationP != Auto.translationPID.getP()) {
            translationPWidget.getEntry().setDouble(Auto.translationPID.getP());
        }
        if (translationI != Auto.translationPID.getI()) {
            translationIWidget.getEntry().setDouble(Auto.translationPID.getI());
        }
        if (translationD != Auto.translationPID.getD()) {
            translationDWidget.getEntry().setDouble(Auto.translationPID.getD());
        }
        if (rotationP != Auto.rotationPID.getP()) {
            rotationPWidget.getEntry().setDouble(Auto.rotationPID.getP());
        }
        if (rotationI != Auto.rotationPID.getI()) {
            rotationIWidget.getEntry().setDouble(Auto.rotationPID.getI());
        }
        if (rotationD != Auto.rotationPID.getD()) {
            rotationDWidget.getEntry().setDouble(Auto.rotationPID.getD());
        }
    }
} 