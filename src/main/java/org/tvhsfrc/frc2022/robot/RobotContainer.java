// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.tvhsfrc.frc2022.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.swervelib.SwerveDrivetrainModel;
import frc.swervelib.SwerveSubsystem;
import org.tvhsfrc.frc2022.robot.commands.*;
import org.tvhsfrc.frc2022.robot.commands.autos.Go5FtBack;
import org.tvhsfrc.frc2022.robot.subsystems.*;
import org.tvhsfrc.frc2022.robot.subsystems.ClimberSubsystem;
import org.tvhsfrc.frc2022.robot.subsystems.DrivetrainSubsystem;
import org.tvhsfrc.frc2022.robot.subsystems.IntakeSubsystem;
import org.tvhsfrc.frc2022.robot.subsystems.ShooterSubsystem;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer
{
    public static SwerveDrivetrainModel swerveDrivetrainModel;
    public static SwerveSubsystem swerveSubsystem;

    public final PhotonVision photonVision = new PhotonVision();

    // The robot's subsystems and commands are defined here...
    private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
    private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();

    private Command autoCommand;


    private final XboxController5881 driveController = new XboxController5881(0, 0.1);

    
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer()
    {
        swerveDrivetrainModel = SwerveDriveHelper.createBearSwerve();
        swerveSubsystem = SwerveDriveHelper.createSwerveSubsystem(swerveDrivetrainModel);

        swerveSubsystem.setDefaultCommand(new RunCommand(() ->
                swerveDrivetrainModel.setModuleStates(driveController.getSwerveInput()), swerveSubsystem));

        photonVision.fieldSetup(swerveDrivetrainModel.getField());

        autoCommand = new Go5FtBack(swerveSubsystem, intakeSubsystem);

        climberSubsystem.setDefaultCommand(new ClimbCommand(
                climberSubsystem, () -> driveController.getPOV(0) == 0, () -> driveController.getPOV(0) == 180,
                () -> driveController.getPOV(0) == 270, () -> driveController.getPOV(0) == 90
        ));

        // Configure the button bindings
        configureButtonBindings();
    }
    
    
    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings()
    {
        JoystickButton aButton = new JoystickButton(driveController, XboxController.Button.kA.value);
        aButton.toggleWhenPressed(new IntakeDeployToggle(intakeSubsystem));

        JoystickButton lTrigger = new JoystickButton(driveController, XboxController.Button.kLeftBumper.value);
        lTrigger.toggleWhenPressed(new IntakeRollerToggle(intakeSubsystem));

        JoystickButton xboxButton = new JoystickButton(driveController, XboxController.Button.kStart.value);
        xboxButton.whenPressed(new ResetGyroCommand(drivetrainSubsystem));

        JoystickButton lBumper = new JoystickButton(driveController, XboxController.Button.kLeftBumper.value);
        lBumper.whenHeld(new ShootCommand(shooterSubsystem));

        JoystickButton rBumper = new JoystickButton(driveController, XboxController.Button.kRightBumper.value);
        rBumper.whenHeld(new RollBeltCommand(shooterSubsystem));
    }
    
    
    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        // An ExampleCommand will run in autonomous
        return autoCommand;
    }

    private static double deadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }

    private static double modifyAxis(double value) {
        // Deadband
        value = deadband(value, 0.07);

        // Square the axis
        value = Math.copySign(value * value, value);

        return value;
    }
}
