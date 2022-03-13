package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.ClimberSubsystem;

import java.util.function.BooleanSupplier;

public class ClimbCommand extends CommandBase {
    private ClimberSubsystem climber;

    private BooleanSupplier up, down;

    public ClimbCommand(ClimberSubsystem climber, BooleanSupplier up, BooleanSupplier down) {
        this.climber = climber;
        this.up = up;
        this.down = down;
        addRequirements(climber);
    }

    @Override
    public void execute() {
        if (up.getAsBoolean() && ! down.getAsBoolean()) {
            climber.climb();
        } else if (!up.getAsBoolean() && down.getAsBoolean()) {
            climber.lower();
        } else {
            climber.stop();
        }

    }

    @Override
    public void end(boolean interrupted) {
        climber.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
