package tp.maze.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import tp.maze.items.DamageType;

public enum MonsterType {

	GOBLIN(0.5, 0.9, 0.028, DamageType.EARTH, DamageType.FIRE, 15, 5),
	RAT(0.25, 0.45, 0.037, DamageType.NEUTRAL, DamageType.WATER, 20, 5),
	SKELETON(1, 1, 0.023, DamageType.FIRE, DamageType.EARTH, 15, 10);
	
	private double baseDamageMultiplier;
	private double baseHealthMultiplier;
	private double baseSpeed;
	private DamageType resistance;
	private DamageType weakness;
	private int resistancePercentage;
	private int weaknessPercentage;
	
	MonsterType(double baseDamageMultiplier, double baseHealthMultiplier, double baseSpeed, DamageType resistance, DamageType weakness, int resistancePercentage, int weaknessPercentage) {
		this.baseDamageMultiplier = baseDamageMultiplier;
		this.baseHealthMultiplier = baseHealthMultiplier;
		this.baseSpeed = baseSpeed;
		this.resistance = resistance;
		this.weakness = weakness;
		this.resistancePercentage = resistancePercentage;
		this.weaknessPercentage = weaknessPercentage;
	}
	
	public double getBaseDamage() {
		return baseDamageMultiplier;
	}
	public double getBaseHealth() {
		return baseHealthMultiplier;
	}
	public double getbaseSpeed() {
		return baseSpeed;
	}
	public DamageType getResistance() {
		return resistance;
	}
	public DamageType getWeakness() {
		return weakness;
	}
	public int getResistancePercentage() {
		return resistancePercentage;
	}
	public int getWeaknessPercentage() {
		return weaknessPercentage;
	}
	
	private static final List<MonsterType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	public static MonsterType randomMonsterType() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
	
}
