package frc.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class FieldConstants {
    /* Blue Field Positions */
    public static final Pose2d BLUE_CAGE_BLUE  = new Pose2d(7.59, 6.2, Rotation2d.fromDegrees(180));
    public static final Pose2d CENTER_PILLAR_BLUE   = new Pose2d(7.580, 4, Rotation2d.fromDegrees(180));
    public static final Pose2d RED_CAGE_BLUE = new Pose2d(7.59, 1.900, Rotation2d.fromDegrees(180));
    
    public static final double BLUE_CAGE_BLUE_GYRO  = 180;
    public static final double CENTER_PILLAR_BLUE_GYRO   = 180;
    public static final double RED_CAGE_BLUE_GYRO = 0;

    public static final Pose2d BLUE_AMP = new Pose2d(1.81, 7.63, Rotation2d.fromDegrees(90));

    public static final Pose2d BLUE_HP_LEFT  = new Pose2d(15.87, 1.25, Rotation2d.fromDegrees(-60));
    public static final Pose2d BLUE_HP_CTR   = new Pose2d(15.44, 1.0, Rotation2d.fromDegrees(-60));
    public static final Pose2d BLUE_HP_RIGHT = new Pose2d(14.94, 0.69, Rotation2d.fromDegrees(-60));
    
    /* Red Fiel Positions */
    public static final Pose2d BLUE_CAGE_RED  = new Pose2d(10.0, 6.2, Rotation2d.fromDegrees(0));
    public static final Pose2d CENTER_PILLAR_RED   = new Pose2d(10.0, 4, Rotation2d.fromDegrees(0));
    public static final Pose2d RED_CAGE_RED = new Pose2d(10.0, 1.9, Rotation2d.fromDegrees(0));

    public static final double BLUE_CAGE_RED_GYRO  = 180;
    public static final double CENTER_PILLAR_RED_GYRO   = 180;
    public static final double RED_CAGE_RED_GYRO = 180;

    public static final Pose2d RED_AMP = new Pose2d(14.71, 7.63, Rotation2d.fromDegrees(90));

    public static final Pose2d RED_HP_LEFT  = new Pose2d(1.67, 0.69, Rotation2d.fromDegrees(-120));
    public static final Pose2d RED_HP_CTR   = new Pose2d(1.17, 0.95, Rotation2d.fromDegrees(-120));
    public static final Pose2d RED_HP_RIGHT = new Pose2d(0.69, 1.23, Rotation2d.fromDegrees(-120));
}
