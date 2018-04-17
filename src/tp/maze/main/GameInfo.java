package tp.maze.main;

import java.util.Random;

import javax.swing.JFrame;

import tp.maze.entities.Player;
import tp.maze.game.Maze;

public class GameInfo {
	
	/**
	 * Information about the game and different testing functions
	 */
	
	public final static int WIDTH = 415;
	public final static int HEIGHT = 315;
	public final static int SCALE = 2;	
	public final static String TITLE = "The Labyrinth";	
	public final static double TICKS = 60.0;
	
	//USED FOR TESTING
	public static boolean COLLISIONS = true;
	public static boolean LIGHTING = true;
	public static boolean USER_INTERFACE = true;
	public static boolean ENABLE_ENEMIES = true;
	public static boolean BOUNDING_BOXES = false;
	public static boolean ENEMY_AI = true;
	
	public static int maxTileSize = 75;
	public static int currentTileSize = 75;

	public final static Random RANDOM = new Random();
	
	public static boolean PAUSED = false;
	public static Player PLAYER;
	public static Maze MAZE;
	
	public static JFrame FRAME;
	
	public enum GameState{
		GAME,
		MENU,
		LOADING
	}
	public static GameState gameState;
	
}
