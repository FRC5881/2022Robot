package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.BeltSubsystem;

public class BeltForDurationCommand extends CommandBase {

    private final double durationMillis;
    private double startMillis;

    private BeltSubsystem beltSubsystem;

    /**
     *
     * @param beltSubsystem
     * @param durationMillis Duration in milliseconds
     */
    public BeltForDurationCommand(BeltSubsystem beltSubsystem, double durationMillis) {
        this.beltSubsystem = beltSubsystem;
        this.durationMillis = durationMillis;
        addRequirements(beltSubsystem);
    }

    @Override
    public void execute() {
        beltSubsystem.run(0.5);
    }

    @Override
    public void end(boolean interrupted) {
        beltSubsystem.stop();
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
