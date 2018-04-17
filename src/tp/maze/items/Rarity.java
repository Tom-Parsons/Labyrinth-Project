package tp.maze.items;

import java.awt.Color;

public enum Rarity {

	COMMON(Color.GRAY),
	UNCOMMON(Color.YELLOW),
	RARE(Color.ORANGE),
	LEGENDARY(Color.CYAN);
	
	Rarity(Color colour){
		this.colour = colour;
	}
	
	private Color colour;
	
	public Color getColour() {
		return colour;
	}
	
}
