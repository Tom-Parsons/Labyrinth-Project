package tp.maze.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import tp.maze.game.Cell;
import tp.maze.game.CellType;
import tp.maze.game.Maze;
import tp.maze.game.Particle;
import tp.maze.game.Pathfinding;
import tp.maze.items.Damage;
import tp.maze.items.DamageIndicator;
import tp.maze.items.DamageType;
import tp.maze.items.Items;
import tp.maze.main.BufferedImageLoader;
import tp.maze.main.GameInfo;

public class Monster extends LivingEntity {

	private MonsterType type;
	private Player player;
	
	private BufferedImage NORTH;
	private BufferedImage SOUTH;
	private BufferedImage EAST;
	private BufferedImage WEST;
	
	/**
	 * Create a new monster
	 * @param maze An instance of the main maze class
	 * @param xPos The x position of the monster that is spawning
	 * @param yPos The y position of the monster that is spawning
	 * @param player An instance of the player
	 * @param monsterType The type of monster to spawn
	 */
	public Monster(Maze maze, double xPos, double yPos, Player player, MonsterType monsterType) {
		super(maze, xPos, yPos);
		this.player = player;
		this.type = monsterType;
		
		Random rnd = new Random();
		double randomize = 0.5 + (1.6 - 0.8) * rnd.nextDouble();
		this.setSpeed(type.getbaseSpeed());
		this.setXP(0);
		this.setLevel(rnd.nextInt((player.getLevel() + 1) - (player.getLevel() - 1) + 1) + player.getLevel() - 1);
		if(getLevel() <= 0) setLevel(1);
		this.setMaxHealth(((((player.getMaxHealth() / 4) * type.getBaseHealth()) * (getLevel() * 0.8)) * randomize));
		this.setHealth(this.getMaxHealth());
		int neutralDamage = (int)Math.round((type.getBaseDamage() * getLevel()) * randomize) + 1;
		int fireDamage = 0;
		int waterDamage = 0;
		int earthDamage = 0;
		int airDamage = 0;
		if(this.getType().getResistance() == DamageType.FIRE) fireDamage = neutralDamage / 2;
		if(this.getType().getResistance() == DamageType.WATER) waterDamage = neutralDamage / 2;
		if(this.getType().getResistance() == DamageType.EARTH) earthDamage = neutralDamage / 2;
		if(this.getType().getResistance() == DamageType.AIR) airDamage = neutralDamage / 2;
		this.setDamage(new Damage(neutralDamage, fireDamage, waterDamage, earthDamage, airDamage));
		
		loadImages();
	}
	
	/**
	 * Create a monster that is being loaded from a file so it isn't necessary to generate random information about the monster
	 * @param maze
	 * @param health
	 * @param maxHealth
	 * @param xPos
	 * @param yPos
	 * @param direction
	 * @param level
	 * @param player
	 * @param monsterType
	 */
	public Monster(Maze maze, double health, double maxHealth, double xPos, double yPos, String direction, int level, Player player, MonsterType monsterType) {
		super(maze, xPos, yPos);
		this.player = player;
		this.type = monsterType;
		
		this.setMaxHealth(maxHealth);
		this.setHealth(health);
		this.setSpeed(type.getbaseSpeed());
		this.setXP(0);
		this.setLevel(level);
		if(getLevel() <= 0) setLevel(1);
		Random rnd = new Random();
		double randomize = 0.5 + (1.6 - 0.8) * rnd.nextDouble();
		setDirection(direction);
		int neutralDamage = (int)Math.round((type.getBaseDamage() * getLevel()) * randomize) + 1;
		int fireDamage = 0;
		int waterDamage = 0;
		int earthDamage = 0;
		int airDamage = 0;
		if(this.getType().getResistance() == DamageType.FIRE) fireDamage = neutralDamage / 2;
		if(this.getType().getResistance() == DamageType.WATER) waterDamage = neutralDamage / 2;
		if(this.getType().getResistance() == DamageType.EARTH) earthDamage = neutralDamage / 2;
		if(this.getType().getResistance() == DamageType.AIR) airDamage = neutralDamage / 2;
		this.setDamage(new Damage(neutralDamage, fireDamage, waterDamage, earthDamage, airDamage));
		
		loadImages();
	}
	
	/**
	 * Load the images of the monster for each direction
	 */
	private void loadImages() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			NORTH = loader.loadImage("/" + type.toString() + "_NORTH.png");
		} catch (Exception e) {
			try {
				NORTH =  loader.loadImage("/Missing_Texture.png");
			} catch (IOException e1) {} 
		}
		try {
			SOUTH = loader.loadImage("/" + type.toString() + "_SOUTH.png");
		} catch (Exception e) {
			try {
				SOUTH =  loader.loadImage("/Missing_Texture.png");
			} catch (IOException e1) {} 
		}
		try {
			EAST = loader.loadImage("/" + type.toString() + "_EAST.png");
		} catch (Exception e) {
			try {
				EAST =  loader.loadImage("/Missing_Texture.png");
			} catch (IOException e1) {} 
		}
		try {
			WEST = loader.loadImage("/" + type.toString() + "_WEST.png");
		} catch (Exception e) {
			try {
				WEST =  loader.loadImage("/Missing_Texture.png");
			} catch (IOException e1) {} 
		}
	}
	
	public MonsterType getType() {
		return type;
	}
	
	private int readyPathFinding = 0;
	private Pathfinding pf = null;
	
	/**
	 * The update method that calls the Entity's tick method but also calculates the pathfinding
	 */
	@Override
	public void tick() {
		super.tick();
		if(readyPathFinding > 0) readyPathFinding--;
		if((player.getCellX() < (int) getXPos() + 7 && player.getCellX() > (int) getXPos() - 7 && player.getCellY() < (int) getYPos() + 7 && player.getCellY() > (int) getYPos() - 7)) {
			if(getMaze().getCell((int) getXPos(), (int) getYPos()) == getMaze().getCell(player.getCellX(), player.getCellY())) {
				boolean closeEnough = false;
				if(GameInfo.ENEMY_AI) {
				if(getXPos() < player.getXPos() - 0.2) {
					setVelX(getSpeed());
					closeEnough = false;
				}else if(getXPos() > player.getXPos() + 0.2) {
					setVelX(-getSpeed());
					closeEnough = false;
				}else {
					setVelX(0);
					closeEnough = true;
				}
				if(getYPos() < player.getYPos() - 0.2) {
					setVelY(getSpeed());
					closeEnough = false;
				}else if(getYPos() > player.getYPos() + 0.2) {
					setVelY(-getSpeed());
					closeEnough = false;
				}else {
					setVelY(0);
					closeEnough = true;
				}
				if(closeEnough) attack();
				}
			}else {
				if(pf == null && readyPathFinding == 0) {
					if(getMaze().getCell((int) getXPos(), (int) getYPos()) != getMaze().getCell(player.getCellX(), player.getCellY())) {
						if(GameInfo.ENEMY_AI) {
							pf = new Pathfinding(getMaze(), getMaze().getCell((int) getXPos(), (int) getYPos()), getMaze().getCell(player.getCellX(), player.getCellY()), 10);
						}else {
							int newPathChance = GameInfo.RANDOM.nextInt(100 - 0 + 1) + 0;
							if(newPathChance <= 10) {
								ArrayList<Cell> possiblePaths = new ArrayList<Cell>();
								if(getMaze().getCell(getCellX() - 1, getCellY()).getType() == CellType.PATH) 
									possiblePaths.add(getMaze().getCell(getCellX() - 1, getCellY()));
								if(getMaze().getCell(getCellX() + 1, getCellY()).getType() == CellType.PATH) 
									possiblePaths.add(getMaze().getCell(getCellX() + 1, getCellY()));
								if(getMaze().getCell(getCellX(), getCellY() - 1).getType() == CellType.PATH) 
									possiblePaths.add(getMaze().getCell(getCellX(), getCellY() - 1));
								if(getMaze().getCell(getCellX(), getCellY() + 1).getType() == CellType.PATH) 
									possiblePaths.add(getMaze().getCell(getCellX(), getCellY() + 1));
								System.out.println(possiblePaths.size() + "");
								if(possiblePaths.size() > 0) {
									int pathChoice = GameInfo.RANDOM.nextInt((possiblePaths.size() - 1) - 0 + 1) + 0;
									pf = new Pathfinding(getMaze(), getMaze().getCell((int) getXPos(), (int) getYPos()), possiblePaths.get(pathChoice), 3);
								}
							}
						}
						if(pf.hasRestarted()) readyPathFinding = 60 * 3;
					}else {
						pf = null;
						setVelX(0);
						setVelY(0);
					}
				}else {
					if(pf != null) {
						if(pf.getPath().size() >= 1) {
							if(getXPos() < pf.getPath().get(0).getX() + 0.3 - (0.35 / 2) - (getSpeed()) && getVelX() != getSpeed()) {
								setVelX(getSpeed());
							}else if(getXPos() > pf.getPath().get(0).getX() + 0.6 - (0.35 / 2) + (getSpeed()) && getVelX() != -getSpeed()) {
								setVelX(-getSpeed());
							}
							if(getYPos() < pf.getPath().get(0).getY() + 0.3 - (0.35 / 2) - (getSpeed()) && getVelY() != getSpeed()) {
								setVelY(getSpeed());
							}else if(getYPos() > pf.getPath().get(0).getY() + 0.6 - (0.35 / 2) + (getSpeed()) && getVelY() != -getSpeed()) {
								setVelY(-getSpeed());
							}
							if(getMaze().getCell((int) getXPos(), (int) getYPos()) == pf.getPath().get(0)) {
								pf = null;
								setVelX(0);
								setVelY(0);
							}
						}else {
							pf = null;
							setVelX(0);
							setVelY(0);
						}
					}else {
						setVelX(0);
						setVelY(0);
					}
				}
			}
		}else {
			pf = null;
			setVelX(0);
			setVelY(0);
		}
		setChunk(getMaze().getCell(getCellX(), getCellY()).getChunk());
	}
	
	private int attackCooldown = 60;
	public void attack() {
		if(attackCooldown == 0) {
			player.takeDamage(this.getDamage());
			attackCooldown = 60;
		}else {
			attackCooldown--;
		}
	}
	
	/**
	 * Damages the monster, but also calculates how much to take off or add depending on its weakness/resistance
	 * @param damage The damage to deal
	 */
	public void takeDamage(Damage damage) {
		double neutralDamage = damage.getNeutralDamage();
		double fireDamage = damage.getFireDamage();
		double waterDamage = damage.getWaterDamage();
		double earthDamage = damage.getEarthDamage();
		double airDamage = damage.getAirDamage();
		Random rnd = new Random();
		if(this.getType().getResistance() == DamageType.NEUTRAL) {
			if(neutralDamage > 0) {
				neutralDamage -= neutralDamage * (this.getType().getResistancePercentage() / 100d);
			}
		}else if(this.getType().getResistance() == DamageType.FIRE) {
			if(fireDamage > 0) {
				fireDamage -= fireDamage * (this.getType().getResistancePercentage() / 100d);
			}
		}else if(this.getType().getResistance() == DamageType.WATER) {
			if(waterDamage > 0) {
				waterDamage -= waterDamage * (this.getType().getResistancePercentage() / 100d);
			}
		}else if(this.getType().getResistance() == DamageType.EARTH) {
			if(earthDamage > 0) {
				earthDamage -= earthDamage * (this.getType().getResistancePercentage() / 100d);
			}
		}else if(this.getType().getResistance() == DamageType.AIR) {
			if(airDamage > 0) {
				airDamage -= airDamage * (this.getType().getResistancePercentage() / 100d);
			}
		}
		if(this.getType().getWeakness() == DamageType.NEUTRAL) {
			if(neutralDamage > 0) {
				neutralDamage += neutralDamage * (this.getType().getWeaknessPercentage() / 100d);
			}
		}else if(this.getType().getWeakness() == DamageType.FIRE) {
			if(fireDamage > 0) {
				fireDamage += fireDamage * (this.getType().getWeaknessPercentage() / 100d);
			}
		}else if(this.getType().getWeakness() == DamageType.WATER) {
			if(waterDamage > 0) {
				waterDamage += waterDamage * (this.getType().getWeaknessPercentage() / 100d);
			}
		}else if(this.getType().getWeakness() == DamageType.EARTH) {
			if(earthDamage > 0) {
				earthDamage += earthDamage * (this.getType().getWeaknessPercentage() / 100d);
			}
		}else if(this.getType().getWeakness() == DamageType.AIR) {
			if(airDamage > 0) {
				airDamage += airDamage * (this.getType().getWeaknessPercentage() / 100d);
			}
		}
		neutralDamage = Math.round(neutralDamage * 100.0) / 100.0;
		fireDamage = Math.round(fireDamage * 100.0) / 100.0;
		waterDamage = Math.round(waterDamage * 100.0) / 100.0;
		earthDamage = Math.round(earthDamage * 100.0) / 100.0;
		airDamage = Math.round(airDamage * 100.0) / 100.0;
		double x = (getXPos() - 0.5) + ((getXPos() + 0.6) - (getXPos() - 0.5)) * rnd.nextDouble();
		double y = (getYPos() - 0.3) + ((getXPos() + 0.3) - (getXPos() - 0.3)) * rnd.nextDouble();
		int increaseAmount = 0;
		if(neutralDamage > 0) {
			increaseAmount += 1;
			new DamageIndicator(getMaze(), player, "-" + ((neutralDamage * 100.0) / 100.0) + DamageType.NEUTRAL.getCharater(), DamageType.NEUTRAL, x, y);
		}
		if(fireDamage > 0) {
			new DamageIndicator(getMaze(), player, "-" + ((fireDamage * 100.0) / 100.0) + DamageType.FIRE.getCharater(), DamageType.FIRE, x, y + (increaseAmount * 0.2));
			increaseAmount += 1;
		}
		if(waterDamage > 0) {
			new DamageIndicator(getMaze(), player, "-" + ((waterDamage * 100.0) / 100.0) + DamageType.WATER.getCharater(), DamageType.WATER, x, y + (increaseAmount * 0.2));
			increaseAmount += 1;
		}
		if(earthDamage > 0) {
			new DamageIndicator(getMaze(), player, "-" + ((earthDamage * 100.0) / 100.0) + DamageType.EARTH.getCharater(), DamageType.EARTH, x, y + (increaseAmount * 0.2));
			increaseAmount += 1;
		}
		if(airDamage > 0) {
			new DamageIndicator(getMaze(), player, "-" + ((airDamage * 100.0) / 100.0) + DamageType.AIR.getCharater(), DamageType.AIR, x, y + (increaseAmount * 0.2));
		}
		setHealth(getHealth() - neutralDamage - fireDamage - waterDamage - earthDamage - airDamage);
		new Particle(getMaze(), player, getXPos() + 0.165, getYPos() + 0.165,  -0.05 + (0.05 - -0.05) * rnd.nextDouble(), -0.05 + (0.05 - -0.05) * rnd.nextDouble(), 5, Color.RED);
		new Particle(getMaze(), player, getXPos() + 0.165, getYPos() + 0.165,  -0.05 + (0.05 - -0.05) * rnd.nextDouble(), -0.05 + (0.05 - -0.05) * rnd.nextDouble(), 5, Color.RED);
	}
	
	@Override
	public void die() {
		Random rnd = new Random();
		int amountOfBlood = rnd.nextInt(8 - 4 + 1) + 3;
		for(int x = 0; x < amountOfBlood; x++) {
			new Particle(getMaze(), player, getXPos() + 0.165, getYPos() + 0.165,  -0.05 + (0.05 - -0.05) * rnd.nextDouble(), -0.05 + (0.05 - -0.05) * rnd.nextDouble(), 5, Color.RED);
		}
		int chance = rnd.nextInt(100 - 0 + 1) + 0;
		if(chance <= 50) {
			Items.spawnRandomItem(getMaze(), player, getXPos() + 0.165, getYPos() + 0.165, getLevel());
		}else if(chance <= 60) {
			Items.spawnRandomItem(getMaze(), player, getXPos() + 0.165, getYPos() + 0.165, getLevel());
			Items.spawnRandomItem(getMaze(), player, getXPos() + 0.165, getYPos() + 0.165, getLevel());
		}
	}
	
	private BufferedImage curImage = NORTH;
	public BufferedImage getCurrentImage() {
		return curImage;
	}
	
	public void render(Graphics g) {
		if(((getXPos() - player.getCellX() > -20) && (getXPos() - player.getCellX() < 20)) || ((getYPos() - player.getCellY() > -20) && (getYPos() - player.getCellY() < 20))) {
			int playerX = (int)(getXPos() * GameInfo.currentTileSize) + (int)((player.getXPos() * -1) * GameInfo.currentTileSize) + GameInfo.WIDTH - 15;
			int playerY =  (int)(getYPos() * GameInfo.currentTileSize) + (int)((player.getYPos() * -1) * GameInfo.currentTileSize) + GameInfo.HEIGHT - 15;
			final int width = (int)Math.ceil(GameInfo.currentTileSize * 0.4);
			final int height = (int)Math.ceil(GameInfo.currentTileSize * 0.4);
			
			if(GameInfo.BOUNDING_BOXES) {
				g.setColor(Color.BLUE);
				g.drawRect(playerX, playerY, width, height);
			}
			
			//g.fillOval(playerX, playerY, 30, 30);
			if(getDirection().contains("NORTH")) {
				g.drawImage(NORTH, playerX, playerY, width, height, GameInfo.FRAME);
				curImage = NORTH;
			}else if(getDirection().contains("SOUTH")) {
				g.drawImage(SOUTH, playerX, playerY, width, height, GameInfo.FRAME);
				curImage = SOUTH;
			}else if(getDirection().contains("EAST")) {
				g.drawImage(EAST, playerX, playerY, width, height, GameInfo.FRAME);
				curImage = EAST;
			}else if(getDirection().contains("WEST")) {
				g.drawImage(WEST, playerX, playerY, width, height, GameInfo.FRAME);
				curImage = WEST;
			}
			if(getHealth() < getMaxHealth()) {
				g.setColor(Color.RED);
				g.fillRect(playerX, playerY - 7, (int)Math.ceil(GameInfo.currentTileSize * 0.4), 5);
				g.setColor(Color.GREEN);
				double healthPercentage = (getHealth() / getMaxHealth()) * (int)Math.ceil(GameInfo.currentTileSize * 0.4);
				if(getHealth() > 0) {
					g.fillRect(playerX, playerY - 7, (int)healthPercentage, 5);
				}
				g.setColor(Color.GRAY);
				g.drawRect(playerX,  playerY - 7, (int)Math.ceil(GameInfo.currentTileSize * 0.4), 5);
				}
			g.setColor(Color.WHITE);
			/**
			 * Used for drawing the monster's path to follow
			 */
			//g.drawString(getXPos() + " " + getYPos(), playerX, playerY);
			//if(pf == null) g.drawString("null", playerX, playerY - 7);
			/*if(pf != null) {
				g.setColor(Color.RED);
				g.drawLine((int)(playerX + (0.2 * 75)), (int)(playerY + (0.2 * 75)), (int)((pf.getPath().get(0).getX() + 0.5) * 75) + (int)((player.getXPos() * -1) * 75) + GameInfo.WIDTH - 15, (int)((pf.getPath().get(0).getY() + 0.5) * 75) + (int)((player.getYPos() * -1) * 75) + GameInfo.HEIGHT - 15);
				Cell prevC = null;
				for(Cell c : pf.getPath()) {
					if(prevC != null) {
						g.drawLine((int)((c.getX() + 0.5) * 75) + (int)((player.getXPos() * -1) * 75) + GameInfo.WIDTH - 15, (int)((c.getY() + 0.5) * 75) + (int)((player.getYPos() * -1) * 75) + GameInfo.HEIGHT - 15, (int)((prevC.getX() + 0.5) * 75) + (int)((player.getXPos() * -1) * 75) + GameInfo.WIDTH - 15, (int)((prevC.getY() + 0.5) * 75) + (int)((player.getYPos() * -1) * 75) + GameInfo.HEIGHT - 15);
					}
					prevC = c;
				}
			}*/
		}
	}
	
}
