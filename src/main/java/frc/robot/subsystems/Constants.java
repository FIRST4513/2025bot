package frc.robot.subsystems;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;

public class Constants {

    public static class Vision {

    // Cam mounted facing forward, half a meter forward of center, half a meter up from center.
    // TODO: make real
        public static final Transform3d kRobotToCam =
            new Transform3d(new Translation3d(0.3048, -0.2286, 0.1524), new Rotation3d(0, 0.0872665, 0));
        public static final Transform3d ktRobotToCam =
            new Transform3d(new Translation3d(0.070, -0.070, 0.990), new Rotation3d(0, 0, 0));

    // TODO: make real
        public static final String kCameraName = "Apriltag Camera";
        public static final String ktCameraName = "TopApriltag Camera";

    // makes variable that holds all apriltag positions on the field
        public static final AprilTagFieldLayout kTagLayout =
            AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

        // The standard deviations of our vision estimated poses, which affect correction rate
        // (Fake values. Experiment and determine estimation noise on an actual robot.)
        /**
    * Standard deviations of the vision measurements. Increase these numbers to
    * trust global measurements from vision less. This matrix is in the form
    * [x, y, theta]ᵀ, with units in meters and radians.
    */
        public static final Matrix<N3, N1> kSingleTagStdDevs = VecBuilder.fill(.50, .50, .75);
        public static final Matrix<N3, N1> kMultiTagStdDevs = VecBuilder.fill(0.25, 0.25, .50);

    }
    



}
