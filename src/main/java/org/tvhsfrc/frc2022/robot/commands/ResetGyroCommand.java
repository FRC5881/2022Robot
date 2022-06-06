package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.swervelib.SwerveSubsystem;
import org.tvhsfrc.frc2022.robot.subsystems.DrivetrainSubsystem;

public class ResetGyroCommand extends CommandBase {
    SwerveSubsystem swerveSubsystem;

    public ResetGyroCommand(SwerveSubsystem swerveSubsystem) {
        this.swerveSubsystem = swerveSubsystem;
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        if (swerveSubsystem.dt.getGyroReady()) {
            swerveSubsystem.dt.zeroGyroscope();
            System.out.println("Gyro Reset!");
        } else {
            System.err.println("Gyro Not Ready - Un able to Reset");
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
