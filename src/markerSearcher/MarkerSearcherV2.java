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
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class MarkerSearcherV2 implements ParcoursWalkable {
	
	private final int MARKER_ONE_COLOR_ID = 2;	// white
	private final int MARKER_TWO_COLOR_ID = 0;	// red
	
	private boolean markerOneFound;
	private boolean markerTwoFound;
	
	private SampleProvider colorIdSensor;
	private SampleProvider distanceSensor;
	
	public MarkerSearcherV2() {
		colorIdSensor = Ports.COLOR_SENSOR.getColorIDMode();
		distanceSensor = Ports.ULTRASONIC_SENSOR.getDistanceMode();
	}
    
    public void followRightWall() { 
    	System.out.println("follow wall");
        Ports.LEFT_MOTOR.setSpeed(300);
        Ports.RIGHT_MOTOR.setSpeed(300);
        
        Ports.LEFT_MOTOR.forward();
        Ports.RIGHT_MOTOR.forward();
        
        while (!Ports.ENTER.isDown()) {
            if (bothButtonsPressed()) {
            	RobotUtils.stopMotors();
            	return;
            } else if (rightButtonPressed()) {
                RobotUtils.stopMotors();
                
                Ports.LEFT_MOTOR.setSpeed(150);
                Ports.LEFT_MOTOR.backward();
                while (rightButtonPressed()) {
                    
                }
                Ports.LEFT_MOTOR.stop();
                Ports.LEFT_MOTOR.setSpeed(300);
                
                Ports.LEFT_MOTOR.forward();
                Ports.RIGHT_MOTOR.forward();
            }
            
        }
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
    	Ports.LEFT_MOTOR.setSpeed(150);
    	
    	Ports.RIGHT_MOTOR.rotate(-500, true);
    	Ports.LEFT_MOTOR.rotate((int) (-500*0.98));
    	
    	RobotUtils.driveStraight(300);
    	
    	RobotUtils.turn90DegreesLeft();
    }
    
    public void driveLine(boolean useUltrasonic) throws KeyPressedException, FinishLineException {
    	System.out.println("line");
    	DriveParallelTask driveParallel = new DriveParallelTask(400);
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
    		}
    		
    		if (!markerTwoFound && isMarkerTwo(colorSensorSample)) {
    			markerTwoFound = true;
    			System.out.println("Marker 2 found");
    		}
    		
    		if (isAtWall(useUltrasonic)) {
    			driveParallel.cancel();
    			break;
    		}
    	}
    }
    
    private void turn180Left() throws KeyPressedException {
    	System.out.println("turn 180 left");
    	
    	
    	System.out.println("- 90");
    	RobotUtils.turn90DegreesLeft();
    	
    	System.out.println("- straight");
    	RobotUtils.driveStraight(100);
    	
		/*
		 * DriveParallelTask driveParallel = new DriveParallelTask(400); while
		 * (!markerOneFound || !markerTwoFound) { if (Ports.ESCAPE.isDown()) throw new
		 * StopException(); if (Ports.ENTER.isDown()) throw new MenuException();
		 * 
		 * driveParallel.run();
		 * 
		 * float[] colorSensorSample = new float[colorIdSensor.sampleSize()]; if
		 * (!markerOneFound && isMarkerOne(colorSensorSample)) { markerOneFound = true;
		 * System.out.println("Marker 1 found"); }
		 * 
		 * if (!markerTwoFound && isMarkerTwo(colorSensorSample)) { markerTwoFound =
		 * true; System.out.println("Marker 2 found"); }
		 * 
		 * if (Ports.LEFT_MOTOR.getTachoCount() > 100 ||
		 * Ports.RIGHT_MOTOR.getTachoCount() > 100) { driveParallel.cancel(); break; } }
		 */
    	
    	System.out.println("- 90");
    	RobotUtils.turn90DegreesLeft();
    	
    }
    
    private void turn180Right() throws KeyPressedException {
    	System.out.println("turn 180 right");
    	Ports.RIGHT_MOTOR.setSpeed(300);
    	Ports.LEFT_MOTOR.setSpeed(300);
    	
    	System.out.println("- back");
    	Ports.RIGHT_MOTOR.rotate(-300, true);
    	Ports.LEFT_MOTOR.rotate((int) (-300*0.98));
    	
    	Ports.LEFT_MOTOR.stop(true);
    	Ports.RIGHT_MOTOR.stop();
    	
    	System.out.println("- 90");
    	
    	RobotUtils.turn90DegreesRight();
    	
    	System.out.println("- forward");
    	
    	RobotUtils.driveStraight(100);
		/*
		 * Ports.LEFT_MOTOR.resetTachoCount(); Ports.RIGHT_MOTOR.resetTachoCount();
		 * DriveParallelTask driveParallel = new DriveParallelTask(400); while
		 * ((!markerOneFound || !markerTwoFound)) { if (Ports.ESCAPE.isDown()) throw new
		 * StopException(); if (Ports.ENTER.isDown()) throw new MenuException();
		 * 
		 * driveParallel.run();
		 * 
		 * float[] colorSensorSample = new float[colorIdSensor.sampleSize()]; if
		 * (!markerOneFound && isMarkerOne(colorSensorSample)) { markerOneFound = true;
		 * System.out.println("Marker 1 found"); }
		 * 
		 * if (!markerTwoFound && isMarkerTwo(colorSensorSample)) { markerTwoFound =
		 * true; System.out.println("Marker 2 found"); }
		 * 
		 * System.out.println(Ports.LEFT_MOTOR.getTachoCount());
		 * 
		 * if (Ports.LEFT_MOTOR.getTachoCount() > 100 ||
		 * Ports.RIGHT_MOTOR.getTachoCount() > 100) { RobotUtils.stopMotors(); break; }
		 * }
		 */
    	
    	System.out.println("- 90");
    	
    	RobotUtils.turn90DegreesRight();
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
			
			return distance[0] < 0.14 || bothButtonsPressed();
		}
		return bothButtonsPressed();
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
				driveLine(false);
				turn180Right();
			}
		}
		catch (FinishLineException e) {
			return WalkableStatus.FINISHED;
		}
	}

}
