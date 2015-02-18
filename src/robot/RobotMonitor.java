package robot;
import java.lang.Thread;
import lejos.nxt.*;

public class RobotMonitor extends Thread {
	private static int delay;
	private static Robot robot;

	public RobotMonitor(Robot r, int d) {
		this.setDaemon(true); 			
		robot = r;
		delay = d;
	}
	
	public void run() {
		while(true) {
			LCD.clear();
			
			LCD.drawString("Touch L = "+ robot.getLeftTouchSensor().isPressed(), 0, 2);
			LCD.drawString("Touch R = " + robot.getRightTouchSensor().isPressed(), 0, 3);
			LCD.drawString("Distance = " + Integer.toString(robot.getUltrasonicSensor().getDistance()), 0, 4);
			
			try { this.sleep(delay); } catch (Exception e) { ; }
		} // end while
	} // end run
} // end class