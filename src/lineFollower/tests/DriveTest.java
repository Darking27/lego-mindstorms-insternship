package lineFollower.tests;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class DriveTest {

	Brick brick;
	
	RegulatedMotor leftMotor;
    RegulatedMotor rightMotor;
	
	public DriveTest() {
		brick = BrickFinder.getDefault();
		
		leftMotor = new EV3LargeRegulatedMotor(brick.getPort("A"));
		rightMotor = new EV3LargeRegulatedMotor(brick.getPort("B"));
	}
	
	public static void main(String[] args) {
		DriveTest test = new DriveTest();

		test.turn100Encoder();
	}
	
	public void runDriveTest() {
		System.out.println("Drive 10s forward");
		
		this.leftMotor.setSpeed(300);
    	this.rightMotor.setSpeed(300);
    	
    	this.rightMotor.forward();
    	this.leftMotor.forward();
    	
    	Delay.msDelay(2500);
    	
    	this.leftMotor.stop(true);
    	this.rightMotor.stop();
	}
	
	public void runTurnTest() {
		System.out.println("Turn 100 degree to the right");
		
		this.leftMotor.setSpeed(300);
    	this.rightMotor.setSpeed(300);
    	
    	this.rightMotor.backward();
    	this.leftMotor.forward();
    	
    	Delay.msDelay(2000);
    	
    	this.leftMotor.stop(true);
    	this.rightMotor.stop();
	}
	
	//600 tacho count
	public void turn100Encoder() {
	    this.leftMotor.resetTachoCount();
	    this.rightMotor.resetTachoCount();
	    
	    this.leftMotor.setSpeed(300);
        this.rightMotor.setSpeed(300);
        
        this.rightMotor.backward();
        this.leftMotor.forward();
        
        Delay.msDelay(2000);
        
        this.leftMotor.stop(true);
        this.rightMotor.stop(true);
        
        System.out.println("Right motor tacho count:" + this.rightMotor.getTachoCount());
        System.out.println("Left motor tacho count:" + this.leftMotor.getTachoCount());
        
        Delay.msDelay(2000);
	}
	
	public void driveForwardEncoder() {
        this.leftMotor.resetTachoCount();
        this.rightMotor.resetTachoCount();
        
        this.leftMotor.setSpeed(300);
        this.rightMotor.setSpeed(300);
        
        this.rightMotor.forward();
        this.leftMotor.forward();
        
        Delay.msDelay(2000);
        
        this.leftMotor.stop(true);
        this.rightMotor.stop(true);
        
        System.out.println("Right motor tacho count:" + this.rightMotor.getTachoCount());
        System.out.println("Left motor tacho count:" + this.leftMotor.getTachoCount());
        
        Delay.msDelay(2000);
    }

}
