package tp.maze.entities;

import java.awt.Color;

import tp.maze.game.Chunk;
import tp.maze.game.Maze;

public class Entity {

	private Maze maze;
	private double xPos;
	private double yPos;
	private Chunk chunk;
	private double speed;
	private double velX = 0;
	private double velY = 0;
	private Color colour;
	
	/**
	 * Creates a new entity
	 * @param maze An instance of the main maze class
	 * @param xPos The x position to spawn the Entity
	 * @param yPos The y position to spawn the entity
	 */
	public Entity(Maze maze, double xPos, double yPos) {
		this.maze = maze;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public void setXPos(double x) {
		xPos = x;
	}
	public void setYPos(double y) {
		yPos = y;
	}
	
	public double getXPos() {
	    return Math.round(xPos * 1000.0) / 1000.0;
	}
	public double getYPos() {
		return Math.round(yPos * 1000.0) / 1000.0;
	}
	public int getCellX() {
		return (int) xPos;
	}
	public int getCellY() {
		return (int) yPos;
	}
	public Chunk getChunk() {
		return chunk;
	}
	public double getSpeed() {
		return speed;
	}
	public double getVelX() {
		return velX;
	}
	public double getVelY() {
		return velY;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public void setVelX(double velX) {
		this.velX = velX;
	}
	public void setVelY(double velY) {
		this.velY = velY;
	}
	public Color getColour() {
		return colour;
	}
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}
	public Maze getMaze() {
		return maze;
	}

}
