package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.ShooterSubsystem;

public class ShootCommand extends CommandBase {
    private final ShooterSubsystem shooterSubsystem;

    public ShootCommand(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        shooterSubsystem.shoot();
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.stopShooting();
    }

    @Override
    public boolean isFinished() {
        return false; // Seems counter-intuitive but don't end this - only run it when holding trigger
    }
}
