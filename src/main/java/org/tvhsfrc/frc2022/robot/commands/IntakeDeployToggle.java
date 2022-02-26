package org.tvhsfrc.frc2022.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.tvhsfrc.frc2022.robot.subsystems.IntakeSubsystem;

public class IntakeDeployToggle extends CommandBase {
    private final IntakeSubsystem intakeSubsystem;

    public IntakeDeployToggle(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        intakeSubsystem.toggleDeployment();
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
    return false;
    //TODO: Finish Me
    }
}