package org.tvhsfrc.frc2022.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    private CANSparkMax climberLeft = new CANSparkMax(28, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax climberRight = new CANSparkMax(29, CANSparkMaxLowLevel.MotorType.kBrushless);

    private static final double CLIMBER_SPOOL_CIRCUMFERENCE = Math.PI * 0.787 /* in */;
    private static final double CLIMBER_GEAR_RATIO = 1d / 35d;

    private double p,i,d;

    private double climbSpeed = 0.4;

    public ClimberSubsystem() {
        climberLeft.setInverted(false);
        climberRight.setInverted(true);

        climberLeft.setIdleMode(CANSparkMax.IdleMode.kBrake);
        climberRight.setIdleMode(CANSparkMax.IdleMode.kBrake);

        climberLeft.getEncoder().setPositionConversionFactor(CLIMBER_SPOOL_CIRCUMFERENCE * CLIMBER_GEAR_RATIO / 42d ); // TODO: Check this - 42 ticks/rev
        climberRight.getEncoder().setPositionConversionFactor(CLIMBER_SPOOL_CIRCUMFERENCE * CLIMBER_GEAR_RATIO / 42d);

        p=0.4;// TODO: Probably horribly wrong
        i=0;
        d=0.002;

        climberLeft.getPIDController().setP(p);
        climberLeft.getPIDController().setI(i);
        climberLeft.getPIDController().setD(d);
        climberRight.getPIDController().setP(p);
        climberRight.getPIDController().setI(i);
        climberRight.getPIDController().setD(d);
    }

    @Override
    public void periodic() {
        super.periodic();
    }

    @Override
    public void simulationPeriodic() {
        super.simulationPeriodic();
    }

    private void setSpeed(double speed) {
        setLeftSpeed(speed);
        setRightSpeed(speed);
    }

    private void setLeftSpeed(double speed) {
        climberLeft.set(speed);
    }

    private void setRightSpeed(double speed) {
        climberRight.set(speed);
    }

    public double getClimbSpeed() {
        return climbSpeed;
    }

    public void setClimbSpeed(double climbSpeed) {
        this.climbSpeed = climbSpeed;
    }

    public void climb() {
        setSpeed(climbSpeed);
    }

    public void climbLeft() { setLeftSpeed(climbSpeed); }

    public void climbRight() { setRightSpeed(climbSpeed); }

    public void lower() {
        setSpeed(-1 * climbSpeed);
    }

    public void stop() {
        climberRight.stopMotor();
        climberLeft.stopMotor();
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
        climberLeft.getPIDController().setP(p);
        climberRight.getPIDController().setP(p);
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
        climberLeft.getPIDController().setI(i);
        climberRight.getPIDController().setI(i);
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
        climberLeft.getPIDController().setD(d);
        climberRight.getPIDController().setD(d);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.setSmartDashboardType("ClimberSubsystem");
        builder.addDoubleProperty("Speed", this::getClimbSpeed, this::setClimbSpeed);
        builder.addDoubleProperty("PID-p", this::getP, this::setP);
        builder.addDoubleProperty("PID-i", this::getI, this::setI);
        builder.addDoubleProperty("PID-d", this::getD, this::setD);
    }
}
