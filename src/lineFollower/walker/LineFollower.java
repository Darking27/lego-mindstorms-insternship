package lineFollower.walker;

import framework.ParcoursWalkable;
import framework.Ports;
import framework.WalkableStatus;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lineFollower.walker.colorSensor.AutoAdjustFilter;
import robotInteraction.InputEvents;

public class LineFollower implements ParcoursWalkable {

	int ENCODER_GAP_DISTANCE = 600;
	int ENCODER_TURN_100 = 600;
	int ENCODER_TURN_45 = 280;
	
	int DEFAULT_SPEED = 300;
	
	
	SampleProvider autoAdjustRGBFilter;
	State state = State.START;
	
	public LineFollower() {
		SampleProvider rgbMode = Ports.COLOR_SENSOR.getRGBMode();
		autoAdjustRGBFilter = new AutoAdjustFilter(rgbMode);
	}
	
	private void logCurrentState() {
    	System.out.println(this.state.toString());
    }
	
	@Override
	public WalkableStatus start_walking() {
		while (!state.equals(State.DONE)) {
			switch (state) {
				case START:
					logCurrentState();
					if (this.handleStart().equals(InputEvents.ENTER)) {
						return WalkableStatus.MENU;
					}
					this.state = State.SEARCH_LINE;
					break;
					
				case SEARCH_LINE:
					logCurrentState();
					if (this.handleSearchLine().equals(InputEvents.ENTER)) {
						return WalkableStatus.MENU;
					}
					break;
					
				case SEARCH_LINE_BORDER_LEFT:
					logCurrentState();
					if (this.handleSearchLineBorderLeft().equals(InputEvents.ENTER)) {
						return WalkableStatus.MENU;
					}
					break;
					
				case FOLLOW_LINE:
					logCurrentState();
					if (this.handleFollowLine().equals(InputEvents.ENTER)) {
						return WalkableStatus.MENU;
					}
					break;
					
				case GAP:
					logCurrentState();
					if (this.driveForwardStraight(ENCODER_GAP_DISTANCE).equals(InputEvents.ENTER)) {
						return WalkableStatus.MENU;
					}
					break;
					
				case OBSTACLE:
					logCurrentState();
					if (Ports.ENTER.isDown()) {
						return WalkableStatus.MENU;
					}
					float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
					autoAdjustRGBFilter.fetchSample(sample, 0);
					if (AutoAdjustFilter.getGrayValue(sample) > 0.8) {
						state = State.FOLLOW_LINE;
					}
					break;
					
				case DONE:
					logCurrentState();
					return WalkableStatus.FINISHED;
			}
		}
		return WalkableStatus.FINISHED;
	}
	
	/**
	 * Regulator to follow the line
	 * @return
	 */
	private InputEvents handleFollowLine() {
		double KpLeft = 600;			//To turn away from the line (should be greater than KpRight)
		double KpRight = 400;
		
		double targetValue = 0.4;
		double defaultSpeed = 300;
		int speedCap = 400;
		
		float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
		autoAdjustRGBFilter.fetchSample(sample, 0);
		
		double currentValue;
	    int leftSpeed;
	    int rightSpeed;
		
		Ports.LEFT_MOTOR.setSpeed((int) defaultSpeed);
		Ports.RIGHT_MOTOR.setSpeed((int) defaultSpeed);
		
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		
		while (!Ports.ENTER.isDown()) {
			if (buttonPressed()) {
				state = State.OBSTACLE;
				return InputEvents.NONE;
			}
			if (isFinishLine(sample)) {
				state = State.DONE;
				return InputEvents.NONE;
			}
			
			currentValue = AutoAdjustFilter.getGrayValue(sample);
			
			//TODO realistic value??
			if (currentValue < 0.1) {
				//turn specific value back to the left after regulater overregulated to the right (maybe even backward)
				Ports.RIGHT_MOTOR.rotate(50);
				state = State.SEARCH_LINE;
				return InputEvents.NONE;
			}
			
			double diff = currentValue - targetValue;
            if (diff >= 0) {
	            leftSpeed = (int) (defaultSpeed - Math.abs(diff * KpLeft));
	            rightSpeed = (int) (defaultSpeed + Math.abs(diff * KpLeft));
            } else {
            	leftSpeed = (int) (defaultSpeed + Math.abs(diff * KpRight));
	            rightSpeed = (int) (defaultSpeed - Math.abs(diff * KpRight));
            }
            
            leftSpeed = limitSpeed(leftSpeed, speedCap);
            rightSpeed = limitSpeed(rightSpeed, speedCap);
            
            Ports.LEFT_MOTOR.setSpeed(Math.abs(leftSpeed));
            Ports.RIGHT_MOTOR.setSpeed(Math.abs(rightSpeed));
            
            if (leftSpeed >= 0) {
            	Ports.LEFT_MOTOR.forward();
            } else {
            	Ports.LEFT_MOTOR.backward();
            }
            if (rightSpeed >= 0) {
            	Ports.RIGHT_MOTOR.forward();
            } else {
            	Ports.RIGHT_MOTOR.backward();
            }
            autoAdjustRGBFilter.fetchSample(sample, 0);
		}
		
		return InputEvents.ENTER;
	}
	
	public boolean isFinishLine(float[] sample) {
		return (sample[0] * 3 < sample [2]) && (sample[1] * 4 < sample[2]);
	}
	
	private int limitSpeed(int targetSpeed, int speedCap) {
		if (Math.abs(targetSpeed) > speedCap) {
			if (targetSpeed >= 0) {
				return speedCap;
			} else {
				return -speedCap;
			}
		}
		return targetSpeed;
	}

	/**
	 * Handles state "start"
	 * 1. Drive forward to drive over the starting line
	 * 2. Turn left and right to callibrate the auto adjust filter
	 * @return
	 */
	private InputEvents handleStart() {
		InputEvents event = driveForwardStraight(ENCODER_GAP_DISTANCE);
		if (event.equals(InputEvents.ENTER)) {
			return event;
		}
		event = calibrateFilter(ENCODER_TURN_45, true);
		if (event.equals(InputEvents.ENTER)) {
			return event;
		}
		event = calibrateFilter(2 * ENCODER_TURN_45, false);
		if (event.equals(InputEvents.ENTER)) {
			return event;
		}
		event = calibrateFilter(ENCODER_TURN_45, true);
		if (event.equals(InputEvents.ENTER)) {
			return event;
		}
		return InputEvents.NONE;
	}
	
	/**
	 * Handles state "search line"
	 * Turn left and right in order to fild line
	 * @return
	 */
	private InputEvents handleSearchLine() {
		InputEvents event = searchLine(ENCODER_TURN_100, true);
		if (event.equals(InputEvents.ENTER)) {
			return event;
		} else if (event.equals(InputEvents.LINE)) {
			state = State.FOLLOW_LINE;
			return InputEvents.NONE;
		} else if (event.equals(InputEvents.BUTTON_PRESSED)) {
			state = State.OBSTACLE;
			return InputEvents.NONE;
		}
		event = searchLine(2 * ENCODER_TURN_100, false);
		if (event.equals(InputEvents.ENTER)) {
			return event;
		} else if (event.equals(InputEvents.LINE)) {
			state = State.SEARCH_LINE_BORDER_LEFT;
			return InputEvents.NONE;
		} else if (event.equals(InputEvents.BUTTON_PRESSED)) {
			state = State.OBSTACLE;
			return InputEvents.NONE;
		}
		event = searchLine(ENCODER_TURN_100, true);
		if (event.equals(InputEvents.ENTER)) {
			return event;
		} else if (event.equals(InputEvents.LINE)) {
			state = State.FOLLOW_LINE;
			return InputEvents.NONE;
		} else if (event.equals(InputEvents.BUTTON_PRESSED)) {
			state = State.OBSTACLE;
			return InputEvents.NONE;
		}
		state = State.GAP;
		return InputEvents.NONE;
	}
	
	private InputEvents handleSearchLineBorderLeft() {
		Ports.LEFT_MOTOR.setSpeed(DEFAULT_SPEED);
		Ports.RIGHT_MOTOR.setSpeed(DEFAULT_SPEED);
		
		Ports.LEFT_MOTOR.backward();
		Ports.RIGHT_MOTOR.forward();
		
		float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
		
		autoAdjustRGBFilter.fetchSample(sample, 0);
		
		while (AutoAdjustFilter.getGrayValue(sample) > 0.4) {
			autoAdjustRGBFilter.fetchSample(sample, 0);
			if (buttonPressed()) {
				state = State.OBSTACLE;
				return InputEvents.NONE;
			}
			if (Ports.ENTER.isDown()) {
				return InputEvents.ENTER;
			}
		}
		
		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(true);
		
		this.state = State.FOLLOW_LINE;
		
		return InputEvents.NONE;
	}
	
	
	// Robot Interaction
	
	private InputEvents driveForwardStraight(int encoderValue) {
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
		
		int leftTachoCount = 0;
		int rightTachoCount = 0;
		
		Ports.LEFT_MOTOR.setSpeed(DEFAULT_SPEED);
		Ports.RIGHT_MOTOR.setSpeed(DEFAULT_SPEED);
		
		Ports.LEFT_MOTOR.forward();
		Ports.RIGHT_MOTOR.forward();
		
		while (leftTachoCount <= encoderValue || rightTachoCount <= encoderValue) {
			if (rightTachoCount >= encoderValue) {
				Ports.RIGHT_MOTOR.stop(true);
			}
			if (leftTachoCount >= encoderValue) {
				Ports.LEFT_MOTOR.stop(true);
			}
			if (Ports.ENTER.isDown()) {
				return InputEvents.ENTER;
			}
			if (buttonPressed()) {
				state = State.OBSTACLE;
				return InputEvents.NONE;
			}
			leftTachoCount = Ports.LEFT_MOTOR.getTachoCount();
			rightTachoCount = Ports.RIGHT_MOTOR.getTachoCount();
		}
		
		Ports.LEFT_MOTOR.stop(true);
		Ports.RIGHT_MOTOR.stop(true);
		
		return InputEvents.NONE;
	}
	
	private InputEvents searchLine(int encoderValue, boolean rightTurn) {
		float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
		
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
		
		RegulatedMotor m1;
		RegulatedMotor m2;
		
		if (rightTurn) {
			m1 = Ports.LEFT_MOTOR;
			m2 = Ports.RIGHT_MOTOR;
		} else {
			m1 = Ports.RIGHT_MOTOR;
			m2 = Ports.LEFT_MOTOR;
		}
		
		int m1TachoCount = 0;
		int m2TachoCount = 0;
		
		Ports.LEFT_MOTOR.setSpeed(DEFAULT_SPEED);
		Ports.RIGHT_MOTOR.setSpeed(DEFAULT_SPEED);
		
		m1.forward();
		m2.backward();
		
		while (m1TachoCount <= encoderValue || m2TachoCount <= encoderValue) {
			if (m1TachoCount >= encoderValue) {
				m1.stop(true);
			}
			if (m2TachoCount >= encoderValue) {
				m2.stop(true);
			}
			if (Ports.ENTER.isDown()) {
				return InputEvents.ENTER;
			}
			
			double gray = AutoAdjustFilter.getGrayValue(sample);
			if (gray >= 0.7) {
				return InputEvents.LINE;
			}
			if (buttonPressed()) {
				return InputEvents.BUTTON_PRESSED;
			}
			m1TachoCount = Math.abs(m1.getTachoCount());
			m2TachoCount = Math.abs(m2.getTachoCount());
			
			autoAdjustRGBFilter.fetchSample(sample, 0);
		}
		
		m1.stop(true);
		m2.stop(true);
		
		return InputEvents.NONE;
	}
	
	private InputEvents calibrateFilter(int encoderValue, boolean rightTurn) {
		float[] sample = new float[autoAdjustRGBFilter.sampleSize()];
		
		Ports.LEFT_MOTOR.resetTachoCount();
		Ports.RIGHT_MOTOR.resetTachoCount();
		
		RegulatedMotor m1;
		RegulatedMotor m2;
		
		if (rightTurn) {
			m1 = Ports.LEFT_MOTOR;
			m2 = Ports.RIGHT_MOTOR;
		} else {
			m1 = Ports.RIGHT_MOTOR;
			m2 = Ports.LEFT_MOTOR;
		}
		
		int m1TachoCount = 0;
		int m2TachoCount = 0;
		
		Ports.LEFT_MOTOR.setSpeed(400);
		Ports.RIGHT_MOTOR.setSpeed(400);
		
		m1.forward();
		m2.backward();
		
		while (m1TachoCount <= encoderValue || m2TachoCount <= encoderValue) {
			if (m1TachoCount >= encoderValue) {
				m1.stop(true);
			}
			if (m2TachoCount >= encoderValue) {
				m2.stop(true);
			}
			if (Ports.ENTER.isDown()) {
				return InputEvents.ENTER;
			}
			m1TachoCount = Math.abs(m1.getTachoCount());
			m2TachoCount = Math.abs(m2.getTachoCount());
			
			autoAdjustRGBFilter.fetchSample(sample, 0);
		}
		
		m1.stop(true);
		m2.stop(true);
		
		return InputEvents.NONE;
	}
	
	private boolean buttonPressed() {
		float[] touchLeft = new float[Ports.LEFT_TOUCH_SENSOR.sampleSize()];
		float[] touchRight = new float[Ports.RIGHT_TOUCH_SENSOR.sampleSize()];
		
		Ports.LEFT_TOUCH_SENSOR.fetchSample(touchLeft, 0);
		Ports.RIGHT_TOUCH_SENSOR.fetchSample(touchRight, 0);
		
		return touchLeft[0] == 1 || touchRight[0] == 1;
	}
}
