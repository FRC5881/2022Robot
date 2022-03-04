package org.tvhsfrc.frc2022.robot.subsystems;
import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.swervelib.Gyroscope;
import frc.swervelib.GyroscopeHelper;
import frc.swervelib.Mk4SwerveModuleHelper;
import frc.swervelib.SwerveConstants;
import frc.swervelib.SwerveModule;
import frc.swervelib.SwerveSubsystem;
import frc.swervelib.SwerveDrivetrainModel;
import frc.wpiClasses.QuadSwerveSim;

import static org.tvhsfrc.frc2022.robot.Constants.*;
import static org.tvhsfrc.frc2022.robot.Constants.FRONT_LEFT_MODULE_STEER_OFFSET;

public class SwerveDriveHelper {
    public static SwerveDrivetrainModel createBearSwerve() {
        passConstants();
        ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");
        ArrayList<SwerveModule> modules = new ArrayList<SwerveModule>(QuadSwerveSim.NUM_MODULES);
        Gyroscope gyro;

        SwerveModule m_frontLeftModule = Mk4SwerveModuleHelper.createNeo(
                // This parameter is optional, but will allow you to see the current state of the module on the dashboard.
                tab.getLayout("Front Left Module", BuiltInLayouts.kList)
                        .withSize(2, 4)
                        .withPosition(0, 0),
                // This can either be STANDARD or FAST depending on your gear configuration
                Mk4SwerveModuleHelper.GearRatio.L1,
                // This is the ID of the drive motor
                FRONT_LEFT_MODULE_DRIVE_MOTOR,
                // This is the ID of the steer motor
                FRONT_LEFT_MODULE_STEER_MOTOR,
                // This is the ID of the steer encoder
                FRONT_LEFT_MODULE_STEER_ENCODER,
                // This is how much the steer encoder is offset from true zero (In our case, zero is facing straight forward)
                FRONT_LEFT_MODULE_STEER_OFFSET, "FL"
        );

        // We will do the same for the other modules
        SwerveModule m_frontRightModule = Mk4SwerveModuleHelper.createNeo(
                tab.getLayout("Front Right Module", BuiltInLayouts.kList)
                        .withSize(2, 4)
                        .withPosition(2, 0),
                Mk4SwerveModuleHelper.GearRatio.L1,
                FRONT_RIGHT_MODULE_DRIVE_MOTOR,
                FRONT_RIGHT_MODULE_STEER_MOTOR,
                FRONT_RIGHT_MODULE_STEER_ENCODER,
                FRONT_RIGHT_MODULE_STEER_OFFSET, "FR"
        );

        SwerveModule m_backLeftModule = Mk4SwerveModuleHelper.createNeo(
                tab.getLayout("Back Left Module", BuiltInLayouts.kList)
                        .withSize(2, 4)
                        .withPosition(4, 0),
                Mk4SwerveModuleHelper.GearRatio.L1,
                BACK_LEFT_MODULE_DRIVE_MOTOR,
                BACK_LEFT_MODULE_STEER_MOTOR,
                BACK_LEFT_MODULE_STEER_ENCODER,
                BACK_LEFT_MODULE_STEER_OFFSET, "BL"
        );

        SwerveModule m_backRightModule = Mk4SwerveModuleHelper.createNeo(
                tab.getLayout("Back Right Module", BuiltInLayouts.kList)
                        .withSize(2, 4)
                        .withPosition(6, 0),
                Mk4SwerveModuleHelper.GearRatio.L1,
                BACK_RIGHT_MODULE_DRIVE_MOTOR,
                BACK_RIGHT_MODULE_STEER_MOTOR,
                BACK_RIGHT_MODULE_STEER_ENCODER,
                BACK_RIGHT_MODULE_STEER_OFFSET, "BR"
        );

        gyro = GyroscopeHelper.createnavXMXP();

        modules.add(m_frontLeftModule);
        modules.add(m_frontRightModule);
        modules.add(m_backLeftModule);
        modules.add(m_backRightModule);
        return new SwerveDrivetrainModel(modules, gyro);
    }

    public static SwerveSubsystem createSwerveSubsystem(SwerveDrivetrainModel dt) {
        return new SwerveSubsystem(dt);
    }

    private static void passConstants() {
        SwerveConstants.MAX_FWD_REV_SPEED_MPS = Units.feetToMeters(12.0);
        SwerveConstants.MAX_STRAFE_SPEED_MPS = Units.feetToMeters(12.0);
        SwerveConstants.MAX_ROTATE_SPEED_RAD_PER_SEC = Units.degreesToRadians(180.0);
        SwerveConstants.MAX_VOLTAGE = 12;
        // TODO: ???
        SwerveConstants.DFLT_START_POSE = new Pose2d(Units.feetToMeters(24.0), Units.feetToMeters(10.0), Rotation2d.fromDegrees(0));

        SwerveConstants.THETACONTROLLERkP = 1;
        SwerveConstants.TRAJECTORYXkP = 1;
        SwerveConstants.TRAJECTORYYkP = 1;
        SwerveConstants.THETACONTROLLERCONSTRAINTS = new TrapezoidProfile.Constraints(
                Math.PI, Math.PI);

        SwerveConstants.TRACKWIDTH_METERS = DRIVETRAIN_TRACKWIDTH_METERS;
        SwerveConstants.TRACKLENGTH_METERS = DRIVETRAIN_WHEELBASE_METERS;
        SwerveConstants.MASS_kg = ROBOT_MASS;
        SwerveConstants.MOI_KGM2 = ROBOT_MOI_KGM2;
        SwerveConstants.KINEMATICS = DRIVE_KINEMATICS;
    }
}