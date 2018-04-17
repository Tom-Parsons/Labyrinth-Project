package tp.maze.items;

public class Potion extends Item {

	public Potion(String ID, String GameID, String name, int level, ItemType type, PotionType potionType, Rarity rarity, int bonus, int uses) {
		super(ID, GameID, name, level, type, rarity);
		this.potionType = potionType;
		this.bonus = bonus;
		this.uses = uses;
	}
	
	private PotionType potionType;
	private int bonus;
	private int uses;
	
	public int getBonus() {
		return bonus;
	}
	public int getUses() {
		return uses;
	}
	public PotionType getPotionType() {
		return potionType;
	}
	public void removeUse() {
		uses--;
	}
	
}
