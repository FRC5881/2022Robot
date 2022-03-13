package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.DrivetrainSubsystem;

public class ResetGyroCommand extends CommandBase {
    DrivetrainSubsystem drivetrainSubsystem;

    public ResetGyroCommand(DrivetrainSubsystem drivetrainSubsystem) {
        this.drivetrainSubsystem = drivetrainSubsystem;
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.zeroGyroscope();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
