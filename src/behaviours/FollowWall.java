package behaviours;

import robot.Robot;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class FollowWall implements Behavior {
	private static final int OUT_OF_RANGE = 255;

    private int[] distances = {0,0,0,0};
    private int startIndex = 0;
    
    private int lastAverage = 0;
    private int newAverage = 0;

    private boolean suppressed = false;
    private boolean rotating = false;

    private int totalAngle = 0;
    
    private Robot robot;

    public FollowWall(Robot robot) {
    	this.robot = robot;
    }

	@Override
	public boolean takeControl() {
		return robot.getUltrasonicSensor().getDistance() < (Robot.CLOSE_DISTANCE * 2);
	}

	@Override
	public void action() {
		LCD.clear();
		LCD.drawString("Following Wall", 0, 1);
		
		suppressed = false;
		
		if(rotating) {
            return;
		}

		int d = robot.getUltrasonicSensor().getDistance();

		if(d == 0 || d >= OUT_OF_RANGE)
			return;
		
		robot.getPilot().travel(10, true);
        
        // store distance
        distances[startIndex] = d;

		// check we have at least 3 readings
		if(startIndex < 3) {
			startIndex++;
			return;
		}

		if(suppressed)
			return;
		
		newAverage = (distances[startIndex - 1] + distances[startIndex - 2] + distances[startIndex - 3]) / 3;
		
		/*// take median of last three readings
		if(distances[startIndex - 1] >=  distances[startIndex - 2]) {
		        
			if(distances[startIndex - 1] >=  distances[startIndex - 3]) {
				//Found Max
				newAverage = Math.max(distances[startIndex - 2], distances[startIndex - 3]);
			} else {
				newAverage = distances[startIndex - 1];
			}
		} else {
			if(distances[startIndex - 1] >=  distances[startIndex - 3]) {
				newAverage = distances[startIndex - 1];
			} else {
				newAverage = Math.max(distances[startIndex - 2], distances[startIndex - 3]);
			}
		        
		}*/
		
        startIndex++;

		// loop index
		if(startIndex >= 4) {
			startIndex = 0;
		}

		// check whether this is the first time we've taken an average
		if(lastAverage == 0) {
			lastAverage = newAverage;
			return;
		}

		if(suppressed) {
			return;
		}

        if(newAverage - lastAverage >= 10) {
                totalAngle = 20;
        } else if(newAverage - lastAverage >= 5) {
                totalAngle = 15;
        } else if(newAverage - lastAverage >= 3) {
                totalAngle = 10;
        } else if(newAverage - lastAverage == 2) {
                totalAngle = 10;
        } else if(newAverage - lastAverage == 1) {
                totalAngle = 5;
        } else if(newAverage - lastAverage <= -10) {
                totalAngle = -20;
        } else if(newAverage - lastAverage <= -5) {
                totalAngle = -15;
        } else if(newAverage - lastAverage <= -3) {
                totalAngle = -10;
        } else if(newAverage - lastAverage == -2) {
                totalAngle = -10;
        } else if(newAverage - lastAverage == -1) {
                totalAngle = -5;
        } else {
                return;
        }
        
        LCD.drawString("Last Avg = " + Integer.toString(lastAverage), 0, 2);
        LCD.drawString("New Avg = " + Integer.toString(newAverage), 0, 3);

        if(totalAngle != 0)
        	robot.getPilot().rotate(totalAngle);
        
        rotating = true;

        robot.getPilot().forward();
        
        d = robot.getUltrasonicSensor().getDistance();
        while(Math.abs(newAverage - d) < 3 && d < Robot.CLOSE_DISTANCE * 2 && !suppressed) {
        	Thread.yield();
        	d = robot.getUltrasonicSensor().getDistance();
        }
        
        resetValues();
        robot.getPilot().stop();
	}

	@Override
	public void suppress() {
		resetValues();
    
		robot.getPilot().stop();
		suppressed = true;
	}
	
	private void resetValues() {
		for(int i=0; i<4; i++) {
			distances[i] = 0;
		}
		startIndex = 0;
		lastAverage = 0;
		rotating = false;
	}

}
