package tp.maze.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class Menu {

	private static Game game;
	
	public static void setupMenu(Game g) {
		game = g;
	}
	
	public enum MenuState{
		MAIN,
		NEW_GAME,
		LOAD_GAME
	}
	
	public static MenuState menuState = MenuState.MAIN;
	
	private static Color newGameButtonColour = Color.GRAY;
	private static Rectangle newGameButton = new Rectangle(GameInfo.WIDTH - 150, 150, 300, 75);
	private static Color loadGameButtonColour = new Color(150, 150, 150, 255);
	private static Rectangle loadGameButton = new Rectangle(GameInfo.WIDTH - 150, 275, 300, 75);
	private static Color exitButtonColour = Color.GRAY;
	
	//Needs to be accessed in the Game.class so that we can stop the program
	public static Rectangle exitButton = new Rectangle(GameInfo.WIDTH - 150, 400, 300, 75);
	
	public static void leftClick(MouseEvent e) {
		if(menuState == MenuState.MAIN) {
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > exitButton.getX() 
						&& GameInfo.FRAME.getMousePosition().getX() < exitButton.getX() + exitButton.getWidth() 
						&& GameInfo.FRAME.getMousePosition().getY() > exitButton.getY()
						&& GameInfo.FRAME.getMousePosition().getY() < exitButton.getY() + exitButton.getHeight()) {
					//HANDLED IN GAME CLASS TO TERMINATE PROGRAM
				}
				if(GameInfo.FRAME.getMousePosition().getX() > newGameButton.getX() 
						&& GameInfo.FRAME.getMousePosition().getX() < newGameButton.getX() + newGameButton.getWidth() 
						&& GameInfo.FRAME.getMousePosition().getY() > newGameButton.getY() + 25
						&& GameInfo.FRAME.getMousePosition().getY() < newGameButton.getY() + newGameButton.getHeight() + 25) {
					menuState = MenuState.NEW_GAME;
				}
				if(GameInfo.FRAME.getMousePosition().getX() > loadGameButton.getX() 
						&& GameInfo.FRAME.getMousePosition().getX() < loadGameButton.getX() + loadGameButton.getWidth() 
						&& GameInfo.FRAME.getMousePosition().getY() > loadGameButton.getY() + 25
						&& GameInfo.FRAME.getMousePosition().getY() < loadGameButton.getY() + loadGameButton.getHeight() + 25
						&& FileHandler.hasSaveFile()) {
					menuState = MenuState.LOAD_GAME;
				}
			}catch(Exception ex) {}
		}else if(menuState == MenuState.NEW_GAME) {
			if(game1Hover == true) {
				//If we are hovering over the first save game
				menuState = MenuState.MAIN;
				//Initialize the game using save '1'
				game.initializeGame(1);
			}
			if(game2Hover == true) {
				menuState = MenuState.MAIN;
				game.initializeGame(2);
			}
			if(game3Hover == true) {
				menuState = MenuState.MAIN;
				game.initializeGame(3);
			}
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 35
						&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 35 + 75
						&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT * 2 - 50 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT * 2 - 50 + 40 + 25) {
					menuState = MenuState.MAIN;
				}
			}catch(Exception ex) {}
		}else if(menuState == MenuState.LOAD_GAME) {
			if(game1Hover == true && FileHandler.hasSave(1)) {
				menuState = MenuState.MAIN;
				game.initializeLoad(1);
			}
			if(game2Hover == true && FileHandler.hasSave(2)) {
				menuState = MenuState.MAIN;
				game.initializeLoad(2);
			}
			if(game3Hover == true && FileHandler.hasSave(3)) {
				menuState = MenuState.MAIN;
				game.initializeLoad(3);
			}
			try {
			if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 35
					&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 35 + 75
					&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT * 2 - 50 + 25
					&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT * 2 - 50 + 40 + 25) {
				menuState = MenuState.MAIN;
			}
			}catch(Exception ex) {}
		}
	}
	
	private static boolean game1Hover = false;
	private static boolean game2Hover = false;
	private static boolean game3Hover = false;
	private static boolean backHover = false;
	
	public static void tick() {
		if(menuState == MenuState.MAIN) {
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > exitButton.getX()
						&& GameInfo.FRAME.getMousePosition().getX() < exitButton.getX() + exitButton.getWidth() 
						&& GameInfo.FRAME.getMousePosition().getY() > exitButton.getY() + 25
						&& GameInfo.FRAME.getMousePosition().getY() < exitButton.getY() + exitButton.getHeight() + 25) {
					exitButtonColour = Color.WHITE;
				}else {
					exitButtonColour = Color.GRAY;
				}
				if(GameInfo.FRAME.getMousePosition().getX() > newGameButton.getX() 
						&& GameInfo.FRAME.getMousePosition().getX() < newGameButton.getX() + newGameButton.getWidth() 
						&& GameInfo.FRAME.getMousePosition().getY() > newGameButton.getY() + 25
						&& GameInfo.FRAME.getMousePosition().getY() < newGameButton.getY() + newGameButton.getHeight() + 25) {
					newGameButtonColour = Color.WHITE;
				}else {
					newGameButtonColour = Color.GRAY;
				}
				if(GameInfo.FRAME.getMousePosition().getX() > loadGameButton.getX() 
						&& GameInfo.FRAME.getMousePosition().getX() < loadGameButton.getX() + loadGameButton.getWidth() 
						&& GameInfo.FRAME.getMousePosition().getY() > loadGameButton.getY() + 25
						&& GameInfo.FRAME.getMousePosition().getY() < loadGameButton.getY() + loadGameButton.getHeight() + 25) {
					loadGameButtonColour = Color.WHITE;
				}else {
					loadGameButtonColour = Color.GRAY;
				}
				if(!FileHandler.hasSaveFile()) loadGameButtonColour = new Color(50, 50, 50, 255); 
			}catch(Exception ex) {}
		}else {
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > 50
						&& GameInfo.FRAME.getMousePosition().getX() < 50 + (GameInfo.WIDTH * 2) / 3 - 50 
						&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + GameInfo.HEIGHT * 2 - 200 + 25) {
					game1Hover = true;
				}else {
					game1Hover = false;
				}
				if(GameInfo.FRAME.getMousePosition().getX() > 50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30
						&& GameInfo.FRAME.getMousePosition().getX() < (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + (GameInfo.WIDTH * 2) / 3 - 50 
						&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + GameInfo.HEIGHT * 2 - 200 + 25) {
					game2Hover = true;
				}else {
					game2Hover = false;
				}
				if(GameInfo.FRAME.getMousePosition().getX() > 50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2
						&& GameInfo.FRAME.getMousePosition().getX() < (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + (GameInfo.WIDTH * 2) / 3 - 50 
						&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + GameInfo.HEIGHT * 2 - 200 + 25) {
					game3Hover = true;
				}else {
					game3Hover = false;
				}
				if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 35
						&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 35 + 75
						&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT * 2 - 50 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT * 2 - 50 + 40 + 25) {
					backHover = true;
				}else {
					backHover = false;
				}
			}catch(Exception ex) {}
		}
		if(GameInfo.gameState == GameInfo.GameState.LOADING) {
			loadingTick++;
			if(loadingTick >= 20) {
				if(loadingDotIncrease) {
					loadingText = loadingText + ".";
					if(loadingText.contains("...")) loadingDotIncrease = false;
				}else {
					loadingText = loadingText.substring(0, loadingText.length() - 1);
					if(loadingText.equals("Loading")) loadingDotIncrease = true;
				}
				loadingTick = 0;
			}
		}
	}
	
	private static int loadingTick = 0;
	private static boolean loadingDotIncrease = true;
	private static String loadingText = "Loading";
	
	public static void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		if(GameInfo.gameState == GameInfo.GameState.MENU) {
			if(menuState == MenuState.MAIN) {
				Font font = new Font("arial", Font.BOLD, 36);
				g2d.setFont(font);
				g2d.setColor(Color.WHITE);
				g2d.drawString(GameInfo.TITLE, GameInfo.WIDTH - g2d.getFontMetrics(font).stringWidth(GameInfo.TITLE) / 2, 100);
				
				g2d.setColor(newGameButtonColour);
				g2d.fill(newGameButton);
				g2d.setColor(loadGameButtonColour);
				g2d.fill(loadGameButton);
				g2d.setColor(exitButtonColour);
				g2d.fill(exitButton);
				Font buttonFont = new Font("arial", Font.BOLD, 28);
				g.setColor(Color.BLACK);
				g2d.setFont(buttonFont);
				g2d.drawString("New Game", GameInfo.WIDTH - g2d.getFontMetrics(buttonFont).stringWidth("New Game") / 2, (int) (newGameButton.getY() + newGameButton.getHeight() - g2d.getFontMetrics(buttonFont).getHeight() / 1.25));
				g2d.drawString("Load Game", GameInfo.WIDTH - g2d.getFontMetrics(buttonFont).stringWidth("Load Game") / 2, (int) (loadGameButton.getY() + loadGameButton.getHeight() - g2d.getFontMetrics(buttonFont).getHeight() / 1.25));
				g2d.drawString("Exit", GameInfo.WIDTH - g2d.getFontMetrics(buttonFont).stringWidth("Exit") / 2, (int) (exitButton.getY() + exitButton.getHeight() - g2d.getFontMetrics(buttonFont).getHeight() / 1.25));
				
				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("arial", Font.BOLD, 26));
				g2d.drawString("Controls", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - g2d.getFontMetrics().stringWidth("Controls"), 100);
				g2d.setFont(new Font("arial", Font.BOLD, 20));
				g2d.drawString("W - Walk Up", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - 150, 125);
				g2d.drawString("A - Walk Left", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - 150, 150);
				g2d.drawString("S - Walk Down", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - 150, 175);
				g2d.drawString("D - Walk Right", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - 150, 200);
				g2d.drawString("Shift - Sprint", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - 150, 225);
				g2d.drawString("I - Open/Close Inventory", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - 150, 250);
				g2d.drawString("Spacebar - Attack", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - 150, 275);
				g2d.drawString("Spacebar - Open Chest", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - 150, 300);
				g2d.drawString("Q - Use Active Potion", GameInfo.WIDTH + (int)(GameInfo.WIDTH / 1.25) - 150, 325);
			}else if(menuState == MenuState.NEW_GAME) {
				Font buttonFont = new Font("arial", Font.BOLD, 28);
				g2d.setFont(buttonFont);
				if(game1Hover) {
					g2d.setColor(Color.GRAY);
					g2d.fillRoundRect(50, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				}
				g2d.setColor(Color.WHITE);
				g2d.drawRoundRect(50, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				if(game2Hover) {
					g2d.setColor(Color.GRAY);
					g2d.fillRoundRect(50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				}
				g2d.setColor(Color.WHITE);
				g2d.drawRoundRect(50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				if(game3Hover) {
					g2d.setColor(Color.GRAY);
					g2d.fillRoundRect(50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				}
				g2d.setColor(Color.WHITE);
				g2d.drawRoundRect(50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				
				g2d.drawString("Save 1", 50 + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("Save 1") / 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + g2d.getFontMetrics(buttonFont).getHeight());
				g2d.drawString("Save 2", (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("Save 2") / 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + g2d.getFontMetrics(buttonFont).getHeight());
				g2d.drawString("Save 3", (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("Save 3") / 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + g2d.getFontMetrics(buttonFont).getHeight());
				
				if(FileHandler.hasSave(1)) {
					g2d.drawString("Overwrite", 50 + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("Overwrite") / 2, GameInfo.HEIGHT - g2d.getFontMetrics(buttonFont).getHeight() / 2);
					Font smallButtonFont = new Font("arial", Font.BOLD, 18);
					g2d.setFont(smallButtonFont);
					g2d.drawString("Last Played:", 50 + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth("Last Played:") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() / 2);
					g2d.drawString(FileHandler.saveLastModified(1), 50 + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth(FileHandler.saveLastModified(1)) / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() + 5);
				}else {
					g2d.drawString("New Game", 50 + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("New Game") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(buttonFont).getHeight() / 2);
				}
				g2d.setFont(buttonFont);
				if(FileHandler.hasSave(2)) {
					g2d.drawString("Overwrite", (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("Overwrite") / 2, GameInfo.HEIGHT - g2d.getFontMetrics(buttonFont).getHeight() / 2);
					Font smallButtonFont = new Font("arial", Font.BOLD, 18);
					g2d.setFont(smallButtonFont);
					g2d.drawString("Last Played:", (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth("Last Played:") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() / 2);
					g2d.drawString(FileHandler.saveLastModified(2), (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth(FileHandler.saveLastModified(2)) / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() + 5);
				}else {
					g2d.drawString("New Game", (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("New Game") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(buttonFont).getHeight() / 2);
				}
				g2d.setFont(buttonFont);
				if(FileHandler.hasSave(3)) {
					g2d.drawString("Overwrite", (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("Overwrite") / 2, GameInfo.HEIGHT - g2d.getFontMetrics(buttonFont).getHeight() / 2);
					Font smallButtonFont = new Font("arial", Font.BOLD, 18);
					g2d.setFont(smallButtonFont);
					g2d.drawString("Last Played:", (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth("Last Played:") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() / 2);
					g2d.drawString(FileHandler.saveLastModified(3), (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth(FileHandler.saveLastModified(3)) / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() + 5);
				}else {
					g2d.drawString("New Game", (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("New Game") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(buttonFont).getHeight() / 2);
				}
				
				g2d.setFont(buttonFont);
				if(backHover) {
					g2d.setColor(Color.GRAY);
					g2d.fillRect(GameInfo.WIDTH - 35, GameInfo.HEIGHT * 2 - 50, 75, 40);
				}
				g2d.setColor(Color.WHITE);
				g2d.drawRect(GameInfo.WIDTH - 35, GameInfo.HEIGHT * 2 - 50, 75, 40);
				g2d.drawString("Back", GameInfo.WIDTH - g2d.getFontMetrics(buttonFont).stringWidth("Back") + 35, GameInfo.HEIGHT * 2 - g2d.getFontMetrics(buttonFont).getHeight() + 15);
			}else if(menuState == MenuState.LOAD_GAME) {
				Font buttonFont = new Font("arial", Font.BOLD, 28);
				g2d.setFont(buttonFont);
				if(game1Hover && FileHandler.hasSave(1)) {
					g2d.setColor(Color.GRAY);
					g2d.fillRoundRect(50, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				}
				g2d.setColor(Color.WHITE);
				if(!FileHandler.hasSave(1)) g2d.setColor(Color.GRAY);
				g2d.drawRoundRect(50, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				if(game2Hover && FileHandler.hasSave(2)) {
					g2d.setColor(Color.GRAY);
					g2d.fillRoundRect(50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				}
				g2d.setColor(Color.WHITE);
				if(!FileHandler.hasSave(2)) g2d.setColor(Color.GRAY);
				g2d.drawRoundRect(50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				if(game3Hover && FileHandler.hasSave(3)) {
					g2d.setColor(Color.GRAY);
					g2d.fillRoundRect(50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				}
				g2d.setColor(Color.WHITE);
				if(!FileHandler.hasSave(3)) g2d.setColor(Color.GRAY);
				g2d.drawRoundRect(50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2, (GameInfo.WIDTH * 2) / 3 - 50, GameInfo.HEIGHT * 2 - 200, 20, 20);
				
				g2d.setColor(Color.WHITE);
				if(!FileHandler.hasSave(1)) g2d.setColor(Color.GRAY);
				g2d.drawString("Save 1", 50 + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("Save 1") / 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + g2d.getFontMetrics(buttonFont).getHeight());
				g2d.setColor(Color.WHITE);
				if(!FileHandler.hasSave(2)) g2d.setColor(Color.GRAY);
				g2d.drawString("Save 2", (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("Save 2") / 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + g2d.getFontMetrics(buttonFont).getHeight());
				g2d.setColor(Color.WHITE);
				if(!FileHandler.hasSave(3)) g2d.setColor(Color.GRAY);
				g2d.drawString("Save 3", (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("Save 3") / 2, GameInfo.HEIGHT - (GameInfo.HEIGHT * 2 - 200) / 2 + g2d.getFontMetrics(buttonFont).getHeight());
				
				if(FileHandler.hasSave(1)) {
					g2d.setColor(Color.WHITE);
					Font smallButtonFont = new Font("arial", Font.BOLD, 18);
					g2d.setFont(smallButtonFont);
					g2d.drawString("Last Played:", 50 + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth("Last Played:") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() / 2);
					g2d.drawString(FileHandler.saveLastModified(1), 50 + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth(FileHandler.saveLastModified(1)) / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() + 5);
				}else {
					g2d.setColor(Color.GRAY);
					g2d.drawString("No Save Found", 50 + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("No Save Found") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(buttonFont).getHeight() / 2);
				}
				g2d.setFont(buttonFont);
				if(FileHandler.hasSave(2)) {
					g2d.setColor(Color.WHITE);
					Font smallButtonFont = new Font("arial", Font.BOLD, 18);
					g2d.setFont(smallButtonFont);
					g2d.drawString("Last Played:", (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth("Last Played:") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() / 2);
					g2d.drawString(FileHandler.saveLastModified(2), (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth(FileHandler.saveLastModified(2)) / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() + 5);
				}else {
					g2d.setColor(Color.GRAY);
					g2d.drawString("No Save Found", (50 + (GameInfo.WIDTH * 2) / 3 - 50 + 30) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("No Save Found") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(buttonFont).getHeight() / 2);
				}
				g2d.setFont(buttonFont);
				if(FileHandler.hasSave(3)) {
					g2d.setColor(Color.WHITE);
					Font smallButtonFont = new Font("arial", Font.BOLD, 18);
					g2d.setFont(smallButtonFont);
					g2d.drawString("Last Played:", (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth("Last Played:") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() / 2);
					g2d.drawString(FileHandler.saveLastModified(3), (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(smallButtonFont).stringWidth(FileHandler.saveLastModified(3)) / 2, GameInfo.HEIGHT + g2d.getFontMetrics(smallButtonFont).getHeight() + 5);
				}else {
					g2d.setColor(Color.GRAY);
					g2d.drawString("No Save Found", (50 + ((GameInfo.WIDTH * 2) / 3 - 50) * 2 + 30 * 2) + ((GameInfo.WIDTH * 2) / 3 - 50) / 2 - g2d.getFontMetrics(buttonFont).stringWidth("No Save Found") / 2, GameInfo.HEIGHT + g2d.getFontMetrics(buttonFont).getHeight() / 2);
				}
				
				g2d.setFont(buttonFont);
				if(backHover) {
					g2d.setColor(Color.GRAY);
					g2d.fillRect(GameInfo.WIDTH - 35, GameInfo.HEIGHT * 2 - 50, 75, 40);
				}
				g2d.setColor(Color.WHITE);
				g2d.drawRect(GameInfo.WIDTH - 35, GameInfo.HEIGHT * 2 - 50, 75, 40);
				g2d.drawString("Back", GameInfo.WIDTH - g2d.getFontMetrics(buttonFont).stringWidth("Back") + 35, GameInfo.HEIGHT * 2 - g2d.getFontMetrics(buttonFont).getHeight() + 15);
			}
		}else if(GameInfo.gameState == GameInfo.GameState.LOADING) {
			Font font = new Font("arial", Font.BOLD, 36);
			g2d.setFont(font);
			g2d.setColor(Color.WHITE);
			g2d.drawString(loadingText, GameInfo.WIDTH - g2d.getFontMetrics(font).stringWidth(loadingText) / 2, GameInfo.HEIGHT - 75);
			
			g2d.setColor(Color.GRAY);
			g2d.drawRect(GameInfo.WIDTH - GameInfo.WIDTH / 2, GameInfo.HEIGHT - 20, GameInfo.WIDTH, 40);
			g2d.setColor(Color.WHITE);
			g2d.fillRect(GameInfo.WIDTH - GameInfo.WIDTH / 2, GameInfo.HEIGHT - 20,  (int)(((double)Game.FH.getCurrentLine() / (double)Game.FH.getMaxLines()) * GameInfo.WIDTH), 40);
			Font font2 = new Font("arial", Font.BOLD, 16);
			g2d.setFont(font2);
			g2d.drawString(Game.FH.getCurrentLine() + "/" + Game.FH.getMaxLines(), GameInfo.WIDTH - g2d.getFontMetrics(font2).stringWidth(Game.FH.getCurrentLine() + "/" + Game.FH.getMaxLines()) / 2, GameInfo.HEIGHT + 50);
		}
	}
	
}
