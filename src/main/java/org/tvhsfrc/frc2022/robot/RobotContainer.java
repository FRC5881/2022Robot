// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.tvhsfrc.frc2022.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.tvhsfrc.frc2022.robot.commands.DefaultDriveCommand;
import org.tvhsfrc.frc2022.robot.commands.ExampleCommand;
import org.tvhsfrc.frc2022.robot.commands.IntakeDeployToggle;
import org.tvhsfrc.frc2022.robot.subsystems.DrivetrainSubsystem;
import org.tvhsfrc.frc2022.robot.subsystems.ExampleSubsystem;
import org.tvhsfrc.frc2022.robot.subsystems.IntakeSubsystem;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer
{
    // The robot's subsystems and commands are defined here...
    private final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
    private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
    private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();

    private final ExampleCommand autoCommand = new ExampleCommand(exampleSubsystem);


    private final XboxController driveController = new XboxController(0);

    
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer()
    {
        // Set up the default command for the drivetrain.
        // The controls are for field-oriented driving:
        // Left stick Y axis -> forward and backwards movement
        // Left stick X axis -> left and right movement
        // Right stick X axis -> rotation
        drivetrainSubsystem.setDefaultCommand(new DefaultDriveCommand(
                drivetrainSubsystem,
                () -> -modifyAxis(driveController.getLeftY()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
                () -> -modifyAxis(driveController.getLeftX()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
                () -> -modifyAxis(driveController.getRightX()) * DrivetrainSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
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
        // Add button to command mappings here.
        // See https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html

        JoystickButton xButton = new JoystickButton(driveController, XboxController.Button.kX.value);
        xButton.toggleWhenPressed(new IntakeDeployToggle(intakeSubsystem));
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
