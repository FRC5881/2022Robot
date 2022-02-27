package org.tvhsfrc.frc2022.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.tvhsfrc.frc2022.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

    private final CANSparkMax rollerMotor = new CANSparkMax(Constants.INTAKE_ROLLER_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax intakeArmMotor1 = new CANSparkMax(Constants.INTAKE_ARM_MOTOR_1_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax intakeArmMotor2 = new CANSparkMax(Constants.INTAKE_ARM_MOTOR_2_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    private double extendedPosition = 95;
    private double insidePosition = 0;

    private double targetPosition = insidePosition; // Set initial starting configuration

    private double rollerMotorSpeedPercentage = 0.5;

    public IntakeSubsystem() {
        // Invert encoder for one of the motors
        //TODO: Needs testing
        intakeArmMotor1.getEncoder().setInverted(true);
        intakeArmMotor2.getEncoder().setInverted(false);

        //TODO: These may be horribly, horribly, wrong. Please test.
        intakeArmMotor1.getPIDController().setP(0.2);
        intakeArmMotor1.getPIDController().setI(0);
        intakeArmMotor1.getPIDController().setD(0.002);
        intakeArmMotor2.getPIDController().setP(0.2);
        intakeArmMotor2.getPIDController().setI(0);
        intakeArmMotor2.getPIDController().setD(0.002);

        intakeArmMotor1.getPIDController().setFeedbackDevice(intakeArmMotor1.getEncoder());
        intakeArmMotor2.getPIDController().setFeedbackDevice(intakeArmMotor2.getEncoder());

        intakeArmMotor1.getEncoder().setPositionConversionFactor(360);
        intakeArmMotor2.getEncoder().setPositionConversionFactor(360);

        intakeArmMotor1.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).enableLimitSwitch(true);
        intakeArmMotor1.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).enableLimitSwitch(true);
        intakeArmMotor2.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).enableLimitSwitch(true);
        intakeArmMotor2.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).enableLimitSwitch(true);
    }

    /**
     * Changes the deployment from extended to inside or vice versa
     */
    public void toggleDeployment() {
        if (targetPosition == insidePosition) {
            // Set to extendedPosition
            targetPosition = extendedPosition;
            intakeArmMotor1.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition);
            intakeArmMotor2.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition);
            runRollerMotors();
        } else {
            // set to insidePosition
            targetPosition = insidePosition;
            intakeArmMotor1.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition);
            intakeArmMotor2.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition);
            stopRollerMotors();
        }
    }

    /**
     * Determines if the current deployment target has been reached. Inside position is reached if both motor
     * reverse limit switches are pressed. Extended is reached is both motor forward limit switches are reached.
     *
     * @return true if deployment is reached, false if not or if target is not a known position.
     */
    public boolean isDeploymentComplete() {
        if (targetPosition == insidePosition) {
            return intakeArmMotor1.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).isPressed()
                    && intakeArmMotor2.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).isPressed();
        } else if (targetPosition == extendedPosition) {
            return intakeArmMotor1.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).isPressed()
                    && intakeArmMotor2.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).isPressed();
        } else {
            System.err.println("Unknown arm target position set: " + targetPosition);
            return false;
        }
    }

    /**
     * Gets the current target position (in degrees)
     * @return target position in degrees
     */
    public double getTargetPosition() {
        return targetPosition;
    }

    /**
     * Turns on the roller motor
     */
    public void runRollerMotors() {
        rollerMotor.set(rollerMotorSpeedPercentage);
    }

    /**
     * Turns off the roller motor
     */
    public void stopRollerMotors() {
        rollerMotor.stopMotor();
    }

    /**
     * Turns off Arm motors. (May leave the arm in a middle position - used for emergencies)
     */
    public void stopArmMotors() {
        intakeArmMotor1.stopMotor();
        intakeArmMotor2.stopMotor();
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
