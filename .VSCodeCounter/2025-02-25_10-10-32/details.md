# Details

Date : 2025-02-25 10:10:32

Directory c:\\Workspace\\Robot\\2025bot\\src\\main\\java\\frc\\robot

Total : 45 files,  2819 codes, 1137 comments, 928 blanks, all 4884 lines

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [src/main/java/frc/robot/Main.java](/src/main/java/frc/robot/Main.java) | Java | 8 | 13 | 5 | 26 |
| [src/main/java/frc/robot/Robot.java](/src/main/java/frc/robot/Robot.java) | Java | 181 | 60 | 58 | 299 |
| [src/main/java/frc/robot/RobotConfig.java](/src/main/java/frc/robot/RobotConfig.java) | Java | 36 | 3 | 15 | 54 |
| [src/main/java/frc/robot/RobotTelemetry.java](/src/main/java/frc/robot/RobotTelemetry.java) | Java | 55 | 50 | 26 | 131 |
| [src/main/java/frc/robot/XBoxCtrlrs/Layouts.md](/src/main/java/frc/robot/XBoxCtrlrs/Layouts.md) | Markdown | 53 | 0 | 11 | 64 |
| [src/main/java/frc/robot/XBoxCtrlrs/operator/OperatorGamepad.java](/src/main/java/frc/robot/XBoxCtrlrs/operator/OperatorGamepad.java) | Java | 119 | 70 | 55 | 244 |
| [src/main/java/frc/robot/XBoxCtrlrs/operator/OperatorGamepadConfig.java](/src/main/java/frc/robot/XBoxCtrlrs/operator/OperatorGamepadConfig.java) | Java | 40 | 9 | 10 | 59 |
| [src/main/java/frc/robot/XBoxCtrlrs/operator/OperatorGamepadTelemetry.java](/src/main/java/frc/robot/XBoxCtrlrs/operator/OperatorGamepadTelemetry.java) | Java | 84 | 6 | 21 | 111 |
| [src/main/java/frc/robot/XBoxCtrlrs/operator/commands/OperatorGamepadCmds.java](/src/main/java/frc/robot/XBoxCtrlrs/operator/commands/OperatorGamepadCmds.java) | Java | 49 | 12 | 21 | 82 |
| [src/main/java/frc/robot/XBoxCtrlrs/pilot/PilotGamepad.java](/src/main/java/frc/robot/XBoxCtrlrs/pilot/PilotGamepad.java) | Java | 113 | 66 | 49 | 228 |
| [src/main/java/frc/robot/XBoxCtrlrs/pilot/PilotGamepadConfig.java](/src/main/java/frc/robot/XBoxCtrlrs/pilot/PilotGamepadConfig.java) | Java | 40 | 9 | 10 | 59 |
| [src/main/java/frc/robot/XBoxCtrlrs/pilot/PilotGamepadTelemetry.java](/src/main/java/frc/robot/XBoxCtrlrs/pilot/PilotGamepadTelemetry.java) | Java | 84 | 6 | 21 | 111 |
| [src/main/java/frc/robot/XBoxCtrlrs/pilot/commands/PilotGamepadCmds.java](/src/main/java/frc/robot/XBoxCtrlrs/pilot/commands/PilotGamepadCmds.java) | Java | 40 | 11 | 17 | 68 |
| [src/main/java/frc/robot/auto/Auto.java](/src/main/java/frc/robot/auto/Auto.java) | Java | 187 | 46 | 38 | 271 |
| [src/main/java/frc/robot/auto/AutoCmds.java](/src/main/java/frc/robot/auto/AutoCmds.java) | Java | 52 | 34 | 16 | 102 |
| [src/main/java/frc/robot/auto/AutoConfig.java](/src/main/java/frc/robot/auto/AutoConfig.java) | Java | 23 | 4 | 12 | 39 |
| [src/main/java/frc/robot/canbus/canfd.java](/src/main/java/frc/robot/canbus/canfd.java) | Java | 5 | 0 | 4 | 9 |
| [src/main/java/frc/robot/drivetrain/AlignmentController.java](/src/main/java/frc/robot/drivetrain/AlignmentController.java) | Java | 61 | 6 | 14 | 81 |
| [src/main/java/frc/robot/drivetrain/DrivetrainOdometry.java](/src/main/java/frc/robot/drivetrain/DrivetrainOdometry.java) | Java | 39 | 8 | 10 | 57 |
| [src/main/java/frc/robot/drivetrain/DrivetrainSubSys.java](/src/main/java/frc/robot/drivetrain/DrivetrainSubSys.java) | Java | 153 | 75 | 60 | 288 |
| [src/main/java/frc/robot/drivetrain/DrivetrainTelemetry.java](/src/main/java/frc/robot/drivetrain/DrivetrainTelemetry.java) | Java | 45 | 37 | 15 | 97 |
| [src/main/java/frc/robot/drivetrain/OdometryThread.java](/src/main/java/frc/robot/drivetrain/OdometryThread.java) | Java | 214 | 194 | 74 | 482 |
| [src/main/java/frc/robot/drivetrain/PigeonGyro.java](/src/main/java/frc/robot/drivetrain/PigeonGyro.java) | Java | 22 | 9 | 12 | 43 |
| [src/main/java/frc/robot/drivetrain/RotationController.java](/src/main/java/frc/robot/drivetrain/RotationController.java) | Java | 50 | 6 | 14 | 70 |
| [src/main/java/frc/robot/drivetrain/SwerveModule.java](/src/main/java/frc/robot/drivetrain/SwerveModule.java) | Java | 142 | 124 | 51 | 317 |
| [src/main/java/frc/robot/drivetrain/commands/DrivetrainCmds.java](/src/main/java/frc/robot/drivetrain/commands/DrivetrainCmds.java) | Java | 16 | 11 | 7 | 34 |
| [src/main/java/frc/robot/drivetrain/commands/SwerveDriveCmd.java](/src/main/java/frc/robot/drivetrain/commands/SwerveDriveCmd.java) | Java | 90 | 23 | 18 | 131 |
| [src/main/java/frc/robot/drivetrain/config/AngleFalconConfig.java](/src/main/java/frc/robot/drivetrain/config/AngleFalconConfig.java) | Java | 49 | 21 | 13 | 83 |
| [src/main/java/frc/robot/drivetrain/config/CanCoderConfig.java](/src/main/java/frc/robot/drivetrain/config/CanCoderConfig.java) | Java | 12 | 7 | 5 | 24 |
| [src/main/java/frc/robot/drivetrain/config/DriveFalconConfig.java](/src/main/java/frc/robot/drivetrain/config/DriveFalconConfig.java) | Java | 44 | 16 | 12 | 72 |
| [src/main/java/frc/robot/drivetrain/config/DrivetrainConfig.java](/src/main/java/frc/robot/drivetrain/config/DrivetrainConfig.java) | Java | 95 | 63 | 30 | 188 |
| [src/main/java/frc/robot/subsystems/climber/ClimberConfig.java](/src/main/java/frc/robot/subsystems/climber/ClimberConfig.java) | Java | 52 | 17 | 20 | 89 |
| [src/main/java/frc/robot/subsystems/climber/ClimberSubSys.java](/src/main/java/frc/robot/subsystems/climber/ClimberSubSys.java) | Java | 70 | 19 | 25 | 114 |
| [src/main/java/frc/robot/subsystems/climber/ClimberTelemetry.java](/src/main/java/frc/robot/subsystems/climber/ClimberTelemetry.java) | Java | 11 | 0 | 4 | 15 |
| [src/main/java/frc/robot/subsystems/climber/commands/ClimberCmds.java](/src/main/java/frc/robot/subsystems/climber/commands/ClimberCmds.java) | Java | 38 | 4 | 9 | 51 |
| [src/main/java/frc/robot/subsystems/elevator/ElevatorConfig.java](/src/main/java/frc/robot/subsystems/elevator/ElevatorConfig.java) | Java | 58 | 17 | 21 | 96 |
| [src/main/java/frc/robot/subsystems/elevator/ElevatorSubSys.java](/src/main/java/frc/robot/subsystems/elevator/ElevatorSubSys.java) | Java | 98 | 18 | 24 | 140 |
| [src/main/java/frc/robot/subsystems/elevator/ElevatorTelemetry.java](/src/main/java/frc/robot/subsystems/elevator/ElevatorTelemetry.java) | Java | 13 | 0 | 4 | 17 |
| [src/main/java/frc/robot/subsystems/elevator/commands/ElevatorCmds.java](/src/main/java/frc/robot/subsystems/elevator/commands/ElevatorCmds.java) | Java | 52 | 4 | 14 | 70 |
| [src/main/java/frc/robot/subsystems/intake/IntakeConfig.java](/src/main/java/frc/robot/subsystems/intake/IntakeConfig.java) | Java | 18 | 9 | 13 | 40 |
| [src/main/java/frc/robot/subsystems/intake/IntakeSubSys.java](/src/main/java/frc/robot/subsystems/intake/IntakeSubSys.java) | Java | 77 | 31 | 25 | 133 |
| [src/main/java/frc/robot/subsystems/intake/IntakeTelemetry.java](/src/main/java/frc/robot/subsystems/intake/IntakeTelemetry.java) | Java | 9 | 2 | 4 | 15 |
| [src/main/java/frc/robot/subsystems/intake/commands/IntakeCmds.java](/src/main/java/frc/robot/subsystems/intake/commands/IntakeCmds.java) | Java | 46 | 15 | 15 | 76 |
| [src/main/java/frc/robot/subsystems/orchestra/orchestraCmds.java](/src/main/java/frc/robot/subsystems/orchestra/orchestraCmds.java) | Java | 15 | 0 | 5 | 20 |
| [src/main/java/frc/robot/subsystems/orchestra/orchestraSubSys.java](/src/main/java/frc/robot/subsystems/orchestra/orchestraSubSys.java) | Java | 61 | 2 | 21 | 84 |

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)