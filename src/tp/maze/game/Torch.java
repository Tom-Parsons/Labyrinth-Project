package tp.maze.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import tp.maze.entities.Player;
import tp.maze.main.BufferedImageLoader;
import tp.maze.main.GameInfo;

public class Torch {

	public Torch(Player player, Maze maze, double X, double Y) {
		this.player = player;
		this.maze = maze;
		this.x = X;
		this.y = Y;
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			image = loader.loadImage("/Torch.png");
		} catch (Exception e) {
			try {
				image =  loader.loadImage("/Missing_Texture.png");
			} catch (IOException e1) {} 
		}
	}
	
	private Player player;
	private Maze maze;
	private double x;
	private double y;
	private BufferedImage image;
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public void render(Graphics g) {
		try{maze.getCell((int)x, (int)y).setDarkness(0);}catch(Exception ex) {}
		Graphics2D g2d = (Graphics2D)g;
		int playerX = (int)(getX() * GameInfo.currentTileSize) + (int)((player.getXPos() * -1) * GameInfo.currentTileSize) + GameInfo.WIDTH - 15;
		int playerY =  (int)(getY() * GameInfo.currentTileSize) + (int)((player.getYPos() * -1) * GameInfo.currentTileSize) + GameInfo.HEIGHT - 15;
		
		if(GameInfo.BOUNDING_BOXES) {
			g2d.setColor(Color.BLUE);
			g2d.drawRect(playerX, playerY, (int)Math.ceil(GameInfo.currentTileSize * 0.6), (int)Math.ceil(GameInfo.currentTileSize * 0.7));
		}
		
		g2d.drawImage(image, playerX, playerY, (int)Math.ceil(GameInfo.currentTileSize * 0.6), (int)Math.ceil(GameInfo.currentTileSize * 0.7), GameInfo.FRAME);
	}
	
}
