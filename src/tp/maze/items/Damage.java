package tp.maze.items;

public class Damage {

	private double neutralDamage = 0;
	private double fireDamage = 0;
	private double waterDamage = 0;
	private double earthDamage = 0;
	private double airDamage = 0;
	
	public Damage(double neutralDamage, double fireDamage, double waterDamage, double earthDamage, double airDamage) {
		this.neutralDamage = neutralDamage;
		this.fireDamage = fireDamage;
		this.waterDamage = waterDamage;
		this.earthDamage = earthDamage;
		this.airDamage = airDamage;
	}
	
	public double getNeutralDamage() {
		return Math.round(neutralDamage * 100.0) / 100.0;
	}
	public double getFireDamage() {
		return Math.round(fireDamage * 100.0) / 100.0;
	}
	public double getWaterDamage() {
		return Math.round(waterDamage * 100.0) / 100.0;
	}
	public double getEarthDamage() {
		return Math.round(earthDamage * 100.0) / 100.0;
	}
	public double getAirDamage() {
		return Math.round(airDamage * 100.0) / 100.0;
	}
	
}
