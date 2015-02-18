package robot;

import behaviours.AvoidWall;
import behaviours.HitWallLeft;
import behaviours.HitWallRight;
import behaviours.DriveForward;
import behaviours.ExploreWallEnd;
import behaviours.AlignWithWall;
import behaviours.TurnToWall;
import behaviours.TurnFromWall;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {

	public static void main(String[] args) {
		LCD.drawString("Press any button to start...", 0, 1);
		Button.waitForAnyPress();
		
		Robot robot = new Robot();
		robot.getPilot().setTravelSpeed(15);
		robot.getPilot().setRotateSpeed(120);

		Behavior b1 = new DriveForward(robot);
		Behavior b2 = new HitWallRight(robot);
		Behavior b3 = new HitWallLeft(robot);
		Behavior b4 = new ExploreWallEnd(robot);
		Behavior b5 = new AlignWithWall(robot);
		Behavior b6 = new TurnToWall(robot);
		Behavior b7 = new TurnFromWall(robot);
		Behavior b8 = new AvoidWall(robot);
		
		Behavior [] bArray = { b1, b6, b7, b5, b4, b8, b2, b3 };
	 	Arbitrator arb = new Arbitrator(bArray);

	 	arb.start();
	}

}