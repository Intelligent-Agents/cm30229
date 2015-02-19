package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class DriveForward implements Behavior {
	private boolean suppressed;
	private Robot robot;
	
	public DriveForward(Robot r) {
		robot = r;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Driving Forward", 0, 1);
		
		suppressed = false;
		
		// drive forward until a higher priority behaviour requests attention
		robot.getPilot().forward();

		while( !suppressed )
			Thread.yield();

		robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
