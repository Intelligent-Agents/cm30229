import lejos.nxt.Button;
import lejos.nxt.Motor;


public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		Button.waitForAnyPress();
		
		while(true) {
			Motor.A.forward();
			Motor.B.forward();
			Motor.C.forward();
		}
	}

}