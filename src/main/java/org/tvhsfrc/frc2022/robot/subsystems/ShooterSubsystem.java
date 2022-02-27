package org.tvhsfrc.frc2022.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.tvhsfrc.frc2022.robot.Constants;

public class ShooterSubsystem extends SubsystemBase {
    enum State {
        EMPTY,
        INTAKING_FIRST,
        HOLDING_FIRST,
        INTAKING_SECOND,
        FULL,
        FIRING
    }

    private final CANSparkMax shooterMotor1 = new CANSparkMax(Constants.SHOOTER_MOTOR_1_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax shooterMotor2 = new CANSparkMax(Constants.SHOOTER_MOTOR_2_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax beltMotor = new CANSparkMax(Constants.BELT_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final DigitalInput sensorA = new DigitalInput(Constants.INTAKE_SENSOR_A_CHANNEL);
    private final DigitalInput sensorB = new DigitalInput(Constants.INTAKE_SENSOR_B_CHANNEL);

    public ShooterSubsystem() {
        shooterMotor1.getPIDController().setP(1);
        shooterMotor1.getPIDController().setI(0.001);
        shooterMotor1.getPIDController().setD(0.01);
        shooterMotor1.setIdleMode(CANSparkMax.IdleMode.kCoast);

        shooterMotor2.setInverted(true);
        shooterMotor2.getPIDController().setP(1);
        shooterMotor2.getPIDController().setI(0.001);
        shooterMotor2.getPIDController().setD(0.01);
        shooterMotor2.setIdleMode(CANSparkMax.IdleMode.kCoast);

        beltMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }

    private static final double INTAKE_SPEED = 0.1;

    private boolean lastA = false;
    private boolean lastB = false;

    private State state = State.EMPTY;

    /**
     * Runs the intake state machine. Expected to be run by the intake command continuously.
     */
    public void runIntake() {
        switch (state) {
            case EMPTY: // Empty State - Expected to be Motor stopped and 0 balls in tunnel
                if (sensorA.get() && !lastA) { // Transition to next state when Sensor A goes low->high
                    beltMotor.set(INTAKE_SPEED);
                    state = State.INTAKING_FIRST;
                }
                break;
            case INTAKING_FIRST: // First ball intake in progress -- Expected belt motor running
                if (!sensorA.get() && lastA) { // Transition to holding (intake complete) on A going high->low
                    beltMotor.set(0);
                    state = State.HOLDING_FIRST;
                }
                break;
            case HOLDING_FIRST: // First ball intake complete -- Expected belt motor stopped
                if (sensorA.get() && !lastA) { // Transition to start 2nd intake on A going low->high
                    state = State.INTAKING_SECOND;
                    beltMotor.set(INTAKE_SPEED);
                }
                break;
            case INTAKING_SECOND: // 2nd ball intake in progress - expecting belt motor on
                if (sensorB.get() && !lastB) { // Transition to full when 2nd sensor (B) goes low->high
                    beltMotor.set(0);
                    state = State.FULL;
                }
                break;
            case FULL:
                // Noop - Exit of this state by driver command
                break;
            case FIRING:
                // Noop - Entry and exit of this state by driver command
                break;
        }

        // Update "last" sensor values to current values to detect state changes
        lastA = sensorA.get();
        lastB = sensorB.get();
    }

    /**
     * Shoot - called continuously while desiring to fire.... Preempts intake command and updates state.
     * User expected to hold the command active until all balls exit.
     */
    public void shoot() {
        // Temp
        double targetVelocity = 5000;

        if (state == State.FULL || state == State.HOLDING_FIRST) {
            state = State.FIRING;
            // TODO: Vision Check - are we aligned?
            // TODO: Distance Check - Set correct velocity
            shooterMotor1.getPIDController().setReference(targetVelocity, CANSparkMax.ControlType.kVelocity);
            shooterMotor2.getPIDController().setReference(targetVelocity, CANSparkMax.ControlType.kVelocity);
        } else if (state == State.FIRING) {
            if (shooterMotor1.getEncoder().getVelocity() > (targetVelocity * .9)
                    && shooterMotor1.getEncoder().getVelocity() < (targetVelocity * 1.1)
                    && shooterMotor2.getEncoder().getVelocity() < (targetVelocity * 1.1)) {
                beltMotor.set(1);
            }
        } else {
            System.err.println("Invalid Shooter State Transition");
        }
    }

    /**
     * Called once to stop shooting and transition to Empty state
     */
    public void stopShooting() {
        beltMotor.set(0);
        shooterMotor1.stopMotor();
        shooterMotor2.stopMotor();
        state = State.EMPTY;
    }

    /**
     * Emergency helper to reset the state to empty in case of error
     */
    public void resetState() {
        stopShooting(); // Basically does it.
    }

    /**
     * Emergency helper to reset the state to Holding One Ball in case of error
     */
    public void resetStateOneHolding() {
        stopShooting();
        state = State.HOLDING_FIRST;
    }

    /**
     * Emergency helper to reset the state to Full in case of error
     */
    public void resetStateFull() {
        stopShooting();
        state = State.FULL;
    }


    @Override
    public void periodic() {


    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }


}
