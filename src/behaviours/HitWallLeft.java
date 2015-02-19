package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class HitWallLeft implements Behavior {
	boolean suppressed;
	Robot robot;
	
	public HitWallLeft(Robot r) {
		robot = r;
		suppressed = false;
	}

	@Override
	public boolean takeControl() {
		// return true if the left sensor is being pressed
		return robot.getLeftTouchSensor().isPressed();
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Left Bumper Hit", 0, 1);

		suppressed = false;
		
		// reverse and rotate to the right by 90 degrees
		robot.getPilot().travel(-1 * Robot.TRAVEL_DIST);		
		robot.getPilot().rotate(-90);
		
		if(robot.getPilot().isMoving() && !suppressed)
			robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
