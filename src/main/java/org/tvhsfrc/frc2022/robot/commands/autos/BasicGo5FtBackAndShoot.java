package org.tvhsfrc.frc2022.robot.commands.autos;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.swervelib.SwerveSubsystem;
import org.tvhsfrc.frc2022.robot.commands.AutoDriveCommand;
import org.tvhsfrc.frc2022.robot.commands.BeltForDurationCommand;
import org.tvhsfrc.frc2022.robot.commands.ShootForDurationCommand;
import org.tvhsfrc.frc2022.robot.subsystems.BeltSubsystem;
import org.tvhsfrc.frc2022.robot.subsystems.ShooterSubsystem;

public class BasicGo5FtBackAndShoot extends SequentialCommandGroup {
    public BasicGo5FtBackAndShoot(SwerveSubsystem swerveSubsystem, ShooterSubsystem shooterSubsystem, BeltSubsystem beltSubsystem) {
        addCommands(
                new AutoDriveCommand(swerveSubsystem, 4000, -0.5, 0, 0),
                new ShootForDurationCommand(shooterSubsystem, 1000),
                new ParallelCommandGroup(new ShootForDurationCommand(shooterSubsystem, 4000), new BeltForDurationCommand(beltSubsystem, 4000))
        );
    }
}
