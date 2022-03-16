package org.tvhsfrc.frc2022.robot.commands;

import org.tvhsfrc.frc2022.robot.subsystems.ShooterSubsystem;

public class ShootForDurationCommand extends ShootCommand{
    private final double durationMillis;
    private double startMillis;

    private ShooterSubsystem shooterSubsystem;

    /**
     *
     * @param shooterSubsystem
     * @param durationMillis Duration in milliseconds
     */
    public ShootForDurationCommand(ShooterSubsystem shooterSubsystem, double durationMillis) {
        super(shooterSubsystem);
        this.shooterSubsystem = shooterSubsystem;
        this.durationMillis = durationMillis;
    }

    @Override
    public void execute() {
        shooterSubsystem.shootPercentage(0.9);
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() > (startMillis + durationMillis);
    }

    @Override
    public void initialize() {
        startMillis = System.currentTimeMillis();
    }
}
