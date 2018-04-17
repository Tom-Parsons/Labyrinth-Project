package tp.maze.items;

public class Weapon extends Item {

	public Weapon(String ID, String GameID, String name, int level, ItemType type, Rarity rarity, Damage damage) {
		super(ID, GameID, name, level, type, rarity);
		this.damage = damage;
	}
	
	private Damage damage;
	
	public Damage getDamage() {
		return damage;
	}
	
}
