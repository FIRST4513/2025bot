package frc.lib.telemetry;

import java.util.Map;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SuppliedValueWidget;
import frc.lib.drivers.SpectrumSolenoid;

public class WidgetsAndLayouts {

    //Standard TalonFX Layout
    public static ShuffleboardLayout TalonFXLayout (String name, ShuffleboardTab tab, com.ctre.phoenix6.hardware.TalonFX motor){
        ShuffleboardLayout layout = tab.getLayout(name, BuiltInLayouts.kGrid);
        layout.withProperties(Map.of("Label position", "TOP"));

        SuppliedValueWidget<Double> outputPercentWidget = layout.addNumber("Output Percent", () -> motor.getMotorOutputStatus().getValueAsDouble());
         outputPercentWidget.withPosition(0, 0);
 
         SuppliedValueWidget<Double> supplyCurrentWidget = layout.addNumber("Supply Current", () -> motor.getSupplyCurrent().getValueAsDouble());
         supplyCurrentWidget.withPosition(0, 1);

         SuppliedValueWidget<Double>  velocityWidget = layout.addNumber("Velocity", ()-> motor.getRotorVelocity().getValueAsDouble());
         velocityWidget.withPosition(0, 3);

         SuppliedValueWidget<Double>  positionWidget = layout.addNumber("Position", ()-> motor.getRotorPosition().getValueAsDouble());
         positionWidget.withPosition(0, 4);

         return layout;
    }

    //public static ShuffleboardLayout TalonFXLayout (String name, ShuffleboardTab tab, WPI_TalonSRX motor){
    //    return TalonFXLayout(name, tab, (TalonFX) motor);
    //}

    //Solenoid Widget
    public static SuppliedValueWidget<Boolean> SolenoidWidget (String name, ShuffleboardTab tab, SpectrumSolenoid sol){
        return tab.addBoolean(name, ()-> sol.get());
    }

    public static SuppliedValueWidget<Boolean> SolenoidWidget (String name, ShuffleboardLayout layout, SpectrumSolenoid sol){
        return layout.addBoolean(name, ()-> sol.get());
    }
    
}
