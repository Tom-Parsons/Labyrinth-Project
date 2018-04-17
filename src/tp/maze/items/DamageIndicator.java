package tp.maze.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import tp.maze.entities.Player;
import tp.maze.game.Maze;
import tp.maze.main.GameInfo;

public class DamageIndicator {

	public DamageIndicator(Maze maze, Player player, String text, DamageType type, double x, double y) {
		this.maze = maze;
		this.player = player;
		this.text = text;
		colour = type.getColour();
		this.x = x;
		this.y = y;
		maze.damageIndicators.add(this);
	}
	
	private Maze maze;
	private Player player;
	private String text;
	private Color colour;
	private double x, y;
	
	private int ticks = 0;
	public void tick() {
		colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha() - 5);
		if(colour.getAlpha() == 0) {
			maze.damageIndicators.remove(this);
		}
		ticks++;
		if(ticks % 1 == 0) {
			y -= 0.01; 
			ticks = 0;
		}
	}
	
	public void render(Graphics g) {
		int playerX = (int)(x * 75) + (int)((player.getXPos() * -1) * 75) + GameInfo.WIDTH - 15;
		int playerY =  (int)(y * 75) + (int)((player.getYPos() * -1) * 75) + GameInfo.HEIGHT - 15; 
		g.setColor(colour);
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.drawString(text, playerX, playerY);
	}
	
}
