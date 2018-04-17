package tp.maze.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import tp.maze.entities.Player;
import tp.maze.game.Maze;
import tp.maze.main.BufferedImageLoader;
import tp.maze.main.GameInfo;

public class Chest {

	public Chest(int level, double X, double Y, Player p, Maze m) {
		this.level = level;
		this.x = X;
		this.y = Y;
		this.player = p;
		this.maze = m;
		this.maze.chests.add(this);
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			image = loader.loadImage("/Chest_Closed.png");
		} catch (Exception e) {
			try {
				image =  loader.loadImage("/Missing_Texture.png");
			} catch (IOException e1) {} 
		}
	}
	
	public void Open() {
		Random rnd = new Random();
		int numberOfItemsChance = rnd.nextInt(100 - 0 + 1) + 0;
		int numberOfItems = 1;
		if(numberOfItemsChance <= 10) {
			numberOfItems = 0;
		}else if(numberOfItemsChance <= 50) {
			numberOfItems = 1;
		}else if(numberOfItemsChance <= 90) {
			numberOfItems = 2;
		}else {
			numberOfItems = 3;
		}
		for(int i = 0; i < numberOfItems; i++) {
			Items.spawnRandomItem(maze, player, x, y, getLevel());
		}
	}
	
	private Player player;
	private Maze maze;
	private int level;
	private double x;
	private double y;
	private BufferedImage image;
	
	public int getLevel() {
		return level;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		int playerX = (int)(getX() * GameInfo.currentTileSize) + (int)((player.getXPos() * -1) * GameInfo.currentTileSize) + GameInfo.WIDTH - 15;
		int playerY =  (int)(getY() * GameInfo.currentTileSize) + (int)((player.getYPos() * -1) * GameInfo.currentTileSize) + GameInfo.HEIGHT - 15;
		
		if(GameInfo.BOUNDING_BOXES) {
			g2d.setColor(Color.BLUE);
			g2d.drawRect(playerX, playerY, (int)Math.ceil(GameInfo.currentTileSize * 0.32), (int)Math.ceil(GameInfo.currentTileSize * 0.32));
		}
		
		g2d.drawImage(image, playerX, playerY, (int)Math.ceil(GameInfo.currentTileSize * 0.32), (int)Math.ceil(GameInfo.currentTileSize * 0.32), GameInfo.FRAME);
	}
	
}
