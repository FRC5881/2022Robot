package org.tvhsfrc.frc2022.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.swervelib.SwerveInput;

public class XboxController5881 extends XboxController {
    private double deadband = 0.1;

    public XboxController5881(int port) {
        super(port);
    }

    public XboxController5881(int port, double deadband) {
        super(port);
        this.deadband = deadband;
    }

    public SwerveInput getSwerveInput() {
        return new SwerveInput(modifyAxis(applyDeadband(-getLeftY())), modifyAxis(applyDeadband(-getLeftX())),
                modifyAxis(applyDeadband(-getRightX())));
    }

    /**
     * Squares the input to smooth values
     * @param value input value
     * @return squared output
     */
    private static double modifyAxis(double value) {
        // Square the axis
        value = Math.copySign(value * value, value);

        return value;
    }

    /**
     * Applies the deadband value -- inputs below the deadband threshold are returned as 0.0
     * @param value input value to apply deadband to
     * @return value or 0.0 if abs(val) < deadband
     */
    private double applyDeadband(double value) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }
}
