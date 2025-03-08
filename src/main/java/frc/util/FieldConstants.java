package frc.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class FieldConstants {
    /* Blue Field Positions */
    public static final Pose2d BLUE_CAGE_BLUE  = new Pose2d(7.59, 6.2, Rotation2d.fromDegrees(0));
    public static final Pose2d CENTER_PILLAR_BLUE   = new Pose2d(7.580, 4, Rotation2d.fromDegrees(0));
    public static final Pose2d RED_CAGE_BLUE = new Pose2d(7.59, 1.900, Rotation2d.fromDegrees(0));
    
    public static final double BLUE_CAGE_BLUE_GYRO  = 0;
    public static final double CENTER_PILLAR_BLUE_GYRO   = 0;
    public static final double RED_CAGE_BLUE_GYRO = 0;

    /* Red Field Positions */
    public static final Pose2d BLUE_CAGE_RED  = new Pose2d(10.0, 6.2, Rotation2d.fromDegrees(0));
    public static final Pose2d CENTER_PILLAR_RED   = new Pose2d(10.0, 4, Rotation2d.fromDegrees(0));
    public static final Pose2d RED_CAGE_RED = new Pose2d(10.0, 1.9, Rotation2d.fromDegrees(0));

    public static final double BLUE_CAGE_RED_GYRO  = 0;
    public static final double CENTER_PILLAR_RED_GYRO = 0;
    public static final double RED_CAGE_RED_GYRO = 0;

   }
