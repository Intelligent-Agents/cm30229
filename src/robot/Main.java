package robot;

import behaviours.HitWallLeft;
import behaviours.HitWallRight;
import behaviours.DriveForward;
import behaviours.ExploreWallEnd;
import behaviours.FollowWall;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {

	public static void main(String[] args) {
		LCD.drawString("Press any button to start...", 0, 1);
		Button.waitForAnyPress();
		
		// create robot and set speeds
		Robot robot = new Robot();
		robot.getPilot().setTravelSpeed(15);
		robot.getPilot().setRotateSpeed(120);

		// create behaviours
		Behavior b1 = new DriveForward(robot);
		Behavior b2 = new FollowWall(robot);
		Behavior b3 = new ExploreWallEnd(robot);
		Behavior b4 = new HitWallRight(robot);
		Behavior b5 = new HitWallLeft(robot);
		
		// create and start arbitrator
		Behavior [] bArray = { b1, b2, b3, b4, b5 };
	 	Arbitrator arb = new Arbitrator(bArray);

	 	arb.start();
	}

}