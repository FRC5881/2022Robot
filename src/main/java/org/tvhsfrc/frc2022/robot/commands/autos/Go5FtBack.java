package org.tvhsfrc.frc2022.robot.commands.autos;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.swervelib.SwerveSubsystem;
import org.tvhsfrc.frc2022.robot.subsystems.IntakeSubsystem;

public class Go5FtBack extends SequentialCommandGroup {
    public Go5FtBack(SwerveSubsystem swerveSubsystem, IntakeSubsystem intakeSubsystem) {
        //TODO: get accurate velocity acceleration
        PathPlannerTrajectory trajectory1 = PathPlanner.loadPath("5ftBack", 8.0, 1.0);

        addCommands(
                new InstantCommand(() -> swerveSubsystem.dt.setKnownPose(trajectory1.getInitialPose())),
                swerveSubsystem.dt.createCommandForTrajectory(trajectory1, swerveSubsystem)
        );
    }
}
