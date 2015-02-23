package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class ExploreWallEnd implements Behavior {
	boolean suppressed;
	int lastDistance, endOfWallDistance;
	boolean endOfWall, isTurning;
	Robot robot;

	public ExploreWallEnd(Robot r) {
		robot = r;
		suppressed = false;
		endOfWall = false;
		isTurning = false;
		lastDistance = 255;
		endOfWallDistance = 255;
	}

	@Override
	public boolean takeControl() {
		int dist = robot.getUltrasonicSensor().getDistance();
		
		// check whether we have reached the end of a wall
		boolean ret = !isTurning && lastDistance < 3 * Robot.CLOSE_DISTANCE &&
				dist - lastDistance > Robot.CLOSE_DISTANCE;
		
		// we have reached the end of a wall, so save these values until we can call action()
	 	if(ret) {
			endOfWall = true;
			endOfWallDistance = lastDistance;
		}
					  
		lastDistance = dist;
		
		// this will continue to return true until action() is called
		return endOfWall;
	}

	@Override
	public void action() {
		endOfWall = false;
		
		LCD.clear();
		LCD.drawString("Exploring", 0, 1);
		
		suppressed = false;
		
		isTurning = true;

		// arc around end of wall
		robot.getPilot().arcForward(endOfWallDistance);
		
		// continue following arc until we find the adjoining wall, or we arc 180 degrees
		int d = robot.getUltrasonicSensor().getDistance();
		float a = robot.getPilot().getAngleIncrement();
		while(d > endOfWallDistance - 2 && a < 180 && !suppressed) {
			Thread.yield();
			
			d = robot.getUltrasonicSensor().getDistance();
			a = robot.getPilot().getAngleIncrement();
		}
		
		endOfWallDistance = 0;
		isTurning = false;

		robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		endOfWall = false;
		endOfWallDistance = 0;
		isTurning = false;
		suppressed = true;
	}

}
