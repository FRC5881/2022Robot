package org.tvhsfrc.frc2022.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.tvhsfrc.frc2022.robot.Constants;

public class BeltSubsystem extends SubsystemBase {
    private final CANSparkMax beltMotor = new CANSparkMax(Constants.BELT_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

    public BeltSubsystem() {
        beltMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
    }

    public void run(double value) {
        beltMotor.set(value);
    }

    public void stop() {
        beltMotor.stopMotor();
    }
}
