package org.tvhsfrc.frc2022.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
    public IntakeSubsystem() {
        // Invert encoder for one of the motors
        //TODO: Needs testing
        intakeAdjMotor1.getEncoder().setInverted(true);
        intakeAdjMotor2.getEncoder().setInverted(false);

        //TODO: These may be horribly, horribly, wrong. Please test.
        intakeAdjMotor1.getPIDController().setP(0.2);
        intakeAdjMotor1.getPIDController().setI(0);
        intakeAdjMotor1.getPIDController().setD(0.002);
        intakeAdjMotor2.getPIDController().setP(0.2);
        intakeAdjMotor2.getPIDController().setI(0);
        intakeAdjMotor2.getPIDController().setD(0.002);

        intakeAdjMotor1.getPIDController().setFeedbackDevice(intakeAdjMotor1.getEncoder());
        intakeAdjMotor2.getPIDController().setFeedbackDevice(intakeAdjMotor2.getEncoder());

        intakeAdjMotor1.getEncoder().setPositionConversionFactor(360);
        intakeAdjMotor2.getEncoder().setPositionConversionFactor(360);

        forwardLimit1 = intakeAdjMotor1.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        reverseLimit1 = intakeAdjMotor1.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        forwardLimit1.enableLimitSwitch(true);
        reverseLimit1.enableLimitSwitch(true);
        forwardLimit2 = intakeAdjMotor2.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        reverseLimit2 = intakeAdjMotor2.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyClosed);
        forwardLimit2.enableLimitSwitch(true);
        reverseLimit2.enableLimitSwitch(true);
    }

    private final CANSparkMax rollerMotor = new CANSparkMax(20, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax intakeAdjMotor1 = new CANSparkMax(21, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax intakeAdjMotor2 = new CANSparkMax(22, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final SparkMaxLimitSwitch forwardLimit1;
    private final SparkMaxLimitSwitch reverseLimit1;
    private final SparkMaxLimitSwitch forwardLimit2;
    private final SparkMaxLimitSwitch reverseLimit2;

    private final double extendedPosition = 95;
    private final double insidePosition = 0;

    private double targetPosition = Double.MIN_VALUE;


    public void toggleDeployment() {
        double curPos = intakeAdjMotor1.getEncoder().getPosition();
        if (curPos < 47 || curPos > 340) {
            // Set to extendedPosition
            targetPosition = extendedPosition;
            intakeAdjMotor1.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition);
            intakeAdjMotor2.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition);
            runRollerMotors();
        } else {
            // set to insidePosition
            targetPosition = insidePosition;
            intakeAdjMotor1.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition);
            intakeAdjMotor2.getPIDController().setReference(targetPosition, CANSparkMax.ControlType.kPosition);
            stopRollerMotors();
        }
    }

    public boolean isDeploymentComplete() {
        return false;
        //TODO:Finish Me
    }

    public void runRollerMotors() {
        rollerMotor.set(0.5);
    }

    public void stopRollerMotors() {
        rollerMotor.stopMotor();
    }


    @Override
    public void periodic()
    { }


    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }
}
