package org.tvhsfrc.frc2022.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.SimVisionSystem;
import org.photonvision.SimVisionTarget;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import org.tvhsfrc.frc2022.robot.Constants;
import org.tvhsfrc.frc2022.robot.FieldConstants;

public class PhotonVision {
    // Creates a new PhotonCamera.
    public PhotonCamera targeting = new PhotonCamera(Constants.PHOTONVISION_NAME_SHOOTER);
    public PhotonCamera intake = new PhotonCamera(Constants.PHOTONVISION_NAME_INTAKE);
    public SimVisionSystem intakeVisionSys;
    public SimVisionSystem shooterVisionSys;

    public PhotonVision() {
        targeting.setPipelineIndex(0); //TODO Multiple Pipelines?
        intake.setPipelineIndex(0);

        double intakeCamDiagFOV = 75.0; // degrees
        double shooterCamDiagFOV = 75.0; // degrees
        Transform2d intakeCameraToRobot = new Transform2d(new Translation2d(0.0, 0.0), new Rotation2d()); // TODO: Find location relative to center
        Transform2d shooterCameraToRobot = new Transform2d(new Translation2d(0.0, 0.0), new Rotation2d()); // meters
        double maxLEDRange = 20;          // meters
        int intakeCamResolutionWidth = 640;     // pixels
        int intakeCamResolutionHeight = 480;    // pixels
        double intakeMinTargetArea = 10;        // square pixels
        int shooterCamResolutionWidth = 640;     // pixels
        int shooterCamResolutionHeight = 480;    // pixels
        double shooterMinTargetArea = 10;        // square pixels


        intakeVisionSys = new SimVisionSystem(Constants.PHOTONVISION_NAME_INTAKE,
                intakeCamDiagFOV,
                Constants.PHOTONVISION_INTAKE_CAM_ANGLE,
                intakeCameraToRobot,
                Constants.PHOTONVISION_INTAKE_CAM_HEIGHT,
                9000, // does not use LEDs
                intakeCamResolutionWidth,
                intakeCamResolutionHeight,
                intakeMinTargetArea);

        shooterVisionSys = new SimVisionSystem(Constants.PHOTONVISION_NAME_SHOOTER,
                shooterCamDiagFOV,
                Constants.PHOTONVISION_SHOOTER_CAM_ANGLE,
                shooterCameraToRobot,
                Constants.PHOTONVISION_SHOOTER_CAM_HEIGHT,
                maxLEDRange,
                shooterCamResolutionWidth,
                shooterCamResolutionHeight,
                shooterMinTargetArea);

        double tgtXPos = Units.feetToMeters(54 / 2.0); // Midfield
        double tgtYPos = Units.feetToMeters(27 / 2.0);
        var targetPose = new Pose2d(new Translation2d(tgtXPos, tgtYPos), Rotation2d.fromDegrees(-21.0)); // meters // TODO What is this?
        double ballTargetWidth = Units.inchesToMeters(9.5);
        double shooterTargetWidth = Units.inchesToMeters(36); // Actually 4ft wide but this is a straight replacement for a curved goal
        double shooterTargetHeight = Units.inchesToMeters(2);

        var ball = new Pose2d(FieldConstants.cargoD.getX(), FieldConstants.cargoD.getY(), new Rotation2d(0)); // Ball position?
        var ballTgt = RectangularSimVisionTarget(ball,
                0,
                ballTargetWidth,
                ballTargetWidth, // same as height
                ballTargetWidth);

        var shooterTgt = RectangularSimVisionTarget(targetPose,
                FieldConstants.visionTargetHeightLower,
                shooterTargetWidth,
                shooterTargetHeight,
                shooterTargetWidth); // width is same as depth since 4ft circle

        for (int i = 0; i < ballTgt.size(); i++) {
            intakeVisionSys.addSimVisionTarget(ballTgt.get(i));
        }
        for (int i = 0; i < shooterTgt.size(); i++) {
            shooterVisionSys.addSimVisionTarget(shooterTgt.get(i));
        }

        NetworkTableInstance.getDefault().getTable("photonvision").getEntry("version").setValue("v2022.1.4");
    }

    public void fieldSetup(Field2d field) {
        var ball = field.getObject("ball");
        var hub = field.getObject("hub");

        ball.setPose(FieldConstants.cargoD.getX(), FieldConstants.cargoD.getY(), Rotation2d.fromDegrees(0));
        hub.setPose(FieldConstants.hubCenter.getX(), FieldConstants.hubCenter.getY(), Rotation2d.fromDegrees(-21));
    }

    public void lightsOn() {
        //m_limelight.setLED(VisionLEDMode.kOn);
    }

    public void lightsOff() {
        //m_limelight.setLED(VisionLEDMode.kOff);
    }

    public double getYaw() {
        var result = targeting.getLatestResult();
        if (result.hasTargets()) {
            return result.getBestTarget().getYaw();
        }
        return -999.0;
    }

    // TODO Both of these are dangerous and need "hasTargets" needs to be checked before using
    public double distanceToBallTarget(PhotonPipelineResult result) {
        return PhotonUtils.calculateDistanceToTargetMeters(Constants.PHOTONVISION_INTAKE_CAM_HEIGHT,
                Units.inchesToMeters(9.5),
                Constants.PHOTONVISION_INTAKE_CAM_ANGLE,
                Units.degreesToRadians(result.getBestTarget().getPitch()));
    }

    public double distanceToShooterTarget(PhotonPipelineResult result) {
        return PhotonUtils.calculateDistanceToTargetMeters(Constants.PHOTONVISION_SHOOTER_CAM_HEIGHT,
                FieldConstants.visionTargetHeightLower,
                Constants.PHOTONVISION_SHOOTER_CAM_ANGLE,
                Units.degreesToRadians(result.getBestTarget().getPitch()));
    }

    public List<SimVisionTarget> RectangularSimVisionTarget(
            Pose2d targetPos,
            double targetHeightAboveGroundMeters,
            double targetWidthMeters,
            double targetHeightMeters,
            double targetDepthMeters) {
        List<SimVisionTarget> targetList = new ArrayList<>();

        var targetPos1 = targetPos.transformBy(new Transform2d(
                new Translation2d(0, -targetDepthMeters/2).rotateBy(targetPos.getRotation()),
                Rotation2d.fromDegrees(180.0)));

        var targetPos2 = targetPos.transformBy(new Transform2d(
                new Translation2d(-targetWidthMeters/2, 0).rotateBy(targetPos.getRotation()),
                Rotation2d.fromDegrees(-90.0)));

        var targetPos3 = targetPos.transformBy(new Transform2d(
                new Translation2d(0, targetDepthMeters/2).rotateBy(targetPos.getRotation()),
                Rotation2d.fromDegrees(0.0)));

        var targetPos4 = targetPos.transformBy(new Transform2d(
                new Translation2d(targetWidthMeters/2, 0).rotateBy(targetPos.getRotation()),
                Rotation2d.fromDegrees(90.0)));

        targetList.add(new SimVisionTarget(targetPos1, // Initial face
                targetHeightAboveGroundMeters,
                targetWidthMeters,
                targetHeightMeters));
        targetList.add(new SimVisionTarget(targetPos2, // Left face
                targetHeightAboveGroundMeters,
                targetDepthMeters, // On the side width is the initial depth
                targetHeightMeters));
        targetList.add(new SimVisionTarget(targetPos3, // Back face
                targetHeightAboveGroundMeters,
                targetWidthMeters,
                targetHeightMeters));
        targetList.add(new SimVisionTarget(targetPos4, // Right face
                targetHeightAboveGroundMeters,
                targetDepthMeters, // On the side width is the initial depth
                targetHeightMeters));
        return targetList;
    }
}