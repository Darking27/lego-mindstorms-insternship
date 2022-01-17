package markerSearcher;

import driving.DriveParallelTask;
import exceptions.FinishLineException;
import exceptions.KeyPressedException;
import exceptions.MenuException;
import exceptions.StopException;
import framework.ParcoursWalkable;
import framework.Ports;
import framework.RobotUtils;
import framework.WalkableStatus;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class MarkerSearcherV2 implements ParcoursWalkable {
	
	private final int MARKER_ONE_COLOR_ID = 2;	// white
	private final int MARKER_TWO_COLOR_ID = 0;	// red
	
	private boolean markerOneFound;
	private boolean markerTwoFound;
	
	private SampleProvider colorIdSensor;
	private SampleProvider distanceSensor;
	
	private void turnLeft() {
	    Ports.LEFT_MOTOR.rotate(-450, true);
        Ports.RIGHT_MOTOR.rotate(450, false);
	}
	
	private void turnRight() {
        Ports.LEFT_MOTOR.rotate(450, true);
        Ports.RIGHT_MOTOR.rotate(-450, false);
    }
	
	public MarkerSearcherV2() {
		colorIdSensor = Ports.COLOR_SENSOR.getColorIDMode();
		distanceSensor = Ports.ULTRASONIC_SENSOR.getDistanceMode();
	}
    
    public void followRightWall() throws KeyPressedException { 
    	System.out.println("follow wall");
        Ports.LEFT_MOTOR.setSpeed(400);
        Ports.RIGHT_MOTOR.setSpeed(400);
        
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
        
        float[] distance = new float[distanceSensor.sampleSize()];
        distanceSensor.fetchSample(distance, 0);
        
        while (distance[0] > 0.4) {
            distanceSensor.fetchSample(distance, 0);
            if (rightButtonPressed()) {
                Delay.msDelay(100);
                RobotUtils.stopMotors();
                
                Ports.LEFT_MOTOR.setSpeed(200);
                Ports.LEFT_MOTOR.backward();
                while (rightButtonPressed()) {
                    
                }
                Ports.LEFT_MOTOR.stop();
                Ports.LEFT_MOTOR.setSpeed(400);
                
                Ports.LEFT_MOTOR.forward();
                Ports.RIGHT_MOTOR.forward();
            }
            if (Ports.ENTER.isDown()) {
                throw new MenuException();
            }
        }
        System.out.println("Distance to wall < 0.4");
        
        Ports.LEFT_MOTOR.rotate(-80, true);
        Ports.RIGHT_MOTOR.rotate(80, false);
        
        Ports.LEFT_MOTOR.setSpeed(400);
        Ports.RIGHT_MOTOR.setSpeed(400);
        
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
        while (!bothButtonsPressed()) {
            
        }
        RobotUtils.stopMotors();
        System.out.println("At Wall --> first turn");
    }
    
    public void followRightUntilFirstTouch() {    
    	System.out.println("right ");
        Ports.LEFT_MOTOR.setSpeed(400);
        Ports.RIGHT_MOTOR.setSpeed(300);
        
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
        
        while (!Ports.ENTER.isDown()) {
        	if (rightButtonPressed()) {
        		RobotUtils.stopMotors();
                return;
            }
        }
    }
    
    public void firstTurn() throws KeyPressedException {
    	System.out.println("first turn");
    	Ports.RIGHT_MOTOR.setSpeed(400);
    	Ports.LEFT_MOTOR.setSpeed(400);
    	
    	Ports.RIGHT_MOTOR.rotate(-100, true);
    	Ports.LEFT_MOTOR.rotate(-100, false);
    	
    	RobotUtils.turn90DegreesLeft();
    	
    	// adjust with back on wall
    	Ports.RIGHT_MOTOR.rotate(-400, true);
        Ports.LEFT_MOTOR.rotate(-400, false);
    }
    
    public void driveLine(boolean useUltrasonic) throws KeyPressedException, FinishLineException {
    	System.out.println("line");
    	DriveParallelTask driveParallel = new DriveParallelTask(600);
    	while (true) {
    		if (markerOneFound && markerTwoFound) {
    			throw new FinishLineException(null);
    		}
    		driveParallel.run();
    		
    		if (Ports.ESCAPE.isDown())
    			throw new StopException();
    		if (Ports.ENTER.isDown())
    			throw new MenuException();
    		
    		float[] colorSensorSample = new float[colorIdSensor.sampleSize()];
    		colorIdSensor.fetchSample(colorSensorSample, 0);
    		if (!markerOneFound && isMarkerOne(colorSensorSample)) {
    			markerOneFound = true;
    			System.out.println("Marker 1 found");
    			markerFoundSignal();
    		}
    		
    		if (!markerTwoFound && isMarkerTwo(colorSensorSample)) {
    			markerTwoFound = true;
    			System.out.println("Marker 2 found");
    			markerFoundSignal();
    		}
    		
    		if (isAtWall(useUltrasonic)) {
    			driveParallel.cancel();
    			break;
    		}
    	}
    }
    
    private void turn180Left() throws KeyPressedException {
    	System.out.println("turn 180 left");
    	
    	
    	RobotUtils.setSpeed(450);
    	RobotUtils.turn90DegreesLeft();
    	RobotUtils.driveStraight(300);
    	RobotUtils.setSpeed(450);
    	RobotUtils.turn90DegreesLeft();
    	
    	RobotUtils.setSpeed(600);
    	
    	// adjust with back on wall
    	Ports.RIGHT_MOTOR.rotate(-550, true);
        Ports.LEFT_MOTOR.rotate(-550, false);
    	
    }
    
    private void turn180Right() throws KeyPressedException {
    	System.out.println("turn 180 right");

    	
    	RobotUtils.setSpeed(450);
        RobotUtils.turn90DegreesRight();
        RobotUtils.driveStraight(300);
        RobotUtils.setSpeed(450);
        RobotUtils.turn90DegreesRight();
        
        RobotUtils.setSpeed(600);
        
        // adjust with back on wall
        Ports.RIGHT_MOTOR.rotate(-550, true);
        Ports.LEFT_MOTOR.rotate(-550, false);
    }
    
    
    private boolean rightButtonPressed() {
        float[] touchRight = new float[Ports.RIGHT_TOUCH_SENSOR.sampleSize()];
        
        Ports.RIGHT_TOUCH_SENSOR.fetchSample(touchRight, 0);
        
        return touchRight[0] == 1;
    }
    
    private boolean bothButtonsPressed() {
        float[] touchLeft = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
        
        Ports.LEFT_TOUCH_SENSOR.fetchSample(touchLeft, 0);
        
        float[] touchRight = new float[Ports.RIGHT_TOUCH_SENSOR.sampleSize()];
        
        Ports.RIGHT_TOUCH_SENSOR.fetchSample(touchRight, 0);
        
        return touchRight[0] == 1 && touchLeft[0] == 1;
    }
    
	private boolean isMarkerOne(float[] colorSensorSample) {
		return colorSensorSample[0] == MARKER_ONE_COLOR_ID;
	}
	
	private boolean isMarkerTwo(float[] colorSensorSample) {
		return colorSensorSample[0] == MARKER_TWO_COLOR_ID;
	}
	
	private boolean isAtWall(boolean useUltrasonic) {
		if (useUltrasonic) {
			float[] distance = new float[distanceSensor.sampleSize()];
			distanceSensor.fetchSample(distance, 0);
			
			return distance[0] < 0.12 || bothButtonsPressed();
		}
		return bothButtonsPressed();
	}
	
	public void markerFoundSignal() {
	    Thread signalThread = new SignalThread();
	    signalThread.start();
	}
	
	public void reset() {
		markerOneFound = false;
		markerTwoFound = false;
		Ports.COLOR_SENSOR.setCurrentMode(0);
		Ports.COLOR_SENSOR.getColorID();
	}

	@Override
	public WalkableStatus start_walking() throws KeyPressedException {
		reset();
		
		try {
			followRightUntilFirstTouch();
			followRightWall();
			firstTurn();
			
			while (true) {
				driveLine(true);
				turn180Left();
				driveLine(true);
				turn180Right();
			}
		}
		catch (FinishLineException e) {
			return WalkableStatus.FINISHED;
		}
	}
	
	private class SignalThread extends Thread {
	    
	    @Override
	    public void run() {
	        Button.LEDPattern(9);
	        Sound.twoBeeps();
	        Delay.msDelay(1000);
	        Button.LEDPattern(0);
	    }
	}

}
