package tp.maze.game;

import java.awt.Color;
import java.awt.Graphics;

import tp.maze.entities.Player;
import tp.maze.main.Game;
import tp.maze.main.GameInfo;

public class Cell {

	private Maze maze;
	private int x;
	private int y;
	private CellType type;
	private Chunk chunk;
	private int darkness = 255;
	private boolean hasLight = false;
	private boolean discovered = false;
	
	private Player player;
	
	public Cell(Maze maze, int x, int y, CellType type, boolean hasLight, Chunk chunk, Player player) {
		this.maze = maze;
		this.x = x;
		this.y = y;
		this.type = type;
		this.hasLight = hasLight;
		this.chunk = chunk;
		this.player = player;
	}
	
	public void setType(CellType type) {
		this.type = type;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public CellType getType() {
		return type;
	}
	public Chunk getChunk() {
		return chunk;
	}
	
	public int getDarkness() {
		return darkness;
	}
	public void setDarkness(int darkness) {
		this.darkness = darkness;
		if(getDarkness() < 0) this.darkness = 0;
		if(getDarkness() > 255) this.darkness = 255;
	}
	public void setHasLight(boolean hasLight) {
		this.hasLight = hasLight;
	}
	public boolean doesHaveLight() {
		return hasLight;
	}
	public boolean isDiscovered() {
		return discovered;
	}
	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}
	
	public void renderDarkness(Graphics g) {
		if(!GameInfo.LIGHTING) return;
		if((player.getCellX() == x && player.getCellY() == y)) setDarkness(0);
		if(getDarkness() < 200) {
			setDiscovered(true);
		}else {
			if(isDiscovered() && !player.hasEnchantedMap()) {
				setDiscovered(false);
			}
		}
		if(((x - player.getCellX() > -7) && (x - player.getCellX() < 7)) ||((y - player.getCellY() > -7) && (y - player.getCellY() < 7))) {
			if(getType() == CellType.PATH) {
				try{
					g.setColor(new Color(0, 0, 0, getDarkness()));
				}catch(Exception ex) {
					System.out.println(getDarkness());
					ex.printStackTrace();
				}
				g.fillRect((x * GameInfo.currentTileSize) + (int)((player.getXPos() * -1) * GameInfo.currentTileSize) + GameInfo.WIDTH - 15, (y * GameInfo.currentTileSize) + (int)((player.getYPos() * -1) * GameInfo.currentTileSize) + GameInfo.HEIGHT - 15, GameInfo.currentTileSize, GameInfo.currentTileSize);
				if(getDarkness() < 255) {
					Cell NORTH = maze.getCell(x, y - 1);
					Cell SOUTH = maze.getCell(x, y + 1);
					Cell EAST = maze.getCell(x + 1, y);
					Cell WEST = maze.getCell(x - 1, y);
					if(NORTH != null) {
						if(NORTH.getType() == CellType.PATH) {
							if(NORTH.getDarkness() > getDarkness()) NORTH.setDarkness(getDarkness() + 50);
						}
					}
					if(SOUTH != null) {
						if(SOUTH.getType() == CellType.PATH) {
							if(SOUTH.getDarkness() > getDarkness()) SOUTH.setDarkness(getDarkness() + 50);
						}
					}
					if(EAST != null) {
						if(EAST.getType() == CellType.PATH) {
							if(EAST.getDarkness() > getDarkness()) EAST.setDarkness(getDarkness() + 50);
						}
					}
					if(WEST != null) {
						if(WEST.getType() == CellType.PATH) {
							if(WEST.getDarkness() > getDarkness()) WEST.setDarkness(getDarkness() + 50);
						}
					}
					setDarkness(255);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		int playerX = (x * GameInfo.currentTileSize) + (int)((player.getXPos() * -1) * GameInfo.currentTileSize) + GameInfo.WIDTH - 15;
		int playerY = (y * GameInfo.currentTileSize) + (int)((player.getYPos() * -1) * GameInfo.currentTileSize) + GameInfo.HEIGHT - 15;
		if(((x - player.getCellX() > -7) && (x - player.getCellX() < 7)) ||((y - player.getCellY() > -7) && (y - player.getCellY() < 7))) {
			if(getType() == CellType.PATH) {
				g.setColor(Color.GRAY);
				g.drawImage(Game.gravelPath, (x * GameInfo.currentTileSize) + (int)((player.getXPos() * -1) * GameInfo.currentTileSize) + GameInfo.WIDTH - 15, (y * GameInfo.currentTileSize) + (int)((player.getYPos() * -1) * GameInfo.currentTileSize) + GameInfo.HEIGHT - 15, GameInfo.currentTileSize, GameInfo.currentTileSize, GameInfo.FRAME);
			}else if (getType() == CellType.WALL){
				g.setColor(Color.BLACK);
				//g.fillRect((x * 75) + (int)((player.getXPos() * -1) * 75) + GameInfo.WIDTH - 15, (y * 75) + (int)((player.getYPos() * -1) * 75) + GameInfo.HEIGHT - 15, 75, 75);
			}else {
				g.setColor(Color.RED);
				g.fillRect(playerX, playerY, GameInfo.currentTileSize, GameInfo.currentTileSize);
				setType(CellType.PATH);
			}
			//if(x % 16 == 0 || y % 16 == 0) g.setColor(Color.RED);
			//Graphics2D g2d = (Graphics2D) g;
			g.setColor(Color.WHITE);
			//g2d.drawString((x - player.getCellX()) + " " + (y - player.getCellY()) + " - " + x + ", " + y, (x * 75) + (int)((player.getXPos() * -1) * 75) + GameInfo.WIDTH, (y * 75) + (int)((player.getYPos() * -1) * 75) + GameInfo.HEIGHT);
			//g.drawString(player.getX() *-1 *100  + "", x, y);
		}
	}
	
}
