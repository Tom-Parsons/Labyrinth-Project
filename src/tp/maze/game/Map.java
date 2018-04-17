package tp.maze.game;

import java.awt.Color;
import java.awt.Graphics;

import tp.maze.entities.Monster;
import tp.maze.entities.Player;
import tp.maze.main.GameInfo;

public class Map {

	public static void init(Player p, Maze m) {
		player = p;
		maze = m;
	}
	
	private static Player player;
	private static Maze maze;
	
	public static void render(Graphics g) {
		if(player.hasMap() && player.isViewingMap()) {
			Chunk playerChunk = player.getChunk();
			int chunkRelativeX = 0;
			int chunkRelativeY = 0;
			for(int chunkX = playerChunk.getX() - 1; chunkX <= playerChunk.getX() + 1; chunkX++) {
				for(int chunkY = playerChunk.getY() - 1; chunkY <= playerChunk.getY() + 1; chunkY++) {
					if(chunkX == playerChunk.getX() - 1) chunkRelativeX = 0;
					if(chunkX == playerChunk.getX()) chunkRelativeX = 1;
					if(chunkX == playerChunk.getX() + 1) chunkRelativeX = 2;
					if(chunkY == playerChunk.getY() - 1) chunkRelativeY = 0;
					if(chunkY == playerChunk.getY()) chunkRelativeY = 1;
					if(chunkY == playerChunk.getY() + 1) chunkRelativeY = 2;
					try {
						Chunk c = maze.getChunk(chunkX, chunkY);
						for(int x = 0; x < 16; x++) {
							for(int y = 0; y < 16; y++) {
								Cell cell = c.getCells()[x][y];
								if(cell != null) {
									if(cell.isDiscovered() && cell.getType() == CellType.PATH) {
										g.setColor(Color.WHITE);
									}else {
										g.setColor(Color.BLACK);
									}
								}else {
									g.setColor(Color.BLACK);
								}
								g.fillRect((10) + (x * 12) + ((chunkRelativeX * 16) * 12), 10 + (y * 12) + ((chunkRelativeY * 16) * 12), 12, 12);
							}
						}
						if(chunkX == playerChunk.getX() && chunkY == playerChunk.getY()) {
							double px = player.getXPos() - (16 * c.getX());
							double py = player.getYPos() - (16 * c.getY());
							px = (px * 1000d) / 1000d;
							py = (py * 1000d) / 1000d;
							g.drawImage(player.currentImage(), (int)(10 + (16 * 12) + (px * 12)), (int)(10 + (16 * 12) + (py * 12)), 5, 5, GameInfo.FRAME);
						}
						for(Monster m : maze.monsters) {
							if(m.getChunk() == c) {
								if(maze.getCell(m.getCellX(), m.getCellY()).isDiscovered()) {
									double mx = m.getXPos() - (16 * c.getX());
									double my = m.getYPos() - (16 * c.getY());
									g.drawImage(m.getCurrentImage(), (int)(10 + ((chunkRelativeX * 16) * 12) + (mx * 12)), (int)(10 + ((chunkRelativeY * 16) * 12) + (my * 12)), 5, 5, GameInfo.FRAME);
								}
							}
						}
						for(Torch t : maze.torches) {
							if(maze.getCell((int)t.getX(), (int)t.getY()).getChunk() == c) {
								if(maze.getCell((int)t.getX(), (int)t.getY()).isDiscovered()) {
									double mx = t.getX() - (16 * c.getX());
									double my = t.getY() - (16 * c.getY());
									//g.drawImage(m.getCurrentImage(), (int)(10 + ((chunkRelativeX * 16) * 12) + (mx * 12)), (int)(10 + ((chunkRelativeY * 16) * 12) + (my * 12)), 5, 5, Game.frame);
									g.setColor(Color.ORANGE);
									g.fillOval((int)(10 + ((chunkRelativeX * 16) * 12) + (mx * 12)), (int)(10 + ((chunkRelativeY * 16) * 12) + (my * 12)), 3, 6);
								}
							}
						}
					}catch(Exception ex) {}
				}
			}
			
			g.setColor(Color.GRAY);
			g.drawRect(10, 10, (16 * 12) * 3, (16 * 12) * 3);
		}
	}
	
}
