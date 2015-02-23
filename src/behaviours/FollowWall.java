package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;

public class FollowWall implements Behavior {

    private int[] distances = {0,0,0,0};
    private int distanceIndex = 0;
    
    private int lastAvg = 0;
    private int newAvg = 0;

    private boolean suppressed = false;
    private boolean rotating = false;
    
    private Robot robot;

    public FollowWall(Robot robot) {
    	this.robot = robot;
    }

	@Override
	public boolean takeControl() {
		// return true if we are close to a wall
		return robot.getUltrasonicSensor().getDistance() < (Robot.CLOSE_DISTANCE * 3);
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Following Wall", 0, 1);
		
		suppressed = false;
		
		// check we're not in the process of turning towards or away from a wall
		if(rotating) {
			resetValues();
            return;
		}

		int d = robot.getUltrasonicSensor().getDistance();

		// check that the distance is within reasonable bounds
		if(d == 0 || d >= Robot.CLOSE_DISTANCE * 5)
			return;
		
		// move forward, before calculating distance again
		robot.getPilot().travel(10, true);
        
        // store distance
        distances[distanceIndex] = d;

		// make sure we have at least 3 readings
		if(distanceIndex < 3) {
			distanceIndex++;
			return;
		}

		if(suppressed) {
			resetValues();
			return;
		}
		
		// calculate the mean of the previous three distances
		newAvg = (distances[distanceIndex - 1] + distances[distanceIndex - 2] + distances[distanceIndex - 3]) / 3;
		
        distanceIndex++;

		// loop index
		if(distanceIndex >= 4) {
			distanceIndex = 0;
		}

		// check whether this is the first time we've taken an average
		if(lastAvg == 0) {
			lastAvg = newAvg;
			return;
		}

		if(suppressed) {
			resetValues();
			return;
		}

		
		int diff = newAvg - lastAvg;
		int turnAngle = 0;
		
		// set the turning angle according to the difference between the last two calculated averages
		if(diff > 0 && newAvg > Robot.CLOSE_DISTANCE) {
			// difference is positive, so robot will turn towards the wall
			if(diff >= 10) {
				turnAngle = 20;
			} else if(diff >= 5) {
				turnAngle = 17;
			} else if(diff >= 3) {
				turnAngle = 15;
			} else if(diff == 2) {
				turnAngle = 10;
			} else if(diff == 1) {
				turnAngle = 5;
			}
		} else if(diff < 0) {
			// negative, so the robot will turn away from the wall
			if(diff <= -10) {
				turnAngle = -20;
			} else if(diff <= -5) {
				turnAngle = -17;
			} else if(diff <= -3) {
				turnAngle = -15;
			} else if(diff == -2) {
				turnAngle = -10;
			} else if(diff == -1) {
				turnAngle = -5;
			}
		} else {
			return;
		}

		rotating = true;

		// turn the robot
		if(turnAngle != 0)
			robot.getPilot().rotate(turnAngle);

		// continue driving forward until our distance from the wall changes
		robot.getPilot().forward();

		d = robot.getUltrasonicSensor().getDistance();
		while(Math.abs(newAvg - d) < d / 10 && d < Robot.CLOSE_DISTANCE * 2 && !suppressed) {
			Thread.yield();
			d = robot.getUltrasonicSensor().getDistance();
		}

		resetValues();
		robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
	
	/**
	 * Resets all distances and averages used by this behaviour.
	 */
	private void resetValues() {
		for(int i=0; i<4; i++) {
			distances[i] = 0;
		}
		distanceIndex = 0;
		lastAvg = 0;
		newAvg = 0;
		rotating = false;
	}

}
