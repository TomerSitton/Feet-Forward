
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FeedForward {

	private double maxV, maxA, maxD, setpoint;
	private final double minV = 1;
	/*
	 * the algorithm work by breaking the movement to 3 parts *
	 */
	double dt1, dt2, dt3;

	/**
	 * 
	 * @param p
	 *            - kp
	 * @param maxV
	 *            max velocity of subsystem
	 * @param maxA
	 *            max acceleration of subsystem
	 * @param maxD
	 *            max deceleration of subsystem
	 * @param setpoint
	 *            the distance to go
	 */
	public FeedForward(double p, double maxV, double maxA, double maxD, double setpoint) {
		this.maxA = maxA;
		this.maxD = maxD;
		this.maxV = maxV;
		this.setpoint = setpoint;
		dt1 = (maxV - minV) / maxA;
		dt2 = (((maxD - maxA) * (Math.pow(maxV, 2) - Math.pow(minV, 2)) - 2 * setpoint * maxA * maxD)
				/ (-2 * maxV * maxA * maxD));
		dt3 = (minV - maxV) / maxD;
		if (dt2 < 0) {
			dt2 = 0;
			double v = Math.sqrt((2 * maxA * maxD) / (maxD - maxA) + maxV * maxV);
			SmartDashboard.putNumber("v", v);
			dt3 = (v - minV) / (-maxD);
			dt1 = (v - minV) / maxA;
			this.maxV = v;
		}
		SmartDashboard.putString("times", "" + dt1 + "," + dt2 + "," + dt3);
	}

	/**
	 * 
	 * @param velocity
	 * @param acceleration
	 * @param location
	 * @param expected
	 * @return value for speed controller
	 */
	// TODO: check return type
	public double getVoltage(double velocity, double acceleration, double expected, double location) {
		return (Constants.DRIVETRAIN.VOLTAGE_VELOCITY_PARAMETER * (velocity + minV)
		/*
		 * + Constants.DRIVETRAIN.VOLTAGE_ACCELERATION_PARAMETER * acceleration
		 */);
	}

	public double getTotalTime() {
		return dt1 + dt2 + dt3;
	}

	/**
	 * 
	 * @param t=time[sec]
	 * @return [expectedLoaction, velocity, acceleration]
	 */
	public double[] getExpected(double t) {
		if (t <= dt1) {
			return new double[] { 0.5 * t * (2 * minV + maxA * t), minV + maxA * t, maxA };
		} else if (t <= (dt1 + dt2)) {
			return new double[] { 0.5 * dt1 * (minV + maxV) + maxV * (t - dt1), maxV, 0 };
		} /* else if (t <= getTotalTime()) { */
		// ask no questions, you will hear no lies
		return new double[] {
				0.5 * dt1 * (minV + maxV) + maxV * (dt2 - dt1)
						+ (2 * maxV + maxD * (t - dt1 - dt2)) * (t - dt1 - dt2) / 2,
				maxV + maxD * (t - dt1 - dt2), maxD };
		// }
		// return new double[] { setpoint, 0, 0 };
	}
}