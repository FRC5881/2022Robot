package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.swervelib.SwerveSubsystem;

public class AutoDriveCommand extends CommandBase {
    private SwerveSubsystem swerveSubsystem;
    private double durationInMillis;
    private double startMillis;
    private double vX, vY, omega;

    public AutoDriveCommand(SwerveSubsystem swerveSubsystem, double durationInMillis, double vX, double vY, double omega) {
        this.swerveSubsystem = swerveSubsystem;
        this.durationInMillis = durationInMillis;
        this.vX = vX;
        this.vY = vY;
        this.omega = omega;
    }

    @Override
    public void initialize() {
        startMillis = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        swerveSubsystem.dt.setModuleStates(new ChassisSpeeds(vX, vY, omega));
    }

    @Override
    public void end(boolean interrupted) {
        swerveSubsystem.dt.setModuleStates(new ChassisSpeeds(0,0,0));
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() > (startMillis + durationInMillis);
    }
}
