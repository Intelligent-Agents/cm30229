package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class HitWallRight implements Behavior {
	boolean suppressed;
	Robot robot;
	
	public HitWallRight(Robot r) {
		robot = r;
		suppressed = false;
	}

	@Override
	public boolean takeControl() {
		boolean pressed = robot.getRightTouchSensor().isPressed();

		return pressed;
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Right Bumper Hit", 0, 1);
		
		suppressed = false;
		
		// back off from the object
		robot.getPilot().travel(-1 * Robot.TRAVEL_DIST);
		
		// if robot is far enough from an object, turn 90 degrees to the left
		if(robot.getUltrasonicSensor().getDistance() >= Robot.CLOSE_DISTANCE * 2) {
			LCD.drawString("Turning Left", 0, 2);
			robot.getPilot().rotate(90);
		// else turn 90 degrees to the right
		} else {
			LCD.drawString("Turning Right", 0, 2);
			robot.getPilot().rotate(-90);
		}
		
		if(robot.getPilot().isMoving() && !suppressed)
			robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
