package org.tvhsfrc.frc2022.robot.sds;

public interface DriveController {
    void setReferenceVoltage(double voltage);

    double getStateVelocity();
}
