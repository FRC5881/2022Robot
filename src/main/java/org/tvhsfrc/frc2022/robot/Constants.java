// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.tvhsfrc.frc2022.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    /**
     * Robot Mass (kg)
     */
    public static final double ROBOT_MASS = Units.lbsToKilograms(130); // TODO Fix me

    /**
     * The left-to-right distance between the drivetrain wheels
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.4445;
    /**
     * The front-to-back distance between the drivetrain wheels.
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_WHEELBASE_METERS = 0.7239;

    /**
     * Model moment of intertia as a square slab slightly bigger than wheelbase with axis through center
     */
    //FIXME: Not right - not a square
    public static final double ROBOT_MOI_KGM2 = 1.0/12.0 * ROBOT_MASS * Math.pow((DRIVETRAIN_TRACKWIDTH_METERS*1.1),2) * 2;

    public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 10;
    public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 11;
    public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 1;
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(43.6816);

    public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 12;
    public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 13;
    public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 2;
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(40.166);

    public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 16;
    public static final int BACK_LEFT_MODULE_STEER_MOTOR = 17;
    public static final int BACK_LEFT_MODULE_STEER_ENCODER = 4;
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(135.9667);

    public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 14;
    public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 15;
    public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 3;
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(73.3886);

    public static final SwerveDriveKinematics DRIVE_KINEMATICS = new SwerveDriveKinematics(
            // Front left
            new Translation2d(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0),
            // Front right
            new Translation2d(DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DRIVETRAIN_WHEELBASE_METERS / 2.0),
            // Back left
            new Translation2d(-DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0),
            // Back right
            new Translation2d(-DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DRIVETRAIN_WHEELBASE_METERS / 2.0)
    );
    /**
     * RoboRIO Input Channel for the A Sensor in the Intake
     */
    public static final int INTAKE_SENSOR_A_CHANNEL = 1;
    /**
     * RoboRIO Input Channel for the B Sensor in the Intake
     */
    public static final int INTAKE_SENSOR_B_CHANNEL = 2;

    /**
     * SparkMAX Shooter Motor 1 CAN bus ID
     */
    public static final int SHOOTER_MOTOR_1_ID = 20;
    /**
     * SparkMAX Shooter Motor 2 CAN bus ID
     */
    public static final int SHOOTER_MOTOR_2_ID = 21;
    /**
     * SparkMAX Belt Motor CAN bus ID
     */
    public static final int BELT_MOTOR_ID = 22;

    /**
     * SparkMAX Intake Arm Motor 1 CAN bus ID
     */
    public static final int INTAKE_ARM_MOTOR_1_ID = 25;
    /**
     * SparkMAX Intake Arm Motor 2 CAN bus ID
     */
    public static final int INTAKE_ARM_MOTOR_2_ID = 26;
    /**
     * SparkMAX Intake Roller Motor CAN bus ID
     */
    public static final int INTAKE_ROLLER_MOTOR_ID = 27;

    public static final int CLIMBER_MOTOR_1_ID = 28;
    public static final int CLIMBER_MOTOR_2_ID = 29;

    public static final String PHOTONVISION_NAME_SHOOTER = "shooter";
    public static final String PHOTONVISION_NAME_INTAKE = "intake";
    public static final double PHOTONVISION_SHOOTER_CAM_ANGLE = 58;
    public static final double PHOTONVISION_SHOOTER_CAM_HEIGHT = Units.inchesToMeters(28);

    public static final double PHOTONVISION_INTAKE_CAM_ANGLE = 0; // TODO
    public static final double PHOTONVISION_INTAKE_CAM_HEIGHT = 0;

}
