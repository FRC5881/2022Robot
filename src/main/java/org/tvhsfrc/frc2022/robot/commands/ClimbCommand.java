package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.ClimberSubsystem;

import java.util.function.BooleanSupplier;

public class ClimbCommand extends CommandBase {
    private ClimberSubsystem climber;

    private BooleanSupplier up, down, left, right;

    public ClimbCommand(ClimberSubsystem climber, BooleanSupplier up, BooleanSupplier down, BooleanSupplier left, BooleanSupplier right) {
        this.climber = climber;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        addRequirements(climber);
    }

    @Override
    public void execute() {
        if (up.getAsBoolean()) {
            climber.climb();
        } else if (down.getAsBoolean()) {
            climber.lower();
        } else if (left.getAsBoolean()) {
            climber.climbLeft();
        } else if (right.getAsBoolean()) {
            climber.climbRight();
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
