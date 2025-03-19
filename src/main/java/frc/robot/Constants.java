/*
 * MIT License
 *
 * Copyright (c) PhotonVision
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package frc.robot;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;

//TODO: These have just been pulled straight from the photon example
//   we'll need to pull them from the actual robot code


public class Constants {
    public static class Vision {
        public static final String kCameraName = "YOUR CAMERA NAME";
        // Cam mounted facing forward, half a meter forward of center, half a meter up from center.
        public static final Transform3d kRobotToCam =
                new Transform3d(new Translation3d(0.5, 0.0, 0.5), new Rotation3d(0, 0, 0));

        // The layout of the AprilTags on the field
        public static final AprilTagFieldLayout kTagLayout =
                AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

        // The standard deviations of our vision estimated poses, which affect correction rate
        // (Fake values. Experiment and determine estimation noise on an actual robot.)
        public static final Matrix<N3, N1> kSingleTagStdDevs = VecBuilder.fill(4, 4, 8);
        public static final Matrix<N3, N1> kMultiTagStdDevs = VecBuilder.fill(0.5, 0.5, 1);
    }

    public static class Motors {
        // ----- Swerve drive -----
        public static final int FLdriveMotorID      =  1;  // Can ID Kraken
        public static final int FRdriveMotorID      =  3;  // Can ID Kraken
        public static final int BLdriveMotorID      =  7;  // Can ID Kraken
        public static final int BRdriveMotorID      =  5;  // Can ID Kraken

        public static final int FLangleMotorID      =  2;  // Can ID Kraken
        public static final int FRangleMotorID      =  4;  // Can ID Kraken
        public static final int BLangleMotorID      =  8;  // Can ID Kraken
        public static final int BRangleMotorID      =  6;  // Can ID Kraken

        public static final int ClimberMotorID      = 20;  // Can ID Kraken

        public static final int IntakeBottomMotorID = 21; // 
        public static final int IntakeTopMotorID    = 22;

        public static final int ElevatorMotorID     = 15;
    }

    public static class Gamepads {
        public static final int pilotGamepadPort      = 0;  // USB PORT
        public static final int operatorGamepadPort   = 1;  // USB PORT
    }

    public static class Gyros {
        public static final int Pigeon2ID          = 13;  // Can ID Pigeon 2
    }

    public static class Encoders {
        // Swerve Angles
        public static final int FLcanCoderID       =  9;  // Cancoder CAN ID
        public static final int FRcanCoderID       = 10;  // Cancoder CAN ID
        public static final int BLcanCoderID       = 12;  // Cancoder CAN ID
        public static final int BRcanCoderID       = 11;  // Cancoder CAN ID
    }

    public static class AnalogPorts {
        public static final int intakeGamepieceSensor = 0;
        //max 4 ports
    }

    public static class PWMPorts {
        public static final int winchLockID = 9;
    }

    public static class Swerve {
        // Physical properties
        public static final double kTrackWidth = Units.inchesToMeters(18.5);
        public static final double kTrackLength = Units.inchesToMeters(18.5);
        public static final double kRobotWidth = Units.inchesToMeters(25 + 3.25 * 2);
        public static final double kRobotLength = Units.inchesToMeters(25 + 3.25 * 2);
        public static final double kMaxLinearSpeed = Units.feetToMeters(15.5);
        public static final double kMaxAngularSpeed = Units.rotationsToRadians(2);
        public static final double kWheelDiameter = Units.inchesToMeters(4);
        public static final double kWheelCircumference = kWheelDiameter * Math.PI;

        public static final double kDriveGearRatio = 6.75; // 6.75:1 SDS MK4 L2 ratio
        public static final double kSteerGearRatio = 12.8; // 12.8:1

        public static final double kDriveDistPerPulse = kWheelCircumference / 1024 / kDriveGearRatio;
        public static final double kSteerRadPerPulse = 2 * Math.PI / 1024;

//        public enum ModuleConstants {
//            FL( // Front left
//                    1, 0, 0, 1, 1, 2, 3, 0, kTrackLength / 2, kTrackWidth / 2),
//            FR( // Front Right
//                    2, 2, 4, 5, 3, 6, 7, 0, kTrackLength / 2, -kTrackWidth / 2),
//            BL( // Back Left
//                    3, 4, 8, 9, 5, 10, 11, 0, -kTrackLength / 2, kTrackWidth / 2),
//            BR( // Back Right
//                    4, 6, 12, 13, 7, 14, 15, 0, -kTrackLength / 2, -kTrackWidth / 2);
//
//            public final int moduleNum;
//            public final int driveMotorID;
//            public final int driveEncoderA;
//            public final int driveEncoderB;
//            public final int steerMotorID;
//            public final int steerEncoderA;
//            public final int steerEncoderB;
//            public final double angleOffset;
//            public final Translation2d centerOffset;
//
//            private ModuleConstants(
//                    int moduleNum,
//                    int driveMotorID,
//                    int driveEncoderA,
//                    int driveEncoderB,
//                    int steerMotorID,
//                    int steerEncoderA,
//                    int steerEncoderB,
//                    double angleOffset,
//                    double xOffset,
//                    double yOffset) {
//                this.moduleNum = moduleNum;
//                this.driveMotorID = driveMotorID;
//                this.driveEncoderA = driveEncoderA;
//                this.driveEncoderB = driveEncoderB;
//                this.steerMotorID = steerMotorID;
//                this.steerEncoderA = steerEncoderA;
//                this.steerEncoderB = steerEncoderB;
//                this.angleOffset = angleOffset;
//                centerOffset = new Translation2d(xOffset, yOffset);
//            }
//        }
//
//        // Feedforward
//        // Linear drive feed forward
//        public static final SimpleMotorFeedforward kDriveFF =
//                new SimpleMotorFeedforward( // real
//                        0.25, // Voltage to break static friction
//                        2.5, // Volts per meter per second
//                        0.3 // Volts per meter per second squared
//                        );
//        // Steer feed forward
//        public static final SimpleMotorFeedforward kSteerFF =
//                new SimpleMotorFeedforward( // real
//                        0.5, // Voltage to break static friction
//                        0.25, // Volts per radian per second
//                        0.01 // Volts per radian per second squared
//                        );
//
//        // PID
//        public static final double kDriveKP = 1;
//        public static final double kDriveKI = 0;
//        public static final double kDriveKD = 0;
//
//        public static final double kSteerKP = 20;
//        public static final double kSteerKI = 0;
//        public static final double kSteerKD = 0.25;
    }
}
