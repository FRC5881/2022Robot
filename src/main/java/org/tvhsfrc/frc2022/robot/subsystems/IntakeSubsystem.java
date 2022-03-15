package org.tvhsfrc.frc2022.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxLimitSwitch;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.tvhsfrc.frc2022.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {

    private final CANSparkMax rollerMotor = new CANSparkMax(Constants.INTAKE_ROLLER_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax intakeArmMotor1 = new CANSparkMax(Constants.INTAKE_ARM_MOTOR_1_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax intakeArmMotor2 = new CANSparkMax(Constants.INTAKE_ARM_MOTOR_2_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    private double extendedPosition = 9.4;
    private double insidePosition = 0;

    private double targetPosition = insidePosition; // Set initial starting configuration

    private double rollerMotorSpeedPercentage = 0.5;

    private double p, i, d;

    public IntakeSubsystem() {
        // Invert encoder for one of the motors
        //TODO: Needs testing
        intakeArmMotor1.setInverted(true);
        intakeArmMotor2.setInverted(false);
        intakeArmMotor1.setInverted(true);
        intakeArmMotor2.setInverted(false);

        // PID Defaults
        p=0.09;
        i=0;
        d=0.005;

        //TODO: These may be horribly, horribly, wrong. Please test.
        intakeArmMotor1.getPIDController().setP(p);
        intakeArmMotor1.getPIDController().setI(i);
        intakeArmMotor1.getPIDController().setD(d);
        intakeArmMotor2.getPIDController().setP(p);
        intakeArmMotor2.getPIDController().setI(i);
        intakeArmMotor2.getPIDController().setD(d);

        intakeArmMotor1.getPIDController().setFeedbackDevice(intakeArmMotor1.getEncoder());
        intakeArmMotor2.getPIDController().setFeedbackDevice(intakeArmMotor2.getEncoder());

        intakeArmMotor1.getEncoder().setPositionConversionFactor(1); // Per REV site, 42 counts/rev
        intakeArmMotor2.getEncoder().setPositionConversionFactor(1);

        intakeArmMotor1.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).enableLimitSwitch(true);
        intakeArmMotor1.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).enableLimitSwitch(true);
        intakeArmMotor2.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).enableLimitSwitch(true);
        intakeArmMotor2.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).enableLimitSwitch(true);

        intakeArmMotor1.getPIDController().setOutputRange(-1, 0.08);
        intakeArmMotor2.getPIDController().setOutputRange(-1, 0.08);

        intakeArmMotor1.setIdleMode(CANSparkMax.IdleMode.kBrake);
        intakeArmMotor2.setIdleMode(CANSparkMax.IdleMode.kBrake);

        Shuffleboard.getTab("Intake").add(this);
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

    public void toggleRollers() {
        if (getRollerMotorSet() > 0) {
            stopRollerMotors();
        } else {
            runRollerMotors();
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
     *
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

    private boolean isForward1Hit() {
        return intakeArmMotor1.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).isPressed();
    }

    private boolean isReverse1Hit() {
        return intakeArmMotor1.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).isPressed();
    }

    private boolean isForward2Hit() {
        return intakeArmMotor2.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).isPressed();
    }

    private boolean isReverse2Hit() {
        return intakeArmMotor2.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).isPressed();
    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    private double get1Position() {
        return intakeArmMotor1.getEncoder().getPosition();
    }

    private double get2Position() {
        return intakeArmMotor2.getEncoder().getPosition();
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
        intakeArmMotor1.getPIDController().setP(p);
        intakeArmMotor2.getPIDController().setP(p);
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
        intakeArmMotor1.getPIDController().setI(i);
        intakeArmMotor2.getPIDController().setI(i);
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
        intakeArmMotor1.getPIDController().setD(d);
        intakeArmMotor2.getPIDController().setD(d);
    }

    public double getExtendedPosition() {
        return extendedPosition;
    }

    public void setExtendedPosition(double extendedPosition) {
        this.extendedPosition = extendedPosition;
    }

    public double getInsidePosition() {
        return insidePosition;
    }

    public void setInsidePosition(double insidePosition) {
        this.insidePosition = insidePosition;
    }

    public double getRollerMotorSet() {
        return rollerMotor.get();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.setSmartDashboardType("Intake");
        builder.addBooleanProperty("LimitForward1", this::isForward1Hit, null);
        builder.addBooleanProperty("LimitReverse1", this::isReverse1Hit, null);
        builder.addBooleanProperty("LimitForward2", this::isForward2Hit, null);
        builder.addBooleanProperty("LimitReverse2", this::isReverse2Hit, null);
        builder.addDoubleProperty("TargetPosition", this::getTargetPosition, this::setTargetPosition);
        builder.addDoubleProperty("1Position",this::get1Position, null);
        builder.addDoubleProperty("2Position",this::get2Position, null);
        builder.addDoubleProperty("extendedPosition", this::getExtendedPosition, this::setExtendedPosition);
        builder.addDoubleProperty("insidePosition", this::getInsidePosition, this::setInsidePosition);
        builder.addDoubleProperty("PID-p", this::getP, this::setP);
        builder.addDoubleProperty("PID-i", this::getI, this::setI);
        builder.addDoubleProperty("PID-d", this::getD, this::setD);
        builder.addDoubleProperty("RollerSpeed", this::getRollerMotorSet, null);
    }
}
