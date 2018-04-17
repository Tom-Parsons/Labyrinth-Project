package tp.maze.items;

import java.awt.Color;

public enum DamageType {

	NEUTRAL(new Color(255, 40, 40, 255), 'N'),
	FIRE(Color.ORANGE, 'F'),
	WATER(Color.BLUE, 'W'),
	EARTH(new Color(0, 160, 0, 255), 'E'),
	AIR(Color.WHITE, 'A');
	
	DamageType(Color colour, char character){
		this.colour = colour;
		this.character = character;
	}
	
	private Color colour;
	private char character;
	
	public Color getColour() {
		return colour;
	}
	public char getCharater() {
		return character;
	}
	
}
