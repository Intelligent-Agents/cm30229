package robot;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot {
	public static final int CLOSE_DISTANCE = 20;
	public static final int TRAVEL_DIST = CLOSE_DISTANCE / 4;
	
	private static final TouchSensor leftTouch = new TouchSensor(SensorPort.S4);
	private static final TouchSensor rightTouch = new TouchSensor(SensorPort.S1);
	private static final UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S2);
	private static final DifferentialPilot p = new DifferentialPilot(5.6, 11.8, Motor.C, Motor.A);

	public Robot() {}

	public TouchSensor getLeftTouchSensor() {
		return leftTouch;
	}
	
	public TouchSensor getRightTouchSensor() {
		return rightTouch;
	}
	
	public UltrasonicSensor getUltrasonicSensor() {
		return sonic;
	}

	public DifferentialPilot getPilot() {
		return p;
	}
}
