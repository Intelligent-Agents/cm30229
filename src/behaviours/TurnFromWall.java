package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class TurnFromWall implements Behavior {
	private boolean suppressed;
	private Robot robot;
	
	public TurnFromWall(Robot r) {
		robot = r;
	}

	@Override
	public boolean takeControl() {
		int dist = robot.getUltrasonicSensor().getDistance();
		
		return dist < (Robot.CLOSE_DISTANCE * 1.5) && robot.isAligned();
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Turning From Wall", 0, 1);
		
		suppressed = false;
		robot.setAligned(false);
		
		int distance = 0, variance = 0;		
		while(!suppressed) {
			distance = robot.getUltrasonicSensor().getDistance();
			
			robot.getPilot().arc(-20, -2 * Robot.TRAVEL_DIST, false);
			
			variance = distance - robot.getUltrasonicSensor().getDistance();
			
			if(variance < 0) {
				robot.setAligned(true);
				break;
			}
		}
		
		robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
