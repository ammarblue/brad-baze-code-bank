import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class RENDER{
	public static final int TARGET_FPS = 60;
	
	public static void RENDER(final JPanel panel){
		Timer timer = new Timer();
		TimerTask repaint = new TimerTask(){
			@Override
			public void run() {
				panel.repaint();	
			}	
		};
		timer.schedule(repaint, 0, 1000/TARGET_FPS);
	}
}
