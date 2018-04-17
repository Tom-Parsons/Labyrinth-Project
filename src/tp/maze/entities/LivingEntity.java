package tp.maze.entities;

import tp.maze.game.CellType;
import tp.maze.game.Maze;
import tp.maze.items.Damage;
import tp.maze.main.GameInfo;

public class LivingEntity extends Entity{

	public LivingEntity(Maze maze, double xPos, double yPos) {
		super(maze, xPos, yPos);
		this.direction = "NORTH";
		this.health = maxHealth;
		setStamina(60 * 5);
	}
	
	private String direction;
	private double health;
	private double maxHealth;
	private int level;
	private int xp;
	private Damage damage;
	
	public String getDirection() {
		return direction;
	}
	public double getHealth() {
		return health;
	}
	public double getMaxHealth() {
		return maxHealth;
	}
	public int getLevel() {
		return level;
	}
	public int getXP() {
		return xp;
	}
	public Damage getDamage() {
		return damage;
	}
	
	public void die() {
		
	}
	
	public void setXP(int xp) {
		this.xp = xp;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public void setHealth(double newHealth) {
		health = Math.round(newHealth * 100.0) / 100.0;;
		if(health <= 0) {
			die();
		}
	}
	public void setMaxHealth(Double maxHealth) {
		this.maxHealth = Math.round(maxHealth * 100.0) / 100.0;
	}
	public void setDamage(Damage damage) {
		this.damage = damage;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	private boolean sprinting;
	public boolean isSprinting() {
		return sprinting;
	}
	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
	}
	private int stamina;
	public int getStamina() {
		return stamina;
	}
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	
	/**
	 * The update method to determine collisions with the wall.
	 */
	public void tick() {
		if(getVelX() != 0) {
			boolean canMove = true;
			if(getVelX() < 0) {
				if(isSprinting() && getStamina() > 0) {
					setVelX(-getSpeed() - (getSpeed() / 2));
					setStamina(getStamina() - 1);
				}else {
					setVelX(-getSpeed());
				}
				if(getMaze().getCell((int) (getXPos() + getVelX()), (int)getYPos()).getType() == CellType.WALL || getMaze().getCell((int) (getXPos() + getVelX() - 0.01), (int)(getYPos() + ((GameInfo.currentTileSize * 0.4) / GameInfo.currentTileSize))).getType() == CellType.WALL) {
					canMove = false;
				}
			}else if(getVelX() > 0) {
				if(isSprinting() && getStamina() > 0) {
					setVelX(getSpeed() + (getSpeed() / 2));
					setStamina(getStamina() - 1);
				}else {
					setVelX(getSpeed());
				}
				if(getMaze().getCell((int) (getXPos() + ((GameInfo.currentTileSize * 0.4) / GameInfo.currentTileSize) + getVelX() + 0.01), (int)getYPos()).getType() == CellType.WALL || getMaze().getCell((int) (getXPos() + ((GameInfo.currentTileSize * 0.4) / GameInfo.currentTileSize) + getVelX() + 0.01), (int)(getYPos() + (GameInfo.currentTileSize * 0.4) / GameInfo.currentTileSize)).getType() == CellType.WALL) {
					canMove = false;
				}
			}
			if(!GameInfo.COLLISIONS) canMove = true;
			if(canMove == true) {
				if(getVelY() != 0) {
					setXPos(getXPos() + (getVelX() * 0.75));
				}else {
					setXPos(getXPos() + getVelX());
				}
			}
		}
		if(getVelY() != 0) {
			boolean canMove = true;
			if(getVelY() < 0) {
				if(isSprinting() && getStamina() > 0) {
					setVelY(-getSpeed() - (getSpeed() / 2));
					setStamina(getStamina() - 1);
				}else {
					setVelY(-getSpeed());
				}
				if(getMaze().getCell((int) getXPos(), (int) (getYPos() + getVelY())).getType() == CellType.WALL || getMaze().getCell((int)(getXPos() + ((GameInfo.currentTileSize * 0.4) / GameInfo.currentTileSize)), (int) (getYPos() + getVelY())).getType() == CellType.WALL) {
					canMove = false;
				}
			}else if(getVelY() > 0) {
				if(isSprinting() && getStamina() > 0) {
					setVelY(getSpeed() + (getSpeed() / 2));
					setStamina(getStamina() - 1);
				}else {
					setVelY(getSpeed());
				}
				if(getMaze().getCell((int) getXPos(), (int) (getYPos() + ((GameInfo.currentTileSize * 0.4) / GameInfo.currentTileSize) + getVelY() + 0.01)).getType() == CellType.WALL || getMaze().getCell((int)(getXPos() + ((GameInfo.currentTileSize * 0.4) / GameInfo.currentTileSize)), (int) (getYPos() + ((GameInfo.currentTileSize * 0.4) / GameInfo.currentTileSize) + getVelY() + 0.01)).getType() == CellType.WALL) {
					canMove = false;
				}
			}
			if(!GameInfo.COLLISIONS) canMove = true;
			if(canMove == true) {
				if(getVelX() != 0) {
					setYPos(getYPos() + (getVelY() * 0.75));
				}else {
					setYPos(getYPos() + getVelY());
				}
			}
		}
		if(getVelX() != 0 && getVelY() != 0) {
			if(getVelX() > 0 && getVelY() > 0) {
				setDirection("SOUTH-EAST");
			}else if(getVelX() > 0 && getVelY() < 0) {
				setDirection("NORTH-EAST");
			}else if(getVelX() < 0 && getVelY() > 0) {
				setDirection("SOUTH-WEST");
			}else if(getVelX() < 0 && getVelY() < 0) {
				setDirection("NORTH-WEST");
			}
		}else if(getVelX() != 0) {
			if(getVelX() > 0) {
				setDirection("EAST");
			}else if(getVelX() < 0) {
				setDirection("WEST");
			}
		}else if(getVelY() != 0) {
			if(getVelY() > 0) {
				setDirection("SOUTH");
			}else if(getVelY() < 0) {
				setDirection("NORTH");
			}
		}
	}
}
