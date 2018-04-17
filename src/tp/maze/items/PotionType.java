package tp.maze.items;

import java.awt.Color;

public enum PotionType {

	HEALTH(Color.RED);
	
	PotionType(Color c) {
		this.colour = c;
	}
	
	private Color colour;
	
	public Color getColour() {
		return colour;
	}
}
