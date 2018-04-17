package tp.maze.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class EventLog {

	public static ArrayList<String> events = new ArrayList<String>();
	
	public static void newEvent(String event) {
		events.add(event);
		if(events.size() > 5) eventView++;
		eventViewTick = 60 * 4;
	}
	
	private static int eventView = 0;
	private static int eventViewTick = 0;
	private static boolean view = false;
	
	public static void tick() {
		if(eventViewTick > 0) eventViewTick--;
		try {
			if(GameInfo.FRAME.getMousePosition().getX() > 15 
					&& GameInfo.FRAME.getMousePosition().getX() < 15 + 200
					&& GameInfo.FRAME.getMousePosition().getY() > 50
					&& GameInfo.FRAME.getMousePosition().getY() < 50 + 120) {
				view = true;
			}else {
				view = false;
			}
		}catch(Exception ex) {view = false;}
	}
	
	public static void render(Graphics g) {
		if(events.size() > 0 && (eventViewTick > 0 || view)) {
			int index = 0;
			g.setFont(new Font("Arial", Font.BOLD, 14));
			g.setColor(Color.WHITE);
			for(int i = eventView; i < events.size(); i++) {
				String event = events.get(i);
				g.drawString(event, 15, 50 + (g.getFontMetrics().getHeight() + 2) * index);
				index++;
			}
		}
	}
	
}
