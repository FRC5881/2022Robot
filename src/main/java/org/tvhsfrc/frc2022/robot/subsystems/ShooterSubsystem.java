package org.tvhsfrc.frc2022.robot.subsystems;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {

    private final CANSparkMax shooterMotor1 = new CANSparkMax(30, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax shooterMotor2 = new CANSparkMax(31, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax beltMotor = new CANSparkMax(32, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final DigitalInput sensorA = new DigitalInput(60);
    private final DigitalInput sensorB = new DigitalInput(61);

    public ShooterSubsystem() {
        shooterMotor1.getPIDController().setFF(1);
        shooterMotor1.getPIDController().setI(0.001);
        shooterMotor1.getPIDController().setD(0.01);

        shooterMotor2.getPIDController().setFF(1);
        shooterMotor2.getPIDController().setI(0.001);
        shooterMotor2.getPIDController().setD(0.01);
    }

    int count = 0;

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        //if button pressed, shoot. Shooting entails spinning up, move ball over, kick
        //shoot both balls


        //storage
        //One ball stored
        if (sensorA.get()) {
            count++;
            while(sensorA.get()){
                beltMotor.set(0.1);
            }
        }

        //Two balls stored
        if(count == 2) {
            while (!sensorB.get()) {
                beltMotor.set(0.1);
            }
        }



        //shoot ball, count minus 1, do twice,
        boolean shoot = true; //temporary

        if(shoot) {
            shooterMotor1.getPIDController().setReference(5000, CANSparkMax.ControlType.kVelocity);
            shooterMotor2.getPIDController().setReference(-5000, CANSparkMax.ControlType.kVelocity);

            while (sensorB.get()) {
                beltMotor.set(1);
            }

            if(!sensorB.get()) {
                beltMotor.set(1);
                //wait(1000L);
                count--;
            }

            if(count == 0) {

            }




        }








    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }



}
