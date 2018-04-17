package tp.maze.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import tp.maze.entities.Monster;
import tp.maze.entities.Player;
import tp.maze.items.Damage;
import tp.maze.items.DamageType;
import tp.maze.items.Inventory;
import tp.maze.items.ItemType;
import tp.maze.items.Items;
import tp.maze.items.PotionType;
import tp.maze.items.Rarity;
import tp.maze.main.BufferedImageLoader;
import tp.maze.main.FileHandler;
import tp.maze.main.Game;
import tp.maze.main.GameInfo;

public class UserInterface {
	
	public UserInterface(Player p, Maze maze) {
		this.player= p;
		this.maze = maze;
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			InventoryGUI = loader.loadImage("/Inventory.png");
		}catch(Exception ex) {
			try {
				InventoryGUI = loader.loadImage("/Missing_Texture.png");
			}catch(Exception e) {}
		}
	}
	
	BufferedImage InventoryGUI = null;
	
	private Player player;
	private Maze maze;
	
	private Color pickupItemTextColour = new Color(255, 255, 255, 255);
	private final String pickupItemText = "Press E to pickup item";
	private boolean pickupItemAlphaDirection = true;
	
	private int deadTicks = 0;
	private int deadSeconds = 0;
	
	public void tick() {
		if(player.isDead()) {
			deadTicks += 1;
			if(deadTicks >= 60) {
				deadSeconds--;
				deadTicks = 0;
			}
		}else {
			deadTicks = 0;
			deadSeconds = 5;
		}
		if(announceLevelUp) {
			announceLevelUpTicks++;
			if(announceLevelUpTicks >= 2) {
				levelUpFont = new Font(levelUpFont.getFontName(), levelUpFont.getStyle(), levelUpFont.getSize() + 1);
				announceLevelUpTicks = 0;
			}
		}
	}
	
	private int announceLevelUpTicks = 0;
	public static boolean announceLevelUp = false;
	private double prevXP = 0;
	private double prevHealth = 0;
	
	public void render(Graphics g) {
		if(!GameInfo.USER_INTERFACE) return;
		g.setColor(Color.WHITE);
		if(player.isStatViewMode()) {
			g.setFont(new Font("Arial", Font.ITALIC, 12));
			g.drawString("Level: " + player.getLevel(), 10, 12);
			g.drawString("XP: " + player.getXP(), 10, 24);
			g.drawString("X: " + player.getXPos() + " Y: " + player.getYPos(), 10, 36);
			g.drawString("CellX: " + player.getCellX() + " CellY: " + player.getCellY(), 10, 48);
			g.drawString("ChunkX: "+ player.getChunk().getX() + " ChunkY: " + player.getChunk().getY(), 10, 60);
		}
		double healthPercentage = ((double)player.getHealth() / (double)player.getMaxHealth()) * GameInfo.WIDTH;
		if(prevHealth < healthPercentage) {
			int difference = (int) (healthPercentage - prevHealth);
			prevHealth += (difference * 0.1) * 0.4;
		}else if(prevHealth > healthPercentage){
			int difference = (int) (prevHealth - healthPercentage);
			prevHealth -= (difference * 0.1) * 0.4;
		}
		g.setColor(new Color(255, 0, 0, 255));
		g.fillRect(GameInfo.WIDTH - (GameInfo.WIDTH / 2), (GameInfo.HEIGHT * GameInfo.SCALE) - 40, GameInfo.WIDTH, 30);
		//g.setColor(Color.GREEN);
		g.setColor(Color.GREEN);
		g.fillRect(GameInfo.WIDTH - (GameInfo.WIDTH / 2), (GameInfo.HEIGHT * GameInfo.SCALE) - 40, (int) prevHealth, 30);
		g.setColor(Color.GRAY);
		g.drawRect(GameInfo.WIDTH - (GameInfo.WIDTH / 2), (GameInfo.HEIGHT * GameInfo.SCALE) - 40, GameInfo.WIDTH, 30);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.drawString(player.getHealth() + "/" + player.getMaxHealth(), GameInfo.WIDTH - (g.getFontMetrics(g.getFont()).stringWidth(player.getHealth() + "/" + player.getMaxHealth()) / 2), (GameInfo.HEIGHT * GameInfo.SCALE) - 35+ g.getFontMetrics(g.getFont()).getHeight());
		
		//XP BAR
		g.setColor(Color.GRAY);
		g.fillRoundRect(GameInfo.WIDTH - (GameInfo.WIDTH / 2), (GameInfo.HEIGHT * GameInfo.SCALE) - 70, GameInfo.WIDTH, 20, 25, 25);
		int XP = 0;
		if(player.getLevel() > 1) XP = player.getLevelXP(player.getLevel());
		double XPPercentage = (((double)player.getXP() - XP) / ((double)player.getLevelXP(player.getLevel() + 1) - XP)) * GameInfo.WIDTH;
		if(prevXP < XPPercentage) {
			int difference = (int) (XPPercentage - prevXP);
			prevXP += (difference * 0.1) * 0.4;
		}else if(prevXP > XPPercentage){
			int difference = (int) (prevXP - XPPercentage);
			prevXP -= (difference * 0.1) * 0.4;
		}
		g.setColor(new Color(25, 75, 255, 255));
		g.fillRoundRect(GameInfo.WIDTH - (GameInfo.WIDTH / 2), (GameInfo.HEIGHT * GameInfo.SCALE) - 70, (int) prevXP, 20, 25, 25);
		g.setColor(Color.BLACK);
		g.drawRoundRect(GameInfo.WIDTH - (GameInfo.WIDTH / 2), (GameInfo.HEIGHT * GameInfo.SCALE) - 70, GameInfo.WIDTH, 20, 25, 25);
		g.setColor(Color.WHITE);
		g.drawString("Level " + player.getLevel(), GameInfo.WIDTH - (g.getFontMetrics(g.getFont()).stringWidth("Level " + player.getLevel())) / 2, (GameInfo.HEIGHT * GameInfo.SCALE) - 70 + g.getFontMetrics(g.getFont()).getHeight());
		
		//STAMINA BAR
		if(player.getStamina() < 60 * 5) {
			g.setColor(Color.GRAY);
			g.fillRect(GameInfo.WIDTH * 2 - 90 + 5 - 100, GameInfo.HEIGHT * 2 - 90 + 5 - 35, 175, 15);
			int StaminaPercentage = (int)(((double)player.getStamina() / (double)(60 * 5)) * 175d);
			g.setColor(new Color(25, 75, 255, 255));
			g.fillRect(GameInfo.WIDTH * 2 - 90 + 5 - 100, GameInfo.HEIGHT * 2 - 90 + 5 - 35, StaminaPercentage, 15);
			g.setColor(Color.BLACK);
			g.drawRect(GameInfo.WIDTH * 2 - 90 + 5 - 100, GameInfo.HEIGHT * 2 - 90 + 5 - 35, 175, 15);
			g.setColor(Color.WHITE);
			g.drawString("Stamina", (GameInfo.WIDTH * 2 - 90 + 5 - 100 + (170 / 2)) - (g.getFontMetrics(g.getFont()).stringWidth("Stamina")) / 2, GameInfo.HEIGHT * 2 - 90 + 5 - 35 + (int)(g.getFontMetrics(g.getFont()).getHeight() / 1.25));
		}
		
		//ACTIVE WEAPON
		g.setColor(new Color(60, 60, 60, 255));
		g.fillRect(GameInfo.WIDTH * 2 - 90, GameInfo.HEIGHT * 2 - 90, 80, 80);
		g.setColor(Color.BLACK);
		g.drawRect(GameInfo.WIDTH * 2 - 90, GameInfo.HEIGHT * 2 - 90, 80, 80);
		if(player.getActiveWeapon() != null) {
			if(!player.getActiveWeapon().getID().equals("Fists")) {
				g.drawImage(player.getActiveWeapon().getImage(), GameInfo.WIDTH * 2 - 90 + 5, GameInfo.HEIGHT * 2 - 90 + 5, 80 - 10, 80 - 10, GameInfo.FRAME);
			}
		}
		g.setColor(new Color(60, 60, 60, 255));
		g.fillRect(10, GameInfo.HEIGHT * 2 - 90, 80, 80);
		g.setColor(Color.BLACK);
		g.drawRect(10, GameInfo.HEIGHT * 2 - 90, 80, 80);
		if(player.getActiveArmour() != null) {
			if(!player.getActiveArmour().getID().equals("Clothes")) {
				g.drawImage(player.getActiveArmour().getImage(), 10 + 5, GameInfo.HEIGHT * 2 - 90 + 5, 80 - 10, 80 - 10, GameInfo.FRAME);
			}
		}
		if(player.getActivePotion() != null) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 14));
			g.drawString("Uses: " + player.getActivePotion().getUses(), GameInfo.WIDTH * 2 - 90 + 5 - 100 + 35 - g.getFontMetrics(g.getFont()).stringWidth("Uses: " + player.getActivePotion().getUses()) / 2, GameInfo.HEIGHT * 2 - 90 + 5 - g.getFontMetrics(g.getFont()).getHeight() / 2);
			g.drawImage(player.getActivePotion().getImage(), GameInfo.WIDTH * 2 - 90 + 5 - 100, GameInfo.HEIGHT * 2 - 90 + 5, 80 - 10, 80 - 10, GameInfo.FRAME);
		}
		
		//TARGETTED ENEMY
		if(player.getTargettedEnemy() != null) {
			Monster m = player.getTargettedEnemy();
			double health = m.getHealth();
			double maxHealth = m.getMaxHealth();
			double percentage = (health / maxHealth) * (GameInfo.WIDTH - 20);
			int level = m.getLevel();
			String name = m.getType().toString().substring(0, 1) + m.getType().toString().substring(1, m.getType().toString().length()).toLowerCase();
			String levelText = "Level " + level;
			g.setColor(Color.GRAY);
			g.drawRect(GameInfo.WIDTH - (GameInfo.WIDTH / 2) + 20, 20, GameInfo.WIDTH - 20, 20);
			g.setColor(Color.RED);
			g.fillRect(GameInfo.WIDTH - (GameInfo.WIDTH / 2) + 20, 20, GameInfo.WIDTH - 20, 20);
			g.setColor(Color.GREEN);
			g.fillRect(GameInfo.WIDTH - (GameInfo.WIDTH / 2) + 20, 20, (int)percentage, 20);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 16));
			g.drawString(name, GameInfo.WIDTH - g.getFontMetrics(g.getFont()).stringWidth(name) / 2, 60);
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString(levelText, GameInfo.WIDTH - g.getFontMetrics(g.getFont()).stringWidth(levelText) / 2, 70);
		}
		
		//PICKUP ITEM
		if(player.isNearItem()) {
				if(pickupItemAlphaDirection) {
					pickupItemTextColour = new Color(pickupItemTextColour.getRed(), pickupItemTextColour.getGreen(), pickupItemTextColour.getBlue(), pickupItemTextColour.getAlpha() - 1);
					if(pickupItemTextColour.getAlpha() == 150) pickupItemAlphaDirection = false;
				}else {
					pickupItemTextColour = new Color(pickupItemTextColour.getRed(), pickupItemTextColour.getGreen(), pickupItemTextColour.getBlue(), pickupItemTextColour.getAlpha() + 1);
					if(pickupItemTextColour.getAlpha() == 255) pickupItemAlphaDirection = true;
				}
			g.setColor(pickupItemTextColour);
			g.setFont(new Font("Arial", Font.BOLD, 18));
			g.drawString(pickupItemText, 10, GameInfo.HEIGHT + 100);
		}else {
			pickupItemTextColour = new Color(255, 255, 255, 255);
			pickupItemAlphaDirection = true;
		}
		
		if(FileHandler.isSaving()) {
			System.out.println("Sving");
			g.setFont(new Font("Arial", Font.BOLD, 18));
			g.setColor(Color.WHITE);
			g.drawString("Saving...", GameInfo.WIDTH * 2 - g.getFontMetrics(g.getFont()).stringWidth("Saving...") - 5, 20);
		}
		
		//INVENTORY
		//OPEN INVENTORY
		String hoveringText = "";
		Damage hoveringDamage = Items.FISTS.getDamage();
		Rarity hoveringRarity = Rarity.COMMON;
		int hoveringUses = 1;
		int hoveringPotionBonus = 0;
		PotionType hoveringPotionType = PotionType.HEALTH;
		ItemType hoveringType = ItemType.AIR;
		double hoveringHealthIncrease = 0;
		int hoveringLevel = 1;
		boolean hovering = false;
		if(player.getOpenInventory() != null) {
			g.setColor(new Color(0, 0, 0, 200));
			g.fillRect(0, 0, GameInfo.WIDTH * 2 + 15, GameInfo.HEIGHT * 2 + 15);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 32));
			g.drawString("Inventory", GameInfo.WIDTH - 60, 50);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Active Armour", GameInfo.WIDTH + 50, 25 + 120 - g.getFontMetrics(g.getFont()).getHeight());
			g.drawString("Active Weapon", GameInfo.WIDTH + 50, 25 + GameInfo.HEIGHT * 2 - 150 + g.getFontMetrics(g.getFont()).getHeight());
			g.drawString("Active Potion", GameInfo.WIDTH * 2 - 170 + 5 + 40 - g.getFontMetrics(g.getFont()).stringWidth("Active Potion") / 2, GameInfo.HEIGHT - 40 - g.getFontMetrics(g.getFont()).getHeight());
			Inventory i = player.getOpenInventory();
			int rows = i.getSize() % 5;
			int rowCount = 0;
			int currentRow = rows;
			g.drawImage(InventoryGUI, (GameInfo.WIDTH - (5 * 80) / 2), (GameInfo.HEIGHT - 110), 400, 240, GameInfo.FRAME);
			for(int curItem = 0; curItem < i.getSize(); curItem++) {
				if(rowCount == 5){
					currentRow--;
					rowCount = 0;
				}
				/*g.setColor(new Color(60, 60, 60, 255));
				g.fillRect((GameInfo.WIDTH - (5 * 80) / 2) + 80 * rowCount, (GameInfo.HEIGHT + 50) + currentRow * 80, 80, 80);
				g.setColor(new Color(0, 0, 0, 255));
				g.drawRect((GameInfo.WIDTH - (5 * 80) / 2) + 80 * rowCount, (GameInfo.HEIGHT + 50) + currentRow * 80, 80, 80);*/
				try {
					if(GameInfo.FRAME.getMousePosition().getX() > (GameInfo.WIDTH - (5 * 80) / 2) + 80 * rowCount 
							&& GameInfo.FRAME.getMousePosition().getX() < ((GameInfo.WIDTH - (5 * 80) / 2) + 80 * rowCount) + 80
							&& GameInfo.FRAME.getMousePosition().getY() > (GameInfo.HEIGHT + 50) + currentRow * 80 + 25
							&& GameInfo.FRAME.getMousePosition().getY() < ((GameInfo.HEIGHT + 50) + currentRow * 80) + 105) {
						g.setColor(new Color(120, 120, 120, 255));	
						g.fillRect((GameInfo.WIDTH - (5 * 80) / 2) + 80 * rowCount + 5, (GameInfo.HEIGHT + 50) + currentRow * 80 + 5, 80 - 10, 80 - 10);
						hoveringItem = curItem;
						hovering = true;
						if(i.getItem(curItem).getType() == ItemType.WEAPON) {
							hoveringDamage = Items.getWeaponFromGameID(i.getItem(curItem).getGameID()).getDamage();
							hoveringRarity = Items.getWeaponFromGameID(i.getItem(curItem).getGameID()).getRarity();
							hoveringLevel = Items.getWeaponFromGameID(i.getItem(curItem).getGameID()).getLevel();
							hoveringType = ItemType.WEAPON;
						}else if(i.getItem(curItem).getType() == ItemType.ARMOUR) {
							hoveringDamage = Items.getArmourFromGameID(i.getItem(curItem).getGameID()).getResistances();
							hoveringRarity = Items.getArmourFromGameID(i.getItem(curItem).getGameID()).getRarity();
							hoveringLevel = Items.getArmourFromGameID(i.getItem(curItem).getGameID()).getLevel();
							hoveringType = ItemType.ARMOUR;
							hoveringHealthIncrease = Items.getArmourFromGameID(i.getItem(curItem).getGameID()).getHealthIncrease();
						}else if(i.getItem(curItem).getType() == ItemType.POTION) {
							hoveringUses = Items.getPotionFromGameID(i.getItem(curItem).getGameID()).getUses();
							hoveringPotionBonus = Items.getPotionFromGameID(i.getItem(curItem).getGameID()).getBonus();
							hoveringPotionType = Items.getPotionFromGameID(i.getItem(curItem).getGameID()).getPotionType();
							hoveringRarity = Items.getPotionFromGameID(i.getItem(curItem).getGameID()).getRarity();
							hoveringLevel = Items.getPotionFromGameID(i.getItem(curItem).getGameID()).getLevel();
							hoveringType = ItemType.POTION;
						}
					}
				}catch(Exception ex) {}
				try {
					if(i.getItem(curItem).getType() != ItemType.AIR) {
						if(i.getSelectedItem() != curItem) {
							g.drawImage(i.getItem(curItem).getImage(), (GameInfo.WIDTH - (5 * 80) / 2) + 80 * rowCount + 5, (GameInfo.HEIGHT + 50) + currentRow * 80 + 5, 80 - 10, 80 - 10, GameInfo.FRAME);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if(GameInfo.FRAME.getMousePosition().getX() > (GameInfo.WIDTH - (5 * 80) / 2) + 80 * rowCount 
							&& GameInfo.FRAME.getMousePosition().getX() < ((GameInfo.WIDTH - (5 * 80) / 2) + 80 * rowCount) + 80
							&& GameInfo.FRAME.getMousePosition().getY() > (GameInfo.HEIGHT + 50) + currentRow * 80 + 25
							&& GameInfo.FRAME.getMousePosition().getY() < ((GameInfo.HEIGHT + 50) + currentRow * 80) + 105) {
						if(i.getItem(curItem).getType() != ItemType.AIR) {
							if(i.getSelectedItem() == -1) {
								hoveringText = i.getItem(curItem).getName();
							}
						}
					}
				}catch(Exception e) {}
				rowCount++;
			}
			g.setColor(new Color(60, 60, 60, 255));
			g.fillRect(GameInfo.WIDTH - 40, GameInfo.HEIGHT * 2 - 150, 80, 80);
			g.setColor(new Color(0, 0, 0, 255));
			g.drawRect(GameInfo.WIDTH - 40, GameInfo.HEIGHT * 2 - 150, 80, 80);
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 40
						&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 40 + 80
						&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT * 2 - 150 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT * 2 - 150 + 105) {
					g.setColor(new Color(120, 120, 120, 255));	
					g.fillRect(GameInfo.WIDTH - 40 + 5, GameInfo.HEIGHT * 2 - 150 + 5, 80 - 10, 80 - 10);
					hoveringItem = 100;
					hovering = true;
					if(!player.getActiveWeapon().getID().equals("Fists")) {
						hoveringDamage = player.getActiveWeapon().getDamage();
						hoveringRarity = player.getActiveWeapon().getRarity();
						hoveringLevel = player.getActiveWeapon().getLevel();
						hoveringType = player.getActiveWeapon().getType();
					}
				}
			}catch(Exception ex) {}
			try {
				if(player.getActiveWeapon() != null) {
					if(!player.getActiveWeapon().getID().equals("Fists")) {
						if(i.getSelectedItem() != 100) {
							g.drawImage(player.getActiveWeapon().getImage(), GameInfo.WIDTH - 40 + 5, GameInfo.HEIGHT * 2 - 150 + 5, 80 - 10, 80 - 10, GameInfo.FRAME);
						}
					}
				}
			} catch (Exception e) {}
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 40
						&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 40 + 80
						&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT * 2 - 150 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT * 2 - 150 + 105) {
					if(player.getActiveWeapon() != null) {
						if(!player.getActiveWeapon().getID().equals("Fists")) {
							if(i.getSelectedItem() == -1) {
								hoveringText= player.getActiveWeapon().getName();
							}
						}
					}
				}
			}catch(Exception e) {}
			g.setColor(new Color(60, 60, 60, 255));
			g.fillRect(GameInfo.WIDTH - 40, 80, 80, 80);
			g.setColor(new Color(0, 0, 0, 255));
			g.drawRect(GameInfo.WIDTH - 40, 80, 80, 80);
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 40
						&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 40 + 80
						&& GameInfo.FRAME.getMousePosition().getY() > 80 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < 80 + 105) {
					g.setColor(new Color(120, 120, 120, 255));	
					g.fillRect(GameInfo.WIDTH - 40 + 5, 80 + 5, 80 - 10, 80 - 10);
					hoveringItem = 200;
					hovering = true;
					if(!player.getActiveWeapon().getID().equals("Clothes")) {
						hoveringDamage = player.getActiveArmour().getResistances();
						hoveringRarity = player.getActiveArmour().getRarity();
						hoveringLevel = player.getActiveArmour().getLevel();
						hoveringType = player.getActiveArmour().getType();
						hoveringHealthIncrease = player.getActiveArmour().getHealthIncrease();
					}
				}
			}catch(Exception ex) {}
			try {
				if(player.getActiveArmour() != null) {
					if(!player.getActiveArmour().getID().equals("Clothes")) {
						if(i.getSelectedItem() != 200) {
							g.drawImage(player.getActiveArmour().getImage(), GameInfo.WIDTH - 40 + 5, 80 + 5, 80 - 10, 80 - 10, GameInfo.FRAME);
						}
					}
				}
			} catch (Exception e) {}
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH - 40
						&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH - 40 + 80
						&& GameInfo.FRAME.getMousePosition().getY() > 80 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < 80 + 105) {
					if(player.getActiveArmour() != null) {
						if(!player.getActiveArmour().getID().equals("Clothes")) {
							if(i.getSelectedItem() == -1) {
								hoveringText= player.getActiveArmour().getName();
							}
						}
					}
				}
			}catch(Exception e) {}
			g.setColor(new Color(60, 60, 60, 255));
			g.fillRect(GameInfo.WIDTH * 2 - 170, GameInfo.HEIGHT - 40, 80, 80);
			g.setColor(new Color(0, 0, 0, 255));
			g.drawRect(GameInfo.WIDTH * 2 - 170, GameInfo.HEIGHT - 40, 80, 80);
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH * 2 - 170
						&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH * 2 - 170 + 80
						&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT - 40 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT - 40 + 105) {
					g.setColor(new Color(120, 120, 120, 255));	
					g.fillRect(GameInfo.WIDTH * 2 - 170 + 5, GameInfo.HEIGHT - 40 + 5, 80 - 10, 80 - 10);
					hoveringItem = 300;
					hovering = true;
					if(player.getActivePotion() != null) {
						hoveringPotionBonus = player.getActivePotion().getBonus();
						hoveringUses = player.getActivePotion().getUses();
						hoveringPotionType = player.getActivePotion().getPotionType();
						hoveringRarity = player.getActivePotion().getRarity();
						hoveringLevel = player.getActivePotion().getLevel();
						hoveringType = player.getActivePotion().getType();
					}
				}
			}catch(Exception ex) {}
			try {
				if(player.getActivePotion() != null) {
					if(i.getSelectedItem() != 300) {
						g.drawImage(player.getActivePotion().getImage(), GameInfo.WIDTH * 2 - 170 + 5, GameInfo.HEIGHT - 40 + 5, 80 - 10, 80 - 10, GameInfo.FRAME);
					}
				}
			} catch (Exception e) {}
			try {
				if(GameInfo.FRAME.getMousePosition().getX() > GameInfo.WIDTH * 2 - 170
						&& GameInfo.FRAME.getMousePosition().getX() < GameInfo.WIDTH * 2 - 170 + 80
						&& GameInfo.FRAME.getMousePosition().getY() > GameInfo.HEIGHT - 40 + 25
						&& GameInfo.FRAME.getMousePosition().getY() < GameInfo.HEIGHT - 40 + 105) {
					if(player.getActivePotion() != null) {
						if(i.getSelectedItem() == -1) {
							hoveringText = player.getActivePotion().getName();
						}
					}
				}
			}catch(Exception e) {}
			if(i.getSelectedItem() != -1) {
				try {
					if(i.getSelectedItem() < 100) {
						g.drawImage(i.getItem(i.getSelectedItem()).getImage(), (int)GameInfo.FRAME.getMousePosition().getX() - 35, (int)GameInfo.FRAME.getMousePosition().getY() - 35, 80 - 10, 80 - 10, GameInfo.FRAME);
					}else if(i.getSelectedItem() == 100){
						if(!player.getActiveWeapon().getID().equals("Fists")) {
							g.drawImage(player.getActiveWeapon().getImage(), (int)GameInfo.FRAME.getMousePosition().getX() - 35, (int)GameInfo.FRAME.getMousePosition().getY() - 35, 80 - 10, 80 - 10, GameInfo.FRAME);
						}
					}else if(i.getSelectedItem() == 200){
						if(!player.getActiveArmour().getID().equals("Clothes")) {
							g.drawImage(player.getActiveArmour().getImage(), (int)GameInfo.FRAME.getMousePosition().getX() - 35, (int)GameInfo.FRAME.getMousePosition().getY() - 35, 80 - 10, 80 - 10, GameInfo.FRAME);
						}
					}else if(i.getSelectedItem() == 300) {
						if(player.getActivePotion() != null) {
							g.drawImage(player.getActivePotion().getImage(), (int)GameInfo.FRAME.getMousePosition().getX() - 35, (int)GameInfo.FRAME.getMousePosition().getY() - 35, 80 - 10, 80 - 10, GameInfo.FRAME);
						}
					}
				}catch(Exception e) {}
			}
		}
		if(!hovering) hoveringItem = -1;
		try {
			if(!hoveringText.equals("")) {
				int overallYSize = 66;
				if(hoveringType == ItemType.ARMOUR) {
					overallYSize += 13;
				}
				if(hoveringType == ItemType.WEAPON || hoveringType == ItemType.ARMOUR) {
					if(hoveringDamage.getNeutralDamage() > 0){
						overallYSize += 13;
					}
					if(hoveringDamage.getFireDamage() > 0){
						overallYSize += 13;
					}
					if(hoveringDamage.getWaterDamage() > 0){
						overallYSize += 13;
					}
					if(hoveringDamage.getEarthDamage() > 0){
						overallYSize += 13;
					}
					if(hoveringDamage.getAirDamage() > 0){
						overallYSize += 13;
					}
				}
				if(hoveringType == ItemType.POTION) {
					overallYSize += 39;
				}
				Font title = new Font("Arial", Font.BOLD, 16);
				g.setColor(new Color(40, 40, 40, 255));
				g.fillRoundRect((int)GameInfo.FRAME.getMousePosition().getX() + 10, (int)GameInfo.FRAME.getMousePosition().getY() - 50, g.getFontMetrics(title).stringWidth(hoveringText) + 8, overallYSize, 8, 8);
				g.setColor(Color.BLACK);
				g.drawRoundRect((int)GameInfo.FRAME.getMousePosition().getX() + 10, (int)GameInfo.FRAME.getMousePosition().getY() - 50, g.getFontMetrics(title).stringWidth(hoveringText) + 8, overallYSize, 8, 8);
				g.setColor(Color.WHITE);
				g.setFont(title);
				g.drawString(hoveringText, (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() - 35);
				Font body = new Font("Arial", Font.BOLD, 14);
				g.setFont(body);
				g.setColor(Color.WHITE);
				g.drawString("Level " + hoveringLevel, (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() - 18);
				g.setColor(hoveringRarity.getColour());
				g.drawString(hoveringRarity.toString().substring(0, 1) + hoveringRarity.toString().substring(1, hoveringRarity.toString().length()).toLowerCase(), (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() - 5);
				int armourYIncrease = 0;
				if(hoveringType == ItemType.ARMOUR) {
					armourYIncrease = 13;
					g.setColor(Color.RED);
					g.drawString("Health: +" + hoveringHealthIncrease, (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + 13);
				}
				String armourPercentageChar = "";
				g.setColor(Color.WHITE);
				if(hoveringType == ItemType.WEAPON || hoveringType == ItemType.ARMOUR) {
					if(hoveringType == ItemType.WEAPON) {
						g.drawString("Damage:", (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + 13 + armourYIncrease);
					}else if(hoveringType == ItemType.ARMOUR) {
						g.drawString("Resistance:", (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + 13 + armourYIncrease);
						armourPercentageChar = "%";
					}
					int increaseAmount = 26 + armourYIncrease;
					if(hoveringDamage.getNeutralDamage() > 0){
						g.setColor(DamageType.NEUTRAL.getColour());
						g.drawString(hoveringDamage.getNeutralDamage() +  armourPercentageChar + " Neutral", (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + increaseAmount);
						increaseAmount += 13;
					}
					if(hoveringDamage.getFireDamage() > 0){
						g.setColor(DamageType.FIRE.getColour());
						g.drawString(hoveringDamage.getFireDamage() + armourPercentageChar + " Fire", (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + increaseAmount);
						increaseAmount += 13;
					}
					if(hoveringDamage.getWaterDamage() > 0){
						g.setColor(DamageType.WATER.getColour());
						g.drawString(hoveringDamage.getWaterDamage() + armourPercentageChar + " Water", (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + increaseAmount);
						increaseAmount += 13;
					}
					if(hoveringDamage.getEarthDamage() > 0){
						g.setColor(DamageType.EARTH.getColour());
						g.drawString(hoveringDamage.getEarthDamage() + armourPercentageChar + " Earth", (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + increaseAmount);
						increaseAmount += 13;
					}
					if(hoveringDamage.getAirDamage() > 0){
						g.setColor(DamageType.AIR.getColour());
						g.drawString(hoveringDamage.getAirDamage() + armourPercentageChar + " Air", (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + increaseAmount);
						increaseAmount += 13;
					}
				}else if(hoveringType == ItemType.POTION) {
					g.setColor(Color.WHITE);
					g.drawString("Effect", (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + 13);
					g.setColor(hoveringPotionType.getColour());
					if(hoveringPotionType == PotionType.HEALTH) {
						g.drawString("+" + hoveringPotionBonus + " HP", (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + 26);
					}
					if(hoveringUses > 1) {
						g.setColor(Color.WHITE);
					}else {
						g.setColor(Color.RED);
					}
					g.drawString("Uses Left: " + hoveringUses, (int)GameInfo.FRAME.getMousePosition().getX() + 10 + 4, (int)GameInfo.FRAME.getMousePosition().getY() + 47);
				}
			}
		}catch(Exception ex) {}
		
		if(announceLevelUp == true) {
			g.setColor(Color.WHITE);
			g.setFont(levelUpFont);
			g.drawString("Reached Level: " + player.getLevel(), GameInfo.WIDTH - (g.getFontMetrics(levelUpFont).stringWidth("Reached Level: " + player.getLevel()) / 2), GameInfo.HEIGHT - 20);
			if(levelUpFont.getSize() >= 72) {
				announceLevelUp = false;
			}
		}else {
			levelUpFont = new Font("Arial", Font.BOLD, 16);		
		}
		
		if(player.hasMap() || player.hasEnchantedMap()) {
			Chunk c = player.getChunk();
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
					g.fillRect((GameInfo.WIDTH * 2 - 160) + (x * 10), 10 + (y * 10), 10, 10);
				}
			}
			double px = player.getXPos() - (16 * c.getX());
			double py = player.getYPos() - (16 * c.getY());
			px = (px * 1000d) / 1000d;
			py = (py * 1000d) / 1000d;
			g.drawImage(player.currentImage(), (int)((GameInfo.WIDTH * 2 - 160) + (px * 10)), (int)(10 + (py * 10)), 4, 4, GameInfo.FRAME);
			for(Monster m : maze.monsters) {
				if(m.getChunk() == c) {
					if(maze.getCell(m.getCellX(), m.getCellY()).isDiscovered()) {
						double mx = m.getXPos() - (16 * c.getX());
						double my = m.getYPos() - (16 * c.getY());
						g.drawImage(m.getCurrentImage(), (int)((GameInfo.WIDTH * 2 - 160) + (mx * 10)), (int)(10 + (my * 10)), 4, 4, GameInfo.FRAME);
					}
				}
			}
			
			g.setColor(Color.GRAY);
			g.drawRect(GameInfo.WIDTH * 2 - 160, 10, 160, 160);
		}
		
		if(player.isDead()) {
			if(deadSeconds == -1) {
				FileHandler.deleteSave(Game.SAVE_FILE);
				Game.SAVE_FILE = 0;
				GameInfo.gameState = GameInfo.GameState.MENU;
			}
			g.setColor(new Color(0, 0, 0, 200));
			g.fillRect(0, 0, GameInfo.WIDTH * 2 + 15, GameInfo.HEIGHT * 2 + 15);
			g.setColor(Color.RED);
			g.setFont(new Font("Arial", Font.BOLD, 48));
			g.drawString("You have died!", GameInfo.WIDTH - g.getFontMetrics(g.getFont()).stringWidth("You have died!") / 2, GameInfo.HEIGHT - 50);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 32));
			g.drawString("Returning to menu in " + deadSeconds, GameInfo.WIDTH - g.getFontMetrics(g.getFont()).stringWidth("Returning to menu in " + deadSeconds) / 2, GameInfo.HEIGHT);
		}
	}
	
	private Font levelUpFont = new Font("Arial", Font.BOLD, 16);
	
	private int hoveringItem = -1;
	public int getHoveringItem() {
		return hoveringItem;
	}
	
}
