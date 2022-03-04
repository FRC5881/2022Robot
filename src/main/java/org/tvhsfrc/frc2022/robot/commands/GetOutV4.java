package org.tvhsfrc.frc2022.robot.commands;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.tvhsfrc.frc2022.robot.subsystems.IntakeSubsystem;
import frc.swervelib.SwerveSubsystem;

public class GetOutV4 extends SequentialCommandGroup {
    public GetOutV4(SwerveSubsystem swerveSubsystem, IntakeSubsystem intakeSubsystem) {
        //TODO: get accurate velocity acceleration
        PathPlannerTrajectory trajectory1 = PathPlanner.loadPath("GTFO4", 8.0, 1.0);

        addCommands(
                new InstantCommand(() -> swerveSubsystem.dt.setKnownPose(trajectory1.getInitialPose())),
                swerveSubsystem.dt.createCommandForTrajectory(trajectory1, swerveSubsystem)
        );
    }
}
