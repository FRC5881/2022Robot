// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.tvhsfrc.frc2022.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import org.tvhsfrc.frc2022.robot.commands.*;
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
    // The robot's subsystems and commands are defined here...
    private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
    private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();


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

        climberSubsystem.setDefaultCommand(new ClimbCommand(
                climberSubsystem, () -> driveController.getPOV(0) == 0, () -> driveController.getPOV(0) == 180
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

        JoystickButton rTrigger = new JoystickButton(driveController, XboxController.Button.kRightBumper.value);
        rTrigger.whenHeld(new ShootCommand(shooterSubsystem));
    }
    
    
    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        // An ExampleCommand will run in autonomous
        //return autoCommand;
        return null;
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
