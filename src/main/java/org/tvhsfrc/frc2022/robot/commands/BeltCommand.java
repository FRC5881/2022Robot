package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.BeltSubsystem;

import java.util.function.DoubleSupplier;

public class BeltCommand extends CommandBase {
    private BeltSubsystem beltSubsystem;
    private DoubleSupplier valueDouble;

    public BeltCommand(BeltSubsystem beltSubsystem, DoubleSupplier valueDouble) {
        this.beltSubsystem = beltSubsystem;
        this.valueDouble = valueDouble;
        addRequirements(beltSubsystem);
    }

    @Override
    public void execute() {
        beltSubsystem.run(valueDouble.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        beltSubsystem.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
