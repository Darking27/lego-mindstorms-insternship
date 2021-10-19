package other;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;

public class HelloWorld {

	public static void main(String[] args) {
		Brick brick = BrickFinder.getDefault();
		GraphicsLCD display = brick.getGraphicsLCD();
		
		display.setFont(Font.getLargeFont());
		display.drawString("Hello World!", 0, 0,
				GraphicsLCD.BASELINE | GraphicsLCD.HCENTER);
		Delay.msDelay(5000);
		display.clear();
		display.refresh();
	}

}
