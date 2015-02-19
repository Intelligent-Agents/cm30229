package robot;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot {
	// set distances
	public static final int CLOSE_DISTANCE = 20;
	public static final int TRAVEL_DIST = CLOSE_DISTANCE / 4;
	
	// create sensors
	private static final TouchSensor leftTouch = new TouchSensor(SensorPort.S4);
	private static final TouchSensor rightTouch = new TouchSensor(SensorPort.S1);
	private static final UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S2);
	private static final DifferentialPilot p = new DifferentialPilot(5.6, 11.8, Motor.C, Motor.A);

	public Robot() {}

	/**
	 * @return The robot's left touch sensor.
	 */
	public TouchSensor getLeftTouchSensor() {
		return leftTouch;
	}
	
	/**
	 * @return The robot's right touch sensor.
	 */
	public TouchSensor getRightTouchSensor() {
		return rightTouch;
	}
	
	/**
	 * @return The robot's ultrasonic sensor.
	 */
	public UltrasonicSensor getUltrasonicSensor() {
		return sonic;
	}

	/**
	 * @return The robot's driving pilot.
	 */
	public DifferentialPilot getPilot() {
		return p;
	}
}
