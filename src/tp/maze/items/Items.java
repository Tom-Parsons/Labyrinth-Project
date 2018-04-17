package tp.maze.items;

import java.util.ArrayList;
import java.util.Random;

import tp.maze.entities.ItemEntity;
import tp.maze.entities.Player;
import tp.maze.game.Maze;

public class Items {

	public static final Item AIR = new Item("AIR", "AIR", "AIR", 0, ItemType.AIR, null);
	public static final Weapon FISTS = new Weapon("Fists", "Fists", "Fists", 0, ItemType.WEAPON, Rarity.COMMON, new Damage(0.5, 0, 0, 0, 0));
	public static final Armour CLOTHES = new Armour("Clothes", "Clothes", "Clothes", 0, ItemType.ARMOUR, Rarity.COMMON, 0, new Damage(0, 0, 0, 0, 0));
	public static ArrayList<Item> defaultItems = new ArrayList<Item>();
	public static ArrayList<Weapon> defaultWeapons = new ArrayList<Weapon>();
	public static ArrayList<Weapon> activeWeapons = new ArrayList<Weapon>();
	public static ArrayList<Armour> defaultArmour = new ArrayList<Armour>();
	public static ArrayList<Armour> activeArmour = new ArrayList<Armour>();
	public static ArrayList<Potion> defaultPotions = new ArrayList<Potion>();
	public static ArrayList<Potion> activePotions = new ArrayList<Potion>();
	public static ArrayList<Item> activeItems = new ArrayList<Item>();
	
	public static void setupItems() {
		//SETUP OTHER ITEMS
		Items.defaultItems.add(new Item("Dirt", "DEFAULT", "Dirt", 1, ItemType.ITEM, Rarity.COMMON));
		Items.defaultItems.add(new Item("Map", "DEFAULT", "Map", 1, ItemType.ITEM, Rarity.UNCOMMON));
		Items.defaultItems.add(new Item("Enchanted_Map", "DEFAULT", "Enchanted Map", 5, ItemType.ITEM, Rarity.RARE));
		Items.defaultItems.add(new Item("Chest_Locator", "DEFAULT", "Chest Locator", 5, ItemType.ITEM, Rarity.LEGENDARY));
		
		//SETUP WEAPONS
		Items.defaultWeapons.add(new Weapon("Axe_Wood", "DEFAULT", "Wooden Axe", 1, ItemType.WEAPON, Rarity.COMMON, new Damage(3, 0, 0, 0, 0))); 
		Items.defaultWeapons.add(new Weapon("Dagger_Wood", "DEFAULT", "Wooden Dagger", 1, ItemType.WEAPON, Rarity.COMMON, new Damage(2, 0, 0, 0, 0)));
		Items.defaultWeapons.add(new Weapon("Sword_Wood", "DEFAULT", "Wooden Sword", 1, ItemType.WEAPON, Rarity.COMMON, new Damage(3, 0, 0, 0, 0)));
		Items.defaultWeapons.add(new Weapon("Sword_Wood_Flames", "DEFAULT", "Wooden Sword of Flames", 1, ItemType.WEAPON, Rarity.UNCOMMON, new Damage(3, 1, 0, 0, 0)));
		Items.defaultWeapons.add(new Weapon("Sword_Wood_Water", "DEFAULT", "Wooden Water Sword", 1, ItemType.WEAPON, Rarity.UNCOMMON, new Damage(3, 0, 1, 0, 0)));
		Items.defaultWeapons.add(new Weapon("Sword_Wood_Earth", "DEFAULT", "Wooden Earth Sword", 1, ItemType.WEAPON, Rarity.RARE, new Damage(3, 0, 0, 2, 0)));
		Items.defaultWeapons.add(new Weapon("Sword_Wood_Air", "DEFAULT", "Wooden Air Sword", 1, ItemType.WEAPON, Rarity.RARE, new Damage(3, 0, 0, 0, 2)));
		Items.defaultWeapons.add(new Weapon("Sword_Wood_Wizard", "DEFAULT", "Wooden Wizard Sword", 1, ItemType.WEAPON, Rarity.LEGENDARY, new Damage(3, 1, 1, 1, 1)));		
		Items.defaultWeapons.add(new Weapon("Sword_Stone", "DEFAULT", "Stone Sword", 2, ItemType.WEAPON, Rarity.RARE, new Damage(4, 0, 0, 0, 0)));
		Items.defaultWeapons.add(new Weapon("Sword_Stone_Air", "DEFAULT", "Stone Air Sword", 2, ItemType.WEAPON, Rarity.RARE, new Damage(4, 0, 0, 0, 2)));
		
		Items.defaultArmour.add(new Armour("Armour_Worn_Leather", "DEFAULT", "Worn Leather Armour", 1, ItemType.ARMOUR, Rarity.COMMON, 5, new Damage(2, 0, 2, 0, 0)));
		Items.defaultArmour.add(new Armour("Armour_Leather", "DEFAULT", "Leather Armour", 1, ItemType.ARMOUR, Rarity.UNCOMMON, 10, new Damage(3, 0, 3, 0, 0)));
		Items.defaultArmour.add(new Armour("Armour_Iron", "DEFAULT", "Iron Armour", 1, ItemType.ARMOUR, Rarity.RARE, 15, new Damage(4, 1, 1, 1, 1)));
		Items.defaultArmour.add(new Armour("Armour_Wizard_Robes", "DEFAULT", "Wizard Robes", 1, ItemType.ARMOUR, Rarity.LEGENDARY, 15, new Damage(1, 2, 2, 2, 2)));
		
		Items.defaultPotions.add(new Potion("Potion_Health_Minor", "DEFAULT", "Minor Health Potion", 1, ItemType.POTION, PotionType.HEALTH, Rarity.COMMON, 5, 1));
		Items.defaultPotions.add(new Potion("Potion_Health", "DEFAULT", "Health Potion", 1, ItemType.POTION, PotionType.HEALTH, Rarity.UNCOMMON, 7, 1));
		Items.defaultPotions.add(new Potion("Potion_Health_Major", "DEFAULT", "Major Health Potion", 1, ItemType.POTION, PotionType.HEALTH, Rarity.RARE, 9, 1));
		Items.defaultPotions.add(new Potion("Potion_Health_Ultimate", "DEFAULT", "Ultimate Health Potion", 1, ItemType.POTION, PotionType.HEALTH, Rarity.LEGENDARY, 15, 1));
	}
	
	public static boolean gameIDExists(String GameID) {
		for(Item i : activeItems){
			if(i.getGameID().equals(GameID)) {
				return true;
			}
		}
		return false;
	}
	
	public static Weapon getWeaponFromID(String ID) {
		if(ID.equals("Fists")) return FISTS;
		for(Weapon w : defaultWeapons) {
			if(w.getID().equals(ID)) {
				return w;
			}
		}
		return null;
	}
	public static Armour getArmourFromID(String ID) {
		if(ID.equals("Clothes")) return CLOTHES;
		for(Armour a : defaultArmour) {
			if(a.getID().equals(ID)) {
				return a;
			}
		}
		return null;
	}
	public static Potion getPotionFromID(String ID) {
		for(Potion p : defaultPotions) {
			if(p.getID().equals(ID)) {
				return p;
			}
		}
		return null;
	}
	
	public static Item getItemFromGameID(String ID) {
		if(ID.equals("Fists")) return FISTS;
		if(ID.equals("Clothes")) return CLOTHES;
		if(ID.equals("AIR")) return AIR;
		for(Item i : activeItems) {
			if(i.getGameID().equals(ID)) {
				return i;
			}
		}
		return null;
	}
	public static Item getItemFromID(String ID) {
		for(Item i : defaultItems) {
			if(i.getID().equals(ID)) {
				return i;
			}
		}
		return null;
	}
	public static Weapon getWeaponFromGameID(String ID) {
		if(ID.equals("Fists")) return FISTS;
		for(Weapon w : activeWeapons) {
			if(w.getGameID().equals(ID)) {
				return w;
			}
		}
		return null;
	}
	public static Armour getArmourFromGameID(String ID) {
		if(ID.equals("Clothes")) return CLOTHES;
		for(Armour a : activeArmour) {
			if(a.getGameID().equals(ID)) {
				return a;
			}
		}
		return null;
	}
	public static Potion getPotionFromGameID(String ID) {
		for(Potion p: activePotions) {
			if(p.getGameID().equals(ID)) {
				return p;
			}
		}
		return null;
	}
	
	public static ArrayList<Weapon> getLeveledWeapons(int level){
		ArrayList<Weapon> leveledWeapons = new ArrayList<Weapon>();
		for(Weapon w : defaultWeapons) {
			if(w.getLevel() == level) {
				leveledWeapons.add(w);
			}
		}
		return leveledWeapons;
	}
	
	public static void spawnRandomItem(Maze maze, Player player, double x, double y, int level) {
		Random rnd = new Random();
		int dropType = rnd.nextInt(4 - 1 + 1) + 1;
		if(dropType == 1) {
			Weapon w = Items.randomLeveledWeapon(level);
			if(w != null) new ItemEntity(maze, x, y, player, w);
		}else if(dropType == 2) {
			Armour a = Items.randomLeveledArmour(level);
			if(a != null) new ItemEntity(maze, x, y, player, a);
		}else if(dropType == 3){
			Potion p = Items.randomLeveledPotion(level);
			if(p != null) new ItemEntity(maze, x, y, player, p);
		}else if(dropType == 4) {
			Item i = Items.randomItem(level);
			if(i != null) new ItemEntity(maze, x, y, player, i);
		}
	}
	
	public static Item randomItem(int level) {
		Rarity rarity = Rarity.COMMON;
		Random rnd = new Random();
		int chance = rnd.nextInt(100 - 0 + 1) + 0;
		if(chance <= 60) {
			rarity = Rarity.COMMON;
		}else if(chance <= 80) {
			rarity = Rarity.UNCOMMON;
		}else if(chance <= 95) {
			rarity = Rarity.RARE;
		}else {
			rarity = Rarity.LEGENDARY;
		}
		ArrayList<Item> possibleRarities = new ArrayList<Item>();
		for(Item i : defaultItems) {
			if(i.getRarity() == rarity && i.getLevel() <= level) {
				possibleRarities.add(i);
			}
		}
		if(possibleRarities.size() > 0) {
			Item defaultItem = possibleRarities.get(rnd.nextInt((possibleRarities.size() - 1) - 0 + 1) + 0);
			String GameID = randomString();
			while(gameIDExists(GameID)) {
				GameID = randomString();
			}
			Item i = new Item(defaultItem.getID(), GameID, defaultItem.getName(), defaultItem.getLevel(), ItemType.ITEM, defaultItem.getRarity());
			activeItems.add(i);
			return i;
		}else {
			return null;
		}
	}
	public static Weapon randomLeveledWeapon(int level) {
		//ArrayList<Weapon> possibles = getLeveledWeapons(level);
		Rarity rarity = Rarity.COMMON;
		Random rnd = new Random();
		int chance = rnd.nextInt(100 - 0 + 1) + 0;
		if(chance <= 60) {
			rarity = Rarity.COMMON;
		}else if(chance <= 80) {
			rarity = Rarity.UNCOMMON;
		}else if(chance <= 95) {
			rarity = Rarity.RARE;
		}else {
			rarity = Rarity.LEGENDARY;
		}
		ArrayList<Weapon> possibleRarities = new ArrayList<Weapon>();
		for(Weapon w : defaultWeapons) {
			if(w.getRarity() == rarity) {
				possibleRarities.add(w);
			}
		}
		Weapon defaultWeapon = possibleRarities.get(rnd.nextInt((possibleRarities.size() - 1) - 0 + 1) + 0);
		String GameID = randomString();
		while(gameIDExists(GameID)) {
			GameID = randomString();
		}
		Weapon newWeapon = new Weapon(defaultWeapon.getID(), GameID, defaultWeapon.getName(), level, defaultWeapon.getType(), rarity, new Damage(defaultWeapon.getDamage().getNeutralDamage() * level * 0.4, defaultWeapon.getDamage().getFireDamage() * level * 0.4, 
				defaultWeapon.getDamage().getWaterDamage() * level * 0.4, defaultWeapon.getDamage().getEarthDamage() * level * 0.4, defaultWeapon.getDamage().getAirDamage() * level * 0.4));
		activeWeapons.add(newWeapon);
		activeItems.add(newWeapon);
		return newWeapon;
		//return possibleRarities.get(rnd.nextInt((possibleRarities.size() - 1) - 0 + 1) + 0);
	}
	public static Armour randomLeveledArmour(int level) {
		//ArrayList<Weapon> possibles = getLeveledWeapons(level);
		Rarity rarity = Rarity.COMMON;
		Random rnd = new Random();
		int chance = rnd.nextInt(100 - 0 + 1) + 0;
		if(chance <= 60) {
			rarity = Rarity.COMMON;
		}else if(chance <= 80) {
			rarity = Rarity.UNCOMMON;
		}else if(chance <= 95) {
			rarity = Rarity.RARE;
		}else {
			rarity = Rarity.LEGENDARY;
		}
		ArrayList<Armour> possibleRarities = new ArrayList<Armour>();
		for(Armour a : defaultArmour) {
			if(a.getRarity() == rarity) {
				possibleRarities.add(a);
			}
		}
		Armour defaultArmour = possibleRarities.get(rnd.nextInt((possibleRarities.size() - 1) - 0 + 1) + 0);
		String GameID = randomString();
		while(gameIDExists(GameID)) {
			GameID = randomString();
		}
		Armour newArmour = new Armour(defaultArmour.getID(), GameID, defaultArmour.getName(), level, defaultArmour.getType(), rarity, defaultArmour.getHealthIncrease() * level * 0.4, new Damage(defaultArmour.getResistances().getNeutralDamage() * level * 0.4, defaultArmour.getResistances().getFireDamage() * level * 0.4, 
				defaultArmour.getResistances().getWaterDamage() * level * 0.4, defaultArmour.getResistances().getEarthDamage() * level * 0.4, defaultArmour.getResistances().getAirDamage() * level * 0.4));
		activeArmour.add(newArmour);
		activeItems.add(newArmour);
		return newArmour;
	}
	public static Potion randomLeveledPotion(int level) {
		//ArrayList<Weapon> possibles = getLeveledWeapons(level);
		Rarity rarity = Rarity.COMMON;
		Random rnd = new Random();
		int chance = rnd.nextInt(100 - 0 + 1) + 0;
		if(chance <= 60) {
			rarity = Rarity.COMMON;
		}else if(chance <= 80) {
			rarity = Rarity.UNCOMMON;
		}else if(chance <= 95) {
			rarity = Rarity.RARE;
		}else {
			rarity = Rarity.LEGENDARY;
		}
		ArrayList<Potion> possibleRarities = new ArrayList<Potion>();
		for(Potion p : defaultPotions) {
			if(p.getRarity() == rarity) {
				possibleRarities.add(p);
			}
		}
		Potion defaultPotion = possibleRarities.get(rnd.nextInt((possibleRarities.size() - 1) - 0 + 1) + 0);
		String GameID = randomString();
		while(gameIDExists(GameID)) {
			GameID = randomString();
		}
		int uses = rnd.nextInt(3 - 1 + 1) + 1;
		Potion newPotion = new Potion(defaultPotion.getID(), GameID, defaultPotion.getName(), level, defaultPotion.getType(), defaultPotion.getPotionType(), rarity, level * defaultPotion.getBonus(), defaultPotion.getUses() * uses);
		activePotions.add(newPotion);
		activeItems.add(newPotion);
		return newPotion;
		//return possibleRarities.get(rnd.nextInt((possibleRarities.size() - 1) - 0 + 1) + 0);
	}
	
	private static String randomString() {
		String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder SB = new StringBuilder();
		Random rnd = new Random();
		int charCount = 0;
		while(SB.length() < 26) {
			charCount++;
			int i = (int) (rnd.nextFloat() * chars.length());
			if(charCount % 6 == 0) SB.append("-");
			SB.append(chars.charAt(i));
		}
		return SB.toString();
	}
	
}
