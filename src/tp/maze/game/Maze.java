package tp.maze.game;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import tp.maze.entities.ItemEntity;
import tp.maze.entities.Monster;
import tp.maze.entities.Player;
import tp.maze.items.Chest;
import tp.maze.items.DamageIndicator;
import tp.maze.items.Items;

public class Maze {

	//Cell[][] cells;
	//public HashMap<HashMap<Integer, Integer>, Cell> cells = new HashMap<HashMap<Integer, Integer>, Cell>();
	public ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	public ArrayList<Monster> monsters = new ArrayList<Monster>();
	public ArrayList<Particle> particles = new ArrayList<Particle>();
	public ArrayList<DamageIndicator> damageIndicators = new ArrayList<DamageIndicator>();
	public ArrayList<ItemEntity> items = new ArrayList<ItemEntity>();
	public ArrayList<Chest> chests = new ArrayList<Chest>();
	public ArrayList<Torch> torches = new ArrayList<Torch>();
	
	private Player player;
	
	public Maze(Player p) {
		this.player = p;
		Random rnd = new Random();
		Chunk center = new Chunk(this, 5000, 5000, rnd.nextInt(13 - 2 + 1) + 2, rnd.nextInt(13 - 2 + 1) + 2,rnd.nextInt(13 - 2 + 1) + 2,rnd.nextInt(13 - 2 + 1) + 2, player);
		chunks.add(center);
		player.setChunk(center);
		p.setXPos(80007.5);
		p.setYPos(80007.5);
		
		Items.setupItems();
	}
	
	public Cell getCell(int x, int y) {
		for(Chunk c : chunks) {
			for(int newX = 0; newX < 16; newX++) {
				for(int newY = 0; newY < 16; newY++) {
					if(c.getCells()[newX][newY].getX() == x && c.getCells()[newX][newY].getY() == y) {
						return c.getCells()[newX][newY];
					}
				}
			}
		}
		return null;
	}
	
	public Chunk getChunk(int x, int y) {
		for(Chunk c : chunks) {
			if(c.getX() == x && c.getY() == y) {
				return c;
			}
		}
		return null;
	}
	
	public void addChunk(Chunk c) {
		chunks.add(c);
	}
	
	public void tick() {
		ArrayList<Particle> deadParticles = new ArrayList<Particle>();
		ArrayList<ItemEntity> deadItems = new ArrayList<ItemEntity>();
		//Surround with a try-catch incase a particle is made during the for loop
		try {
			for(Particle particle : particles) {
				particle.tick();
				if(!particle.isAlive()) deadParticles.add(particle);
			}
			for(Particle particle : deadParticles) particles.remove(particle);
			for(Monster m : monsters) m.tick();
			for(DamageIndicator di : damageIndicators) di.tick();
			for(ItemEntity i : items) {
				i.tick();
				if(!i.isAlive()) deadItems.add(i);
			}
			for(ItemEntity i : deadItems) items.remove(i);
		}catch(Exception ex) {}
	}
	
	public void render(Graphics g) {
		ArrayList<Chunk> removeChunks = new ArrayList<Chunk>();
		for(Chunk chunk : chunks) {
			if(chunk.getX() >= player.getChunk().getX() - 1 && chunk.getX() <= player.getChunk().getX() + 1 && chunk.getY() >= player.getChunk().getY() - 1 && chunk.getY() <= player.getChunk().getY() + 1) {
				for(int x = 0; x < 16; x++) {
					for(int y = 0; y < 16; y++) {
						chunk.getCells()[x][y].render(g);
					}
				}
			}
			if(chunk.getX() <= player.getChunk().getX() - 5
					|| chunk.getX() >= player.getChunk().getX() + 5
					|| chunk.getY() <= player.getChunk().getY() - 5
					|| chunk.getY() >= player.getChunk().getY() + 5) {
				removeChunks.add(chunk);
			}
		}
		for(Chunk c : removeChunks) {
			ArrayList<Torch> removeTorches = new ArrayList<Torch>();
			for(Torch t : torches) {
				if(getCell((int)t.getX(), (int)t.getY()).getChunk() == c) {
					removeTorches.add(t);
				}
			}
			ArrayList<Chest> removeChests = new ArrayList<Chest>();
			for(Chest ch : chests) {
				if(getCell((int)ch.getX(), (int)ch.getY()).getChunk() == c) {
					removeChests.add(ch);
				}
			}
			for(Torch t : removeTorches) torches.remove(t);
			for(Chest ch : removeChests) chests.remove(ch);
			chunks.remove(c);
		}
		//Surround with a try-catch incase a particle is made during the for loop
		try {
			for(Particle particle : particles) particle.render(g);
			for(Monster m : monsters) {
				if(((m.getXPos() - player.getCellX() > -20) && (m.getXPos() - player.getCellX() < 20)) ||((m.getYPos() - player.getCellY() > -20) && (m.getYPos() - player.getCellY() < 20))) {
					m.render(g);
				}
			}
			for(DamageIndicator di : damageIndicators) di.render(g);
			for(ItemEntity i : items) i.render(g);
		}catch(Exception ex) {}
		
		if(!player.getInventory().hasItem("Chest_Locator")) {
			for(Chest chest : chests) {
				chest.render(g);
			}
		}
		
		for(Chunk chunk : chunks) {
			if(chunk.getX() >= player.getChunk().getX() - 1 && chunk.getX() <= player.getChunk().getX() + 1 && chunk.getY() >= player.getChunk().getY() - 1 && chunk.getY() <= player.getChunk().getY() + 1) {
				for(int x = 0; x < 16; x++) {
					for(int y = 0; y < 16; y++) {
						chunk.getCells()[x][y].renderDarkness(g);
					}
				}
			}
		}
		
		if(player.getInventory().hasItem("Chest_Locator")) {
			for(Chest chest : chests) {
				chest.render(g);
			}
		}
		
		for(Torch t : torches) {
			if(getCell((int)t.getX(), (int)t.getY()).getChunk().getX() >= player.getChunk().getX() - 1 && getCell((int)t.getX(), (int)t.getY()).getChunk().getX() <= player.getChunk().getX() + 1 && getCell((int)t.getX(), (int)t.getY()).getChunk().getY() >= player.getChunk().getY() - 1 && getCell((int)t.getX(), (int)t.getY()).getChunk().getY() <= player.getChunk().getY() + 1) {
				t.render(g);
			}
		}
	}
	
}
