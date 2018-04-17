package tp.maze.items;

import java.awt.image.BufferedImage;

import tp.maze.main.BufferedImageLoader;

public class Item {

	public Item(String ID, String GameID, String name, int level, ItemType type, Rarity rarity) {
		this.ID = ID;
		this.GameID = GameID;
		this.name = name;
		this.level = level;
		this.type = type;
		this.rarity = rarity;
		if(!getID().equals("AIR") && !getID().equals("Fists") && !getID().equals("Clothes")) {
			BufferedImageLoader loader = new BufferedImageLoader();
			try {
				image = loader.loadImage("/" + getID() + ".png");
			} catch (Exception e) {
				try {
					image = loader.loadImage("/Missing_Texture.png");
				}catch(Exception ex) {}
			}
		}
	}
	
	private String ID;
	private String GameID;
	private String name;
	private int level;
	private ItemType type;
	private Rarity rarity;
	private BufferedImage image;
	
	public String getID() {
		return ID;
	}
	public String getGameID() {
		return GameID;
	}
	public String getName() {
		return name;
	}
	public int getLevel() {
		return level;
	}
	public ItemType getType() {
		return type;
	}
	public Rarity getRarity() {
		return rarity;
	}
	public BufferedImage getImage() {
		return image;
	}
	
}
