package tp.maze.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import tp.maze.entities.Player;
import tp.maze.main.GameInfo;

public class Particle {

	public Particle(Maze maze, Player player, double posX, double posY,  double velX, double velY, int size, Color colour) {
		this.maze = maze;
		this.player = player;
		this.posX = posX;
		this.posY = posY;
		if(velX > 0) {
			velXIncrease = true;
		}else {
			velX *= -1;
		}
		if(velY > 0) {
			velYIncrease = true;
		}else {
			velY *= -1;
		}
		this.velX = velX;
		this.velY = velY;
		this.size = size;
		this.colour = colour;
		this.maze.particles.add(this);
	}
	
	private boolean readyToDie = false;
	private boolean dying = false;
	private boolean alive = true;
	public boolean isAlive() {
		return alive;
	}
	
	private boolean velXIncrease = false;
	private boolean velYIncrease = false;
	
	public void tick() {
		ArrayList<HashMap<Double, Double>> removeLocations = new ArrayList<HashMap<Double, Double>>();
		for(HashMap<Double, Double> location : fadeLocations.keySet()) {
			Color c = fadeLocations.get(location);
			c = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha() - 1);
			fadeLocations.put(location, c);
			if(c.getAlpha() == 0) {
				removeLocations.add(location);
			}
		}
		for(HashMap<Double, Double> location : removeLocations) {
			fadeLocations.remove(location);
		}
		if(readyToDie == true && fadeLocations.size() == 0) {
			alive = false;
		}
		/*ticksAlive++;
		if(ticksAlive >= 60 * 5) {
			colour = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha() - 1);
			if(colour.getAlpha() == 0) {
				this.maze.particles.remove(this);
			}
		}*/
		if(velX > 0 || velY > 0 || velX < 0 || velY < 0) {
			HashMap<Double, Double> currentLocation = new HashMap<Double, Double>();
			currentLocation.put(posX, posY);
			fadeLocations.put(currentLocation, new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), 150));
			readyToDie = true;
		}
		if(dying == false) {
			if(maze.getCell((int) (getXPos() + getVelX()), (int)getYPos()).getType() == CellType.WALL || maze.getCell((int) (getXPos() + getVelX() - 0.01), (int)(getYPos() + (size / 75d))).getType() == CellType.WALL) {
				velXIncrease = !velXIncrease;
			}
			if(maze.getCell((int) (getXPos() + (size / 75d) + getVelX() + 0.01), (int)getYPos()).getType() == CellType.WALL || maze.getCell((int) (getXPos() + (size / 75d) + getVelX() + 0.01), (int)(getYPos() + size / 75d)).getType() == CellType.WALL) {
				velXIncrease = !velXIncrease;
			}
			if(maze.getCell((int) getXPos(), (int) (getYPos() + getVelY())).getType() == CellType.WALL || maze.getCell((int)(getXPos() + (size / 75d)), (int) (getYPos() + getVelY())).getType() == CellType.WALL) {
				velYIncrease = !velYIncrease;
			}
			if(maze.getCell((int) getXPos(), (int) (getYPos() + (size / 75d) + getVelY() + 0.01)).getType() == CellType.WALL || maze.getCell((int)(getXPos() + (size / 75d)), (int) (getYPos() + (size / 75d) + getVelY() + 0.01)).getType() == CellType.WALL) {
				velYIncrease = !velYIncrease;
			}
			if(velXIncrease == true) {
				posX +=  velX;
			}else {
				posX -= velX;
			}
			if(velYIncrease == true) {
				posY += velY;
			}else {
				posY -= velY;
			}
			if(velX > 0) velX -= 0.001;
			if(velY > 0) velY -= 0.001;
			velX = Math.round(velX * 1000.0) / 1000.0;
			velY = Math.round(velY * 1000.0) / 1000.0;
			if(velX <= 0 && velY <= 0) dying = true;
		}
	}
	
	public void render(Graphics g) {
		if(isAlive()) {
			for(HashMap<Double,  Double> location : fadeLocations.keySet()) {
				for(double x : location.keySet()) {
					double y = location.get(x);
					Color c = fadeLocations.get(location);
					g.setColor(c);
					g.fillOval((int)(x * GameInfo.currentTileSize) + (int)((player.getXPos() * -1) * GameInfo.currentTileSize) + GameInfo.WIDTH - 15, (int)(y * GameInfo.currentTileSize) + (int)((player.getYPos() * -1) * GameInfo.currentTileSize) + GameInfo.HEIGHT - 15, size, size);
				}
			}
		}
		if(readyToDie == false && dying == false && isAlive()) {
			g.setColor(colour);
			int playerX = (int)(getXPos() * GameInfo.currentTileSize) + (int)((player.getXPos() * -1) * GameInfo.currentTileSize) + GameInfo.WIDTH - 15;
			int playerY =  (int)(getYPos() * GameInfo.currentTileSize) + (int)((player.getYPos() * -1) * GameInfo.currentTileSize) + GameInfo.HEIGHT - 15;
			g.fillOval(playerX, playerY, size, size);
		}
	}
	
	private HashMap<HashMap<Double, Double>, Color> fadeLocations = new HashMap<HashMap<Double, Double>, Color>();
	
	private Maze maze;
	private Player player;
	private double posX;
	private double posY;
	private double velX;
	private double velY;
	private int size;
	private Color colour;
	
	public double getXPos() {
		return posX;
	}
	public double getYPos() {
		return posY;
	}
	public double getVelX() {
		return velX;
	}
	public double getVelY() {
		return velY;
	}
	
}
