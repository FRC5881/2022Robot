package org.tvhsfrc.frc2022.robot.commands;

import org.tvhsfrc.frc2022.robot.subsystems.ShooterSubsystem;

public class ShootForDurationCommand extends ShootCommand{
    private final double durationMillis;
    private double startMillis;

    /**
     *
     * @param shooterSubsystem
     * @param duration Duration in milliseconds
     */
    public ShootForDurationCommand(ShooterSubsystem shooterSubsystem, double duration) {
        super(shooterSubsystem);
        durationMillis = duration*1000;
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
