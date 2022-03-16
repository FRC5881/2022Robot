package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.ShooterSubsystem;

import java.util.function.DoubleSupplier;

public class PrimitiveShootCommand extends CommandBase {
    private ShooterSubsystem shooterSubsystem;
    private DoubleSupplier doubleSupplier;

    public PrimitiveShootCommand(ShooterSubsystem shooterSubsystem, DoubleSupplier valueDouble) {
        this.shooterSubsystem = shooterSubsystem;
        this.doubleSupplier = valueDouble;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        shooterSubsystem.shootPercentage(doubleSupplier.getAsDouble());
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
