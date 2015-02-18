package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class AlignWithWall implements Behavior {
	public static int thisDistance, lastDistance;
	public static boolean justAvoidedWall;
	
	private boolean suppressed;
	private Robot robot;
	
	public AlignWithWall(Robot r) {
		robot = r;
		thisDistance = 255;
		lastDistance = 255;
		justAvoidedWall = false;
	}

	@Override
	public boolean takeControl() {
		int dist = robot.getUltrasonicSensor().getDistance();
		
		lastDistance = thisDistance;
		thisDistance = dist;
		
		if(justAvoidedWall) {
			justAvoidedWall = false;
			return true;
		}
		
		return dist > Robot.CLOSE_DISTANCE && dist < (Robot.CLOSE_DISTANCE * 2) && !robot.isAligned();
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Aligning Wall", 0, 1);
		
		suppressed = false;
		robot.setAligned(false);
		
		boolean wasMovingAwayFromWall = lastDistance < thisDistance;
		
		int newDist = 0, variance = 0;		
		while(!suppressed) {
			newDist = robot.getUltrasonicSensor().getDistance();
			
			if(wasMovingAwayFromWall) {
				LCD.drawString("Moving From Wall", 0, 2);
				robot.getPilot().arc(20, 4 * Robot.TRAVEL_DIST, false);
			} else {
				LCD.drawString("Moving To Wall", 0, 2);
				robot.getPilot().arc(-20, -4 * Robot.TRAVEL_DIST, false);
			}
			
			variance = newDist - robot.getUltrasonicSensor().getDistance();
			
			if(wasMovingAwayFromWall) {
				if(variance > 0) {
					robot.getPilot().rotate(-2 * Robot.TRAVEL_DIST);
					robot.setAligned(true);
					break;
				}
			} else {
				if(variance < 0) {
					robot.getPilot().rotate(2 * Robot.TRAVEL_DIST);
					robot.setAligned(true);
					break;
				}
			}
			/// Include else if here for when closer to the wall ?
		}
		
		robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
