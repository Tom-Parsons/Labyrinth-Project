package tp.maze.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import tp.maze.main.Game;
import tp.maze.main.GameInfo;
import tp.maze.main.Menu;

public class PauseMenu {

	public static void leftClick(MouseEvent e) {
		if(resumeHover == true) {
			GameInfo.PAUSED = false;
		}
		if(saveHover == true) {
			Game.Save();
		}
		if(menuHover == true) {
			Game.SAVE_FILE = 0;
			Menu.menuState = Menu.MenuState.MAIN;
			GameInfo.PAUSED = false;
			GameInfo.gameState = GameInfo.GameState.MENU;
		}
	}
	
	private static Font titleFont = new Font("Arial", Font.BOLD, 52);
	private static Font selectionFont = new Font("Arial", Font.BOLD, 42);
	private static boolean resumeHover = false;
	private static boolean saveHover = false;
	private static boolean menuHover = false;
	
	public static void tick() {
		try {
			if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 200
					&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 200 + 140
					&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT - 50 - 5
					&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT - 50 + 28) {
				resumeHover = true;
			}else {
				resumeHover = false;
			}
			if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 200
					&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 200 + 70
					&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT - 5
					&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT+ 28) {
				saveHover = true;
			}else {
				saveHover = false;
			}
			if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 200
					&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 200 + 200
					&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT + 50 - 5
					&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT + 50 + 28) {
				menuHover = true;
			}else {
				menuHover = false;
			}
		}catch(Exception ex) {}
	}
	
	public static void render(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, GameInfo.WIDTH * 2 + 15, GameInfo.HEIGHT * 2 + 15);
		
		g.setColor(Color.WHITE);
		g.setFont(titleFont);
		g.drawString("Paused", GameInfo.WIDTH - g.getFontMetrics(titleFont).stringWidth("Paused") / 2, GameInfo.HEIGHT - 150);
		
		g.setFont(selectionFont);
		g.setColor(Color.WHITE);
		if(resumeHover) g.setColor(Color.GRAY);
		g.drawString("Resume", GameInfo.WIDTH - 200, GameInfo.HEIGHT - 50);
		
		g.setColor(Color.WHITE);
		if(saveHover) g.setColor(Color.GRAY);
		g.drawString("Save", GameInfo.WIDTH - 200, GameInfo.HEIGHT);
		
		g.setColor(Color.WHITE);
		if(menuHover) g.setColor(Color.GRAY);
		g.drawString("Return to Menu", GameInfo.WIDTH - 200, GameInfo.HEIGHT + 50);
	}
	
}
