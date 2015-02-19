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
		boolean pressed = robot.getLeftTouchSensor().isPressed();
		
		return pressed;
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Left Bumper Hit", 0, 1);

		suppressed = false;
		
		robot.getPilot().travel(-5);		
		robot.getPilot().rotate(-90);
		
		if(robot.getPilot().isMoving() && !suppressed)
			robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
