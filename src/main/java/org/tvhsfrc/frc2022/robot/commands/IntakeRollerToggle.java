package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.IntakeSubsystem;

public class IntakeRollerToggle extends CommandBase {
    private final IntakeSubsystem intakeSubsystem;

    public IntakeRollerToggle(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intakeSubsystem.toggleRollers();
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) // Only stop the motors if we're interrupted by an aborted command
            intakeSubsystem.stopRollerMotors();
    }

    @Override
    public boolean isFinished() {
        return true; // This command is a simple toggle, after setting the new motor position it's done.
    }
}