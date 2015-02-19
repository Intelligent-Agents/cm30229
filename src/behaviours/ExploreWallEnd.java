package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class ExploreWallEnd implements Behavior {
	boolean suppressed;
	int lastDistance;
	Robot robot;

	public ExploreWallEnd(Robot r) {
		robot = r;
		suppressed = false;
		lastDistance = 255;
	}

	@Override
	public boolean takeControl() {
		int dist = robot.getUltrasonicSensor().getDistance();
		boolean ret = lastDistance < Robot.CLOSE_DISTANCE * 2
					  && dist > Robot.CLOSE_DISTANCE * 5;
		
		lastDistance = dist;
		
		return ret;
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Exploring", 0, 1);
		
		suppressed = false;

		robot.getPilot().arc(20, 180, true);
		
		
		
		while(robot.getUltrasonicSensor().getDistance() > Robot.CLOSE_DISTANCE * 2 && !suppressed)
			Thread.yield();
		
		robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
