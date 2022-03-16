package org.tvhsfrc.frc2022.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.PhotonCamera;
import org.tvhsfrc.frc2022.robot.Constants;

import java.util.HashMap;
import java.util.Map;

public class ShooterSubsystem extends SubsystemBase implements Sendable {
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
    //private final CANSparkMax beltMotor = new CANSparkMax(Constants.BELT_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final DigitalInput sensorA = new DigitalInput(Constants.INTAKE_SENSOR_A_CHANNEL);
    private final DigitalInput sensorB = new DigitalInput(Constants.INTAKE_SENSOR_B_CHANNEL);

    private final PhotonCamera shooterCamera = new PhotonCamera("shooter");

    private double p, i, d;

    enum SHOT_LOCATION {
        TARMAC_LINE,
        SAFE_POINT,
        HAIL_MARY
    }

    private final Map<SHOT_LOCATION, Double> shotVelocities;

    private final SendableChooser<SHOT_LOCATION> shotLocationSendableChooser = new SendableChooser<>();

    public ShooterSubsystem() {
        p = 0.005;
        i = 0.0;
        d = 0.02;

        shooterMotor1.getPIDController().setP(p);
        shooterMotor1.getPIDController().setI(i);
        shooterMotor1.getPIDController().setD(d);
        shooterMotor1.setIdleMode(CANSparkMax.IdleMode.kCoast);

        shooterMotor2.setInverted(true);
        shooterMotor2.getPIDController().setP(p);
        shooterMotor2.getPIDController().setI(i);
        shooterMotor2.getPIDController().setD(d);
        shooterMotor2.setIdleMode(CANSparkMax.IdleMode.kCoast);

        //beltMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);

        shotVelocities = new HashMap<>();
        shotVelocities.put(SHOT_LOCATION.TARMAC_LINE, 3500.0);
        shotVelocities.put(SHOT_LOCATION.SAFE_POINT, 4500.0);
        shotVelocities.put(SHOT_LOCATION.HAIL_MARY, 5500.0);

        shotLocationSendableChooser.addOption("Tarmac", SHOT_LOCATION.TARMAC_LINE);
        shotLocationSendableChooser.setDefaultOption("Safe Point", SHOT_LOCATION.SAFE_POINT);
        shotLocationSendableChooser.addOption("Hail Mary", SHOT_LOCATION.HAIL_MARY);

        SmartDashboard.putData(shotLocationSendableChooser);
    }

    private double intakeSpeed = 0.1;

    private boolean lastA = false;
    private boolean lastB = false;

    private State state = State.EMPTY;

    /**
     * Runs the intake state machine. Expected to be run by the intake command continuously.
     */
    public void runIntake() {
        //beltMotor.set(intakeSpeed);

        /*
        switch (state) {
            case EMPTY: // Empty State - Expected to be Motor stopped and 0 balls in tunnel
                if (sensorA.get() && !lastA) { // Transition to next state when Sensor A goes low->high
                    beltMotor.set(intakeSpeed);
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
                    beltMotor.set(intakeSpeed);
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

         */
    }

    /**
     * Shoot - called continuously while desiring to fire.... Preempts intake command and updates state.
     * User expected to hold the command active until all balls exit.
     */
    public void shoot() {
        /*
        if (state.equals(State.EMPTY)) { state = State.FULL; }

        if (state == State.FULL || state == State.HOLDING_FIRST) {
            state = State.FIRING;

            // TODO: Vision Check - are we aligned?
            // Query the latest result from PhotonVision
            var result = camera.getLatestResult();



            // TODO: Distance Check - Set correct velocity
            shooterMotor1.getPIDController().setReference(getShotVelocity(), CANSparkMax.ControlType.kVelocity);
            shooterMotor2.getPIDController().setReference(getShotVelocity(), CANSparkMax.ControlType.kVelocity);
        } else if (state == State.FIRING) {
            if (shooterMotor1.getEncoder().getVelocity() > (getShotVelocity() * .9)
                    && shooterMotor1.getEncoder().getVelocity() < (getShotVelocity() * 1.1)
                    && shooterMotor2.getEncoder().getVelocity() < (getShotVelocity() * 1.1)) {
                beltMotor.set(1);
            }
        } else {
            System.err.println("Invalid Shooter State Transition");
        }
        */

        shooterMotor1.getPIDController().setReference(getShotVelocity(), CANSparkMax.ControlType.kVelocity);
        shooterMotor2.getPIDController().setReference(getShotVelocity(), CANSparkMax.ControlType.kVelocity);

        if (shooterMotor1.getEncoder().getVelocity() > (getShotVelocity() * .95)
                && shooterMotor2.getEncoder().getVelocity() > (getShooter1Velocity() * .95)
                && shooterMotor1.getEncoder().getVelocity() < (getShotVelocity() * 1.1)
                && shooterMotor2.getEncoder().getVelocity() < (getShotVelocity() * 1.1)) {
            //beltMotor.set(1);
        }
    }

    public void shootPercentage(double value) {
        shooterMotor1.set(value);
        shooterMotor2.set(value);
    }

    public boolean isVisionValid() {
        return shooterCamera.getLatestResult().hasTargets(); // TODO: Yeah... this is uh... ugh.
    }

    public double getVisionCenterOffset() {
        if (!isVisionValid()) {
            return 0;
        }

        return shooterCamera.getLatestResult().getBestTarget().getYaw();
    }

    /**
     * Called once to stop shooting and transition to Empty state
     */


    public void stopShooting() {
        //beltMotor.set(0);
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

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getI() {
        return i;
    }

    public void setI(double i) {
        this.i = i;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getIntakeSpeed() {
        return intakeSpeed;
    }

    public void setIntakeSpeed(double intakeSpeed) {
        this.intakeSpeed = intakeSpeed;
    }

    public State getState() {
        return state;
    }

    public String getStateString() {
        return getState().toString();
    }

    public SHOT_LOCATION getCurrentShotLocation() {
        return shotLocationSendableChooser.getSelected();
    }

    public boolean getSensorA() {
        return !sensorA.get();
    }

    public boolean getSensorB() {
        return !sensorB.get();
    }

    @Override
    public void periodic() {
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }

    public double getShotVelocity() {
        return getShotVelocity(shotLocationSendableChooser.getSelected());
    }

    public double getShotVelocity(SHOT_LOCATION shot) {
        return shotVelocities.get(shot);
    }

    public void setShotVelocity(SHOT_LOCATION shot, double velocity) {
        shotVelocities.put(shot, velocity);
    }

    public double getShooter1Velocity() {
        return shooterMotor1.getEncoder().getVelocity();
    }

    public double getShooter2Velocity() {
        return shooterMotor2.getEncoder().getVelocity();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.setSmartDashboardType("Intake");
        builder.addStringProperty("CurrentShotLoc", () -> getCurrentShotLocation().toString(), null);
        builder.addDoubleProperty("IntakeSpeed", this::getIntakeSpeed, this::setIntakeSpeed);
        builder.addDoubleProperty("PID-p", this::getP, this::setP);
        builder.addDoubleProperty("PID-i", this::getI, this::setI);
        builder.addDoubleProperty("PID-d", this::getD, this::setD);
        builder.addBooleanProperty("VisionValid", this::isVisionValid, null);
        builder.addDoubleProperty("VisionOffset", this::getVisionCenterOffset, null);
        builder.addStringProperty("IntakeState", this::getStateString, null);
        builder.addBooleanProperty("SensorA", this::getSensorA, null);
        builder.addBooleanProperty("SensorB", this::getSensorB, null);
        builder.addDoubleProperty("Shooter1Vel", this::getShooter1Velocity, null);
        builder.addDoubleProperty("Shooter2Vel", this::getShooter2Velocity, null);
        builder.addDoubleProperty("ShotVel", this::getShotVelocity, null);
        builder.addDoubleProperty(SHOT_LOCATION.TARMAC_LINE.toString(), () -> getShotVelocity(SHOT_LOCATION.TARMAC_LINE), value -> setShotVelocity(SHOT_LOCATION.TARMAC_LINE, value));
        builder.addDoubleProperty(SHOT_LOCATION.SAFE_POINT.toString(), () -> getShotVelocity(SHOT_LOCATION.SAFE_POINT), value -> setShotVelocity(SHOT_LOCATION.SAFE_POINT, value));
        builder.addDoubleProperty(SHOT_LOCATION.HAIL_MARY.toString(), () -> getShotVelocity(SHOT_LOCATION.HAIL_MARY), value -> setShotVelocity(SHOT_LOCATION.HAIL_MARY, value));
    }
}
