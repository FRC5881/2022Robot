package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.ShooterSubsystem;

public class RollBeltCommand extends CommandBase {
    private ShooterSubsystem shooterSubsystem;

    public RollBeltCommand(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;

        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        shooterSubsystem.runIntake();
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.stopShooting();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
