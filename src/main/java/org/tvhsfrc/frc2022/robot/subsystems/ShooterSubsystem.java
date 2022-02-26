package org.tvhsfrc.frc2022.robot.subsystems;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

enum State {
    Empty(),
    IntakingFirst(),
    HoldingFirst(),
    IntakingSecond(),
    Full(),
    Firing()
}

public class ShooterSubsystem extends SubsystemBase {

    private final CANSparkMax shooterMotor1 = new CANSparkMax(30, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax shooterMotor2 = new CANSparkMax(31, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax beltMotor = new CANSparkMax(32, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final DigitalInput sensorA = new DigitalInput(60);
    private final DigitalInput sensorB = new DigitalInput(61);

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

    private static double INTAKE_SPEED = 0.1;

    private int cargoCount = 0;

    private boolean lastA = false;
    private boolean lastB = false;

    State state = State.Empty;

    public void SomeReallyLongNameJustUsedAsAPlaceHolderUntilIFindABetterOne() {
        switch (state) {
            case Empty:
                if (sensorA.get() && !lastA) {
                    beltMotor.set(INTAKE_SPEED);
                    state = State.IntakingFirst;
                }
                break;

            case IntakingFirst:
                //A: low to high
                if (!sensorA.get() && lastA) {
                    beltMotor.set(0);
                    state = State.HoldingFirst;
                }
                break;

            case HoldingFirst:
                //A: high to low
                //can either fire or load second
                cargoCount++;
                if(sensorA.get() && !lastA) {
                    state = State.IntakingSecond;
                    beltMotor.set(INTAKE_SPEED);
                }
                break;

            case IntakingSecond:
                //A: low to high
                if(sensorB.get() && !lastB) {
                    beltMotor.set(0);
                    state = State.Full;
                }
                break;

            case Full:
                // Noop - Exit of this state by driver command
                break;

            case Firing:
                // Noop - Entry and exit of this state by driver command
                break;
        }
        lastA = sensorA.get();
        lastB = sensorB.get();


    }

    /**
     * Shoot - called continuously while desiring to fire....
     */
    public void shoot() {
        // Temp
        double targetVelocity = 5000;

        if (state == State.Full || state == State.HoldingFirst) {
            state = State.Firing;
            // TODO: Vision Check - are we aligned?
            // TODO: Distance Check - Set correct velocity
            shooterMotor1.getPIDController().setReference(targetVelocity, CANSparkMax.ControlType.kVelocity);
            shooterMotor2.getPIDController().setReference(targetVelocity, CANSparkMax.ControlType.kVelocity);
        } else if (state == State.Firing) {
            if (shooterMotor1.getEncoder().getVelocity() > (targetVelocity*.9)
                    && shooterMotor1.getEncoder().getVelocity() < (targetVelocity*1.1)
                    && shooterMotor2.getEncoder().getVelocity() < (targetVelocity*1.1)) {
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
        state = State.Empty;
        cargoCount = 0;
    }





    @Override
    public void periodic() {


    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }



}
