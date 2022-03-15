package org.tvhsfrc.frc2022.robot.commands.autos;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.swervelib.SwerveSubsystem;
import org.tvhsfrc.frc2022.robot.commands.ShootForDurationCommand;
import org.tvhsfrc.frc2022.robot.subsystems.IntakeSubsystem;
import org.tvhsfrc.frc2022.robot.subsystems.ShooterSubsystem;

public class Go5FtBackAndShoot extends SequentialCommandGroup {
    public Go5FtBackAndShoot(SwerveSubsystem swerveSubsystem, ShooterSubsystem shooterSubsystem) {
        //TODO: get accurate velocity acceleration
        PathPlannerTrajectory trajectory1 = PathPlanner.loadPath("5ftBack", 8.0, 1.0);

        addCommands(
                new InstantCommand(() -> swerveSubsystem.dt.setKnownPose(trajectory1.getInitialPose())),
                swerveSubsystem.dt.createCommandForTrajectory(trajectory1, swerveSubsystem),
                new ShootForDurationCommand(shooterSubsystem, 5000)
        );
    }
}
