package frc.robot;

public class RobotConfig {
    
    public final class Motors {
        // ----- Swerve drive -----
        public final static int FLdriveMotorID     =  1;  // Can ID Kraken
        public final static int FRdriveMotorID     =  2;  // Can ID Kraken
        public final static int BLdriveMotorID     =  3;  // Can ID Kraken
        public final static int BRdriveMotorID     =  4;  // Can ID Kraken

        public final static int FLangleMotorID     =  5;  // Can ID Kraken
        public final static int FRangleMotorID     =  6;  // Can ID Kraken
        public final static int BLangleMotorID     =  7;  // Can ID Kraken
        public final static int BRangleMotorID     =  8;  // Can ID Kraken
    }
    public final class Gamepads {
        public final static int pilotGamepadPort      = 0;  // USB PORT
        public final static int operatorGamepadPort   = 1;  // USB PORT
    }
    public final class Gyros {
        public final static int Pigeon2ID          = 13;  // Can ID Pigeon 2
    }

    public final class Encoders {
        // Swerve Angles
        public final static int FLcanCoderID       =  9;  // Cancoder CAN ID
        public final static int FRcanCoderID       = 10;  // Cancoder CAN ID
        public final static int BLcanCoderID       = 11;  // Cancoder CAN ID
        public final static int BRcanCoderID       = 12;  // Cancoder CAN ID
    }

}
