package tp.maze.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import tp.maze.game.Chunk;
import tp.maze.game.Maze;
import tp.maze.game.UserInterface;
import tp.maze.items.Armour;
import tp.maze.items.Chest;
import tp.maze.items.Damage;
import tp.maze.items.Inventory;
import tp.maze.items.Item;
import tp.maze.items.ItemType;
import tp.maze.items.Items;
import tp.maze.items.Potion;
import tp.maze.items.PotionType;
import tp.maze.items.Weapon;
import tp.maze.main.BufferedImageLoader;
import tp.maze.main.EventLog;
import tp.maze.main.Game;
import tp.maze.main.GameInfo;

public class Player extends LivingEntity {

	public Player(Maze maze, double xPos, double yPos) {
		super(maze, xPos, yPos);
		this.setLevel(1);
		setMaxHealth(20 + 0 + (20 * (getLevel() * 0.43)));
		this.setHealth(getMaxHealth());
		this.setSpeed(0.03);
		this.setActiveWeapon(Items.FISTS);
		this.setActiveArmour(Items.CLOTHES);
		try {
			this.inventory = new Inventory(15);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			north = loader.loadImage("/Player_North.png");
			currentPlayer = north;
		} catch (Exception e) {
			try {
				north = loader.loadImage("/Missing_Texture.png");
			}catch(Exception ex) {}
		}
		try {
			south = loader.loadImage("/Player_South.png");
		} catch (Exception e) {
			try {
				south = loader.loadImage("/Missing_Texture.png");
			}catch(Exception ex) {}
		}
		try {
			east = loader.loadImage("/Player_East.png");
		} catch (Exception e) {
			try {
				east = loader.loadImage("/Missing_Texture.png");
			}catch(Exception ex) {}
		}
		try {
			west = loader.loadImage("/Player_West.png");
		} catch (Exception e) {
			try {
				west = loader.loadImage("/Missing_Texture.png");
			}catch(Exception ex) {}
		}
	}
	
	public void init(Maze maze) {
		setMaze(maze);
	}
	
	private boolean statViewMode = false;
	private boolean viewingMap = false;
	public boolean hasMap() {
		for(Item i : getInventory().getContents()) {
			try {
			if(i.getID().equals("Map")) {
				return true;
			}
			}catch(Exception ex) {}
		}
		return false;
	}
	public boolean hasEnchantedMap() {
		for(Item i : getInventory().getContents()) {
			try {
			if(i.getID().equals("Enchanted_Map")) {
				return true;
			}
			}catch(Exception ex) {}
		}
		return false;
	}
	public boolean isViewingMap() {
		return viewingMap;
	}
	public boolean isStatViewMode() {
		return statViewMode;
	}
	public void setViewingMap(boolean viewingMap) {
		this.viewingMap = viewingMap;
	}
	public void setStatViewMode(boolean statViewMode) {
		this.statViewMode = statViewMode;
	}
		
	private Weapon activeWeapon = null;
	private Armour activeArmour = null;
	private Potion activePotion = null;
	
	public Weapon getActiveWeapon() {
		return activeWeapon;
	}
	public void setActiveWeapon(Weapon weapon) {
		this.activeWeapon = weapon;
	}
	public Armour getActiveArmour() {
		return activeArmour;
	}
	public void setActiveArmour(Armour armour) {
		this.activeArmour = armour;
	}
	public Potion getActivePotion() {
		return activePotion;
	}
	public void setActivePotion(Potion potion) {
		this.activePotion = potion;
	}
	
	private Inventory inventory;
	private Inventory openInventory;
	
	public Inventory getInventory() {
		return inventory;
	}
	public Inventory getOpenInventory() {
		return openInventory;
	}
	
	public void openInventory(Inventory i) {
		openInventory = i;
	}
	public void closeInventory() {
		openInventory = null;
	}
	
	public void pickupItem(Item i) throws Exception {
		for(int x = 0; x < getInventory().getSize(); x++) {
			if(getInventory().getItem(x).getType() == ItemType.AIR) {
				getInventory().setItem(x, i);
				break;
			}
		}
	}
	
	private Monster targettedEnemy;
	
	public Monster getTargettedEnemy() {
		return targettedEnemy;
	}
	
	public void usePotion() {
		if(activePotion != null) {
			if(activePotion.getPotionType() == PotionType.HEALTH) {
				setHealth(getHealth() + activePotion.getBonus());
				activePotion.removeUse();
				if(activePotion.getUses() == 0) {
					Items.activePotions.remove(activePotion);
					Items.activeItems.remove(activePotion);
					activePotion = null;
				}
			}
		}
	}
	
	public void attack() {
		if(attackDelay >= 40) {
			ArrayList<Chest> openedChests = new ArrayList<Chest>();
			for(Chest chest : getMaze().chests) {
				if(chest.getX() > getXPos() - 0.3 && chest.getX() < getXPos() + 0.3 && chest.getY() > getYPos() - 0.3 && chest.getY() < getYPos() + 0.3) {
					chest.Open();
					openedChests.add(chest);
				}
			}
			for(Chest chest : openedChests) {
				getMaze().chests.remove(chest);
			}
			ArrayList<Monster> deadMonsters = new ArrayList<Monster>();
			for(Monster m : getMaze().monsters) {
				if(m.getXPos() > getXPos() - 0.3 && m.getXPos() < getXPos() + 0.3 && m.getYPos() > getYPos() - 0.3 && m.getYPos() < getYPos() + 0.3) {
					m.takeDamage(getDamage());
					targettedEnemy = m;
					if(m.getHealth() <= 0) deadMonsters.add(m);
				}
			}
			for(Monster m : deadMonsters) {
				EventLog.newEvent("Killed a level " + m.getLevel() + " " + m.getType().toString().substring(0, 1) + m.getType().toString().toLowerCase().substring(1));
				setXP((int)(getXP() + m.getLevel() + (m.getLevel() / 2)));
				getMaze().monsters.remove(m);
				if(targettedEnemy == m) targettedEnemy = null;
			}
			attackDelay = 0;
		}
	}
	private int attackDelay = 0;
	
	public int getLevelXP(int level) {
		return (level * level) * (int)(level / 2);
	}
	
	public void takeDamage(Damage damage) {
		healthRegenDelay = 60 * 3;
		double neutralDamage = damage.getNeutralDamage();
		double fireDamage = damage.getFireDamage();
		double waterDamage = damage.getWaterDamage();
		double earthDamage = damage.getEarthDamage();
		double airDamage = damage.getAirDamage();
		neutralDamage -= neutralDamage * getActiveArmour().getResistances().getNeutralDamage() / 100;
		fireDamage -= fireDamage * getActiveArmour().getResistances().getFireDamage() / 100;
		waterDamage -= waterDamage * getActiveArmour().getResistances().getWaterDamage() / 100;
		earthDamage -= earthDamage * getActiveArmour().getResistances().getEarthDamage() / 100;
		airDamage -= airDamage * getActiveArmour().getResistances().getAirDamage() / 100;
		
		setHealth(getHealth() - neutralDamage - fireDamage - waterDamage - earthDamage - airDamage);
		
		if(getHealth() <= 0) {
			dead = true;
		}
	}
	
	private boolean dead = false;
	public boolean isDead() {
		return dead;
	}
	
	private BufferedImage north;
	private BufferedImage south;
	private BufferedImage east;
	private BufferedImage west;
	private BufferedImage currentPlayer;
	
	public BufferedImage currentImage() {
		return currentPlayer;
	}
	
	public void tick() {
		super.tick();
		attackDelay++;
		if(getXP() >= getLevelXP(getLevel() + 1)) {
			setLevel(getLevel() + 1);
			UserInterface.announceLevelUp = true;
		}
		setDamage(activeWeapon.getDamage());
		double activeArmourHealth = 0;
		if(getActiveArmour() != null) activeArmourHealth = getActiveArmour().getHealthIncrease();
		setMaxHealth(20 + activeArmourHealth + (20 * (getLevel() * 0.43)));
		if(getHealth() > getMaxHealth()) setHealth(getMaxHealth());
		for(int x = getChunk().getX() - 1; x <= getChunk().getX() + 1; x++) {
			for(int y = getChunk().getY() - 1; y <= getChunk().getY() + 1; y++) {
				if(getMaze().getChunk(x, y) == null) {
					Random rnd = new Random();
					int northX = rnd.nextInt(13 - 2 + 1) + 2;
					int southX = rnd.nextInt(13 - 2 + 1) + 2;
					int eastY = rnd.nextInt(13 - 2 + 1) + 2;
					int westY = rnd.nextInt(13 - 2 + 1) + 2;
					if(getMaze().getChunk(x, y - 1) != null) northX = getMaze().getChunk(x, y - 1).getSouthX();
					if(getMaze().getChunk(x, y + 1) != null) southX = getMaze().getChunk(x, y + 1).getNorthX();
					if(getMaze().getChunk(x + 1, y) != null) eastY = getMaze().getChunk(x + 1, y).getWestY();
					if(getMaze().getChunk(x - 1, y) != null) westY = getMaze().getChunk(x - 1, y).getEastY();
					getMaze().addChunk(new Chunk(getMaze(), x, y, northX, southX, eastY, westY, this));
				}
			}
		}
		setChunk(getMaze().getCell(getCellX(), getCellY()).getChunk());

		//Determine the direction of the player and display the correct image for that direction
		if(getDirection().contains("NORTH")) {
			currentPlayer = north;
		}else if(getDirection().contains("SOUTH")) {
			currentPlayer = south;
		}else if(getDirection().contains("EAST")) {
			currentPlayer = east;
		}else if(getDirection().contains("WEST")) {
			currentPlayer = west;
		}

		//HEALTH REGENERATION
		healthRegenTicks++;
		if(healthRegenDelay > 0) healthRegenDelay--;
		if(healthRegenTicks >= 120 && healthRegenDelay <= 0 && !isDead()) {
			setHealth(Math.round(getHealth() + getMaxHealth() / 10));
			if(getHealth() > getMaxHealth()) setHealth(getMaxHealth());
			healthRegenTicks = 0;
		}
		
		boolean near = false;
		for(ItemEntity item : getMaze().items) {
			if(item.getXPos() > getXPos() - 0.5 && item.getXPos() < getXPos() + 0.5 && item.getYPos() > getYPos() - 0.5 && item.getYPos() < getYPos() + 0.5) {
				near = true;
			}
		}
		if(near == false) {
			setNearItem(false);
		}else {
			setNearItem(true);
		}
		
		if(getStamina() < 60 * 5) {
			if(previousSprint == false) {
				staminaRegenDelay--;
				if(staminaRegenDelay <= 0) {
					setStamina(getStamina() + 1);
				}
			}else {
				staminaRegenDelay = 60 * 5;
			}
			previousSprint = isSprinting();
		}else if(getStamina() > 60 * 5) {
			setStamina(60 * 5);
		}
	}
	private int healthRegenTicks = 0;
	private int healthRegenDelay = 60 * 5;
	private int staminaRegenDelay = 60 * 5;
	private boolean previousSprint = false;
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		//g.fillOval(GameInfo.WIDTH - 15, GameInfo.HEIGHT - 15, 30, 30);
		//g.drawImage(currentPlayer,GameInfo.WIDTH - 15, GameInfo.HEIGHT - 15, 30, 30, GameInfo.FRAME);
		Graphics2D g2d = (Graphics2D)g;
		
		if(GameInfo.BOUNDING_BOXES) {
			g.setColor(Color.BLUE);
			g.drawRect(GameInfo.WIDTH - 15, GameInfo.HEIGHT - 15, (int)Math.ceil(GameInfo.currentTileSize * 0.4), (int)Math.ceil(GameInfo.currentTileSize * 0.4));
		}
		
		if(activeWeapon != null) {
			if(getDirection().contains("NORTH")) {
				if(attackDelay >= 35) {
					g2d.drawImage(activeWeapon.getImage(), GameInfo.WIDTH + 4, GameInfo.HEIGHT - 9, 18, 18, GameInfo.FRAME);
				}else {
					g2d.rotate(Math.toRadians(45), GameInfo.WIDTH + 4, GameInfo.HEIGHT - 9); 
					g2d.drawImage(activeWeapon.getImage(), GameInfo.WIDTH + 10, GameInfo.HEIGHT - 9, 18, 18, GameInfo.FRAME);
					g2d.rotate(Math.toRadians(-45), GameInfo.WIDTH + 4, GameInfo.HEIGHT - 9); 
				}
			}else if(getDirection().contains("SOUTH")) {
			}else if(getDirection().contains("EAST")) {
			}else if(getDirection().contains("WEST")) {
				if(attackDelay >= 35) {
					g2d.rotate(Math.toRadians(270), GameInfo.WIDTH - 10, GameInfo.HEIGHT);  
					g2d.drawImage(activeWeapon.getImage(), GameInfo.WIDTH - 20, GameInfo.HEIGHT - 8, 18, 18, GameInfo.FRAME);
					g2d.rotate(Math.toRadians(-270), GameInfo.WIDTH - 10, GameInfo.HEIGHT);
				}else {
					g2d.rotate(Math.toRadians(225), GameInfo.WIDTH - 10, GameInfo.HEIGHT);  
					g2d.drawImage(activeWeapon.getImage(), GameInfo.WIDTH - 21, GameInfo.HEIGHT - 16, 18, 18, GameInfo.FRAME);
					g2d.rotate(Math.toRadians(-225), GameInfo.WIDTH - 10, GameInfo.HEIGHT);
				}
			}
		}
		g2d.drawImage(currentPlayer, GameInfo.WIDTH - 15, GameInfo.HEIGHT - 15, (int)Math.ceil(GameInfo.currentTileSize * 0.4), (int)Math.ceil(GameInfo.currentTileSize * 0.4), GameInfo.FRAME);
		
		if(activeWeapon != null) {
			if(getDirection().contains("NORTH")) {
			}else if(getDirection().contains("SOUTH")) {
				if(attackDelay >= 35) {
					g2d.rotate(Math.toRadians(270), GameInfo.WIDTH - 10, GameInfo.HEIGHT);
					g2d.drawImage(activeWeapon.getImage(), GameInfo.WIDTH - 20, GameInfo.HEIGHT - 17, 18, 18, GameInfo.FRAME);
					g2d.rotate(Math.toRadians(-270), GameInfo.WIDTH - 10, GameInfo.HEIGHT);
				}else {
					g2d.rotate(Math.toRadians(225), GameInfo.WIDTH - 10, GameInfo.HEIGHT);  
					g2d.drawImage(activeWeapon.getImage(), GameInfo.WIDTH - 15, GameInfo.HEIGHT - 23, 18, 18, GameInfo.FRAME);
					g2d.rotate(Math.toRadians(-225), GameInfo.WIDTH - 10, GameInfo.HEIGHT);
				}
			}else if(getDirection().contains("EAST")) {
				if(attackDelay >= 35) {
					g2d.drawImage(activeWeapon.getImage(), GameInfo.WIDTH - 3, GameInfo.HEIGHT - 9, 18, 18, GameInfo.FRAME);
				}else {
					g2d.rotate(Math.toRadians(45), GameInfo.WIDTH - 3, GameInfo.HEIGHT - 9);
					g2d.drawImage(activeWeapon.getImage(), GameInfo.WIDTH + 7, GameInfo.HEIGHT - 17, 18, 18, GameInfo.FRAME);
					g2d.rotate(Math.toRadians(-45), GameInfo.WIDTH - 3, GameInfo.HEIGHT - 9);
				}
			}else if(getDirection().contains("WEST")) {
			}
		}
	}
	
	private boolean nearItem = false;
	public boolean isNearItem() {
		return nearItem;
	}
	public void setNearItem(boolean nearItem) {
		this.nearItem = nearItem;
	}
	
}
