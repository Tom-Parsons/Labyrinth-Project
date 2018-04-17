package tp.maze.items;

public class Armour extends Item {

	public Armour(String ID, String GameID, String name, int level, ItemType type, Rarity rarity, double healthIncrease, Damage resistances) {
		super(ID, GameID, name, level, type, rarity);
		this.healthIncrease = healthIncrease;
		this.resistances = resistances;
	}

	private double healthIncrease = 0;
	private Damage resistances;
	
	public double getHealthIncrease() {
		return healthIncrease;
	}
	public Damage getResistances() {
		return resistances;
	}
	
}
