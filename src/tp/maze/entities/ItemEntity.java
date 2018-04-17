package tp.maze.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

import tp.maze.game.CellType;
import tp.maze.game.Maze;
import tp.maze.game.Particle;
import tp.maze.items.Item;
import tp.maze.main.GameInfo;

public class ItemEntity extends Entity {

	public ItemEntity(Maze maze, double xPos, double yPos, Player player, Item item) {
		super(maze, xPos, yPos);
		this.player = player;
		this.item = item;
		Random rnd = new Random();
		setVelX(-0.05 + (0.05 - -0.05) * rnd.nextDouble());
		setVelY(-0.05 + (0.05 - -0.05) * rnd.nextDouble());
		if(getVelX() > 0) {
			velXIncrease = true;
		}else {
			setVelX(getVelX() * -1);
		}
		if(getVelY() > 0) {
			velYIncrease = true;
		}else {
			setVelY(getVelY() * -1);
		}
		maze.items.add(this);
	}

	private Player player;
	private Item item;
	
	public Item getItem() {
		return item;
	}
	
	private boolean velXIncrease = false;
	private boolean velYIncrease = false;
	
	private int ticksAlive = 0;
	
	/**
	 * The update method to determind an item's speed loss when it spawns
	 */
	public void tick() {
		if(getMaze().getCell((int) (getXPos() + getVelX()), (int)getYPos()).getType() == CellType.WALL || getMaze().getCell((int) (getXPos() + getVelX() - 0.01), (int)(getYPos() + ((GameInfo.currentTileSize * 0.26) / GameInfo.currentTileSize))).getType() == CellType.WALL) {
			velXIncrease = !velXIncrease;
		}
		if(getMaze().getCell((int) (getXPos() + ((GameInfo.currentTileSize * 0.26) / GameInfo.currentTileSize) + getVelX() + 0.01), (int)getYPos()).getType() == CellType.WALL || getMaze().getCell((int) (getXPos() + ((GameInfo.currentTileSize * 0.26) / GameInfo.currentTileSize) + getVelX() + 0.01), (int)(getYPos() + 20d / 75d)).getType() == CellType.WALL) {
			velXIncrease = !velXIncrease;
		}
		if(getMaze().getCell((int) getXPos(), (int) (getYPos() + getVelY())).getType() == CellType.WALL || getMaze().getCell((int)(getXPos() + ((GameInfo.currentTileSize * 0.26) / GameInfo.currentTileSize)), (int) (getYPos() + getVelY())).getType() == CellType.WALL) {
			velYIncrease = !velYIncrease;
		}
		if(getMaze().getCell((int) getXPos(), (int) (getYPos() + ((GameInfo.currentTileSize * 0.26) / GameInfo.currentTileSize) + getVelY() + 0.01)).getType() == CellType.WALL || getMaze().getCell((int)(getXPos() + ((GameInfo.currentTileSize * 0.26) / GameInfo.currentTileSize)), (int) (getYPos() + ((GameInfo.currentTileSize * 0.26) / GameInfo.currentTileSize) + getVelY() + 0.01)).getType() == CellType.WALL) {
			velYIncrease = !velYIncrease;
		}
		if(velXIncrease == true) {
			setXPos(getXPos() + getVelX());
		}else {
			setXPos(getXPos() - getVelX());
		}
		if(velYIncrease == true) {
			setYPos(getYPos() + getVelY());
		}else {
			setYPos(getYPos() - getVelY());
		}
		if(getVelX() > 0) setVelX(getVelX() - 0.001);
		if(getVelY() > 0) setVelY(getVelY() - 0.001);
		if(getVelX() < 0) setVelX(0);
		if(getVelY() < 0) setVelY(0);
		setVelX(Math.round(getVelX() * 1000.0) / 1000.0);
		setVelY(Math.round(getVelY() * 1000.0) / 1000.0);
		
		ticksAlive++;
		if(ticksAlive % 60 == 0) {
			Random rnd = new Random();
			new Particle(getMaze(), player, getXPos() + 0.05, getYPos() + 0.05,  -0.02 + (0.02 - -0.02) * rnd.nextDouble(), -0.02 + (0.02 - -0.02) * rnd.nextDouble(), 5, getItem().getRarity().getColour());
		}
		if(ticksAlive >= 60 * 60) {
			die();
		}
	}
	
	private boolean alive = true;
	public boolean isAlive() {
		return alive;
	}
	
	public void die() {
		alive = false;
	}
	
	public void render(Graphics g) {
		int playerX = (int)(getXPos() * GameInfo.currentTileSize) + (int)((player.getXPos() * -1) * GameInfo.currentTileSize) + GameInfo.WIDTH - 15;
		int playerY =  (int)(getYPos() * GameInfo.currentTileSize) + (int)((player.getYPos() * -1) * GameInfo.currentTileSize) + GameInfo.HEIGHT - 15;
		final int width = (int)Math.ceil(GameInfo.currentTileSize * 0.26);
		final int height = (int)Math.ceil(GameInfo.currentTileSize * 0.26);
		if(GameInfo.BOUNDING_BOXES) {
			g.setColor(Color.BLUE);
			g.drawRect(playerX, playerY, width, height);
		}
		
		g.drawImage(item.getImage(), playerX, playerY, width, height, GameInfo.FRAME);
		if(getXPos() > player.getXPos() - 0.5 && getXPos() < player.getXPos() + 0.5 && getYPos() > player.getYPos() - 0.5 && getYPos() < player.getYPos() + 0.5) {
			g.setFont(new Font("Arial", Font.PLAIN, 14));
			g.setColor(Color.WHITE);
			g.drawString(item.getName(), playerX - (g.getFontMetrics(g.getFont()).stringWidth(item.getName()) / 2) + 10, playerY);
			player.setNearItem(true);
		}
	}
	
}
