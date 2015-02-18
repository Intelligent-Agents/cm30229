package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class TurnToWall implements Behavior {
	private boolean suppressed;
	private Robot robot;
	
	public TurnToWall(Robot r) {
		robot = r;
	}

	@Override
	public boolean takeControl() {
		int dist = robot.getUltrasonicSensor().getDistance();
		
		if(dist > Robot.CLOSE_DISTANCE * 4)
			robot.setAligned(false);
		
		return dist > (Robot.CLOSE_DISTANCE * 2.5) && robot.isAligned();
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Turning To Wall", 0, 1);
		
		suppressed = false;
		robot.setAligned(false);
		
		int distance = 0, variance = 0;		
		while(!suppressed) {
			distance = robot.getUltrasonicSensor().getDistance();
			
			robot.getPilot().arc(20, 4 * Robot.TRAVEL_DIST, false);
			
			variance = distance - robot.getUltrasonicSensor().getDistance();
			
			if(variance > 0) {
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
