package tp.maze.game;

import java.util.ArrayList;
import java.util.Random;

import tp.maze.entities.Monster;
import tp.maze.entities.MonsterType;
import tp.maze.entities.Player;
import tp.maze.items.Chest;
import tp.maze.main.GameInfo;

public class Chunk {

	private Maze maze;
	private int x;
	private int y;
	private Cell[][] cells;
	
	private Player player;
	
	public Chunk(Maze maze, int x, int y, int northX, int southX, int eastY, int westY, Player player) {
		this.maze = maze;
		this.x = x;
		this.y = y;
		this.northConnection = northX;
		this.southConnection = southX;
		this.eastConnection = eastY;
		this.westConnection = westY;
		this.player = player;
		Random rnd = new Random();
		int chance = rnd.nextInt(100 - 0 + 1) + 0;
		if(chance <= 95) {
			cells = generateChunk(x, y, northConnection, eastConnection, southConnection, westConnection);
		}else {
			cells = generateRoomChunk(x, y, northConnection, eastConnection, southConnection, westConnection);
		}
	}
	
	public Chunk(Maze maze, int x, int y, int northX, int southX, int eastY, int westY, Player player, Cell[][] cells) {
		this.maze = maze;
		this.x = x;
		this.y = y;
		this.northConnection = northX;
		this.southConnection = southX;
		this.eastConnection = eastY;
		this.westConnection = westY;
		this.player = player;
		this.cells = cells;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public Cell[][] getCells(){
		return cells;
	}
	
	public int getNorthX() {
		return northConnection;
	}
	public int getSouthX() {
		return southConnection;
	}
	public int getEastY() {
		return eastConnection;
	}
	public int getWestY() {
		return westConnection;
	}
	
	private int northConnection;
	private int southConnection;
	private int eastConnection;
	private int westConnection;
	
	public void addConnectingPath(String dir, int cellX, int cellY) {
		if(dir.equals("NORTH")) {
			cells[cellX][0].setType(CellType.PATH);
			cells[cellX][1].setType(CellType.PATH);
		}
		if(dir.equals("SOUTH")) {
			cells[cellX][15].setType(CellType.PATH);
			cells[cellX][14].setType(CellType.PATH);
		}
		if(dir.equals("EAST")) {
			cells[0][cellY].setType(CellType.PATH);
			cells[1][cellY].setType(CellType.PATH);
		}
		if(dir.equals("WEST")) {
			cells[15][cellY].setType(CellType.PATH);
			cells[14][cellY].setType(CellType.PATH);
		}
	}
	
	private ArrayList<Cell> path = new ArrayList<Cell>();
	
	private Cell[][] generateRoomChunk(int x, int y, int northX, int eastY, int southX, int westY) {
		northConnection = northX;
		southConnection = southX;
		eastConnection = eastY;
		westConnection = westY;
		Cell[][] generatedCells = new Cell[16][16];
		for(int newX = 0; newX < 16 ; newX++) {
			for(int newY = 0; newY < 16; newY++) {
				generatedCells[newX][newY] = new Cell(maze, (x * 16) + newX, (y * 16) + newY, CellType.WALL, false, this, player);
				if(newX > 0 && newX < 15 && newY > 0 && newY < 15) {
					Random rnd = new Random();
					int chance = rnd.nextInt(100 - 0 + 1) + 0;
					if(chance < 10) {
						generatedCells[newX][newY].setType(CellType.WALL);
					}else {
						generatedCells[newX][newY].setType(CellType.PATH);
						path.add(generatedCells[newX][newY]);
					}
				}
			}
		}
		if(northX != -1) generatedCells[northX][0].setType(CellType.PATH);
		if(northX != -1) generatedCells[northX][1].setType(CellType.PATH);
		if(northX != -1) generatedCells[northX][2].setType(CellType.PATH);
		if(eastY != -1) generatedCells[15][eastY].setType(CellType.PATH); 
		if(eastY != -1) generatedCells[14][eastY].setType(CellType.PATH); 
		if(eastY != -1) generatedCells[13][eastY].setType(CellType.PATH); 
		if(southX != -1) generatedCells[southX][15].setType(CellType.PATH);
		if(southX != -1) generatedCells[southX][14].setType(CellType.PATH);
		if(southX != -1) generatedCells[southX][13].setType(CellType.PATH);
		if(westY != -1) generatedCells[0][westY].setType(CellType.PATH);
		if(westY != -1) generatedCells[1][westY].setType(CellType.PATH);
		if(westY != -1) generatedCells[2][westY].setType(CellType.PATH);
		Random rnd = new Random();
		int monsterChance = rnd.nextInt(100 - 0 + 1) + 0;
		if(monsterChance <= 90) {
			int amountOfMonsters = 1;
			int monsterAmountChance = rnd.nextInt(100 - 0 + 1) + 0;
			if(monsterAmountChance < 70) {
				amountOfMonsters = 1;
			}else if(monsterAmountChance < 95){
				amountOfMonsters = 2;
			}else {
				amountOfMonsters = 3;
			}
			for(int a = 1; a <= amountOfMonsters; a++) {
				Cell cell = path.get(rnd.nextInt((path.size() - 1) - 0 + 1) + 0);
				int posX = cell.getX();
				int posY = cell.getY();
				Monster m = new Monster(maze, posX + 0.5, posY + 0.5, player, MonsterType.randomMonsterType());
				maze.monsters.add(m);
			}
		}
		for(Cell cell : path) {
			int chance = rnd.nextInt(500 - 0 + 1) + 0;
			if(chance < 1) {
				new Chest(player.getLevel(), cell.getX() + 0.5, cell.getY() + 0.5, player, maze);
			}
			chance = rnd.nextInt(100 - 0 + 1) + 0;
			if(chance <= 1) {
				cell.setHasLight(true);
			}
		}
		
		return generatedCells;
	}
	
	private Cell[][] generateChunk(int x, int y, int northX, int eastY, int southX, int westY) {
		northConnection = northX;
		southConnection = southX;
		eastConnection = eastY;
		westConnection = westY;
		Cell[][] generatedCells = new Cell[16][16];
		for(int newX = 0; newX < 16 ; newX++) {
			for(int newY = 0; newY < 16; newY++) {
				generatedCells[newX][newY] = new Cell(maze, (x * 16) + newX, (y * 16) + newY, CellType.WALL, false, this, player);
			}
		}
		//GENERATE
		ArrayList<Cell> badCells = new ArrayList<Cell>();
		int startx = 7;
		int starty = 7;
		int currentCellX = startx;
		int currentCellY= starty;
		int wallKnockX = currentCellX;
		int wallKnockY = currentCellY;
		generatedCells[currentCellX][currentCellY].setType(CellType.PATH);
		//System.out.println("X: " + currentCellX + " Y: " + currentCellY);
		boolean generated = false;
		while(!generated) {
			Random rnd = new Random();
			int direction = rnd.nextInt(4 - 1 + 1) + 1; // (max - min + 1) + min;
			//System.out.println(direction);
			//UP
			if(direction == 1) {
				
				//boolean valid = false;
				//It is below the chunk border
				if(currentCellY > 0 + 3) {
					//Check if there is a path 1 above the current tile
					//Check if there is a path 2 above the current tile
					//Check if there is a path 3 above the current tile
					if(//generatedCells[currentCellX][currentCellY - 1].getType() != CellType.PATH
							generatedCells[currentCellX][currentCellY - 2].getType() != CellType.PATH
							&& generatedCells[currentCellX][currentCellY - 3].getType() != CellType.PATH
						//Check if there is a path 1 above and 1 to the left of the current tile
						//Check if there is a path 1 above and 1 to the right of the current tile
						//Check if there is a path 2 above and 1 to the left of the current tile
						//Check if there is a path 2 above and 1 to the right of the current tile
						&& generatedCells[currentCellX - 1][currentCellY - 2].getType() != CellType.PATH
								&& generatedCells[currentCellX + 1][currentCellY - 2].getType() != CellType.PATH
								&& generatedCells[currentCellX - 1][currentCellY - 3].getType() != CellType.PATH
								&& generatedCells[currentCellX + 1][currentCellY - 3].getType() != CellType.PATH) {
							currentCellY -= 2;
							wallKnockX = currentCellX;
							wallKnockY = currentCellY + 1;
							generatedCells[currentCellX][currentCellY].setType(CellType.PATH);
							generatedCells[wallKnockX][wallKnockY].setType(CellType.PATH);
							path.add(generatedCells[currentCellX][currentCellY]);
							path.add(generatedCells[wallKnockX][wallKnockY]);
							//valid = true;
						//}
					}else {
						//if(valid == false) {
							boolean up = false;
							boolean down = false;
							boolean left = false;
							boolean right = false;
							//Check up
							if(currentCellY <= 3) {
								up = true;
							}else {
								if(generatedCells[currentCellX][currentCellY - 2].getType() == CellType.PATH
										|| generatedCells[currentCellX][currentCellY - 3].getType() == CellType.PATH
										|| generatedCells[currentCellX + 1][currentCellY - 2].getType() == CellType.PATH
										|| generatedCells[currentCellX - 1][currentCellY - 2].getType() == CellType.PATH
										|| generatedCells[currentCellX - 1][currentCellY - 3].getType() == CellType.PATH
										|| generatedCells[currentCellX + 1][currentCellY - 3].getType() == CellType.PATH) {
									up = true;
								}
							}
							//Check down
							if(currentCellY >= 15 - 3) {
								down = true;
							}else {
								if(generatedCells[currentCellX][currentCellY + 2].getType() == CellType.PATH
										|| generatedCells[currentCellX][currentCellY + 3].getType() == CellType.PATH
										|| generatedCells[currentCellX + 1][currentCellY + 2].getType() == CellType.PATH
										|| generatedCells[currentCellX - 1][currentCellY + 2].getType() == CellType.PATH
										|| generatedCells[currentCellX - 1][currentCellY + 3].getType() == CellType.PATH
										|| generatedCells[currentCellX + 1][currentCellY + 3].getType() == CellType.PATH) {
									down = true;
								}
							}
							//Check left
							if(currentCellX <= 3) {
								left = true;
							}else {
								if(generatedCells[currentCellX - 2][currentCellY].getType() == CellType.PATH
										|| generatedCells[currentCellX - 3][currentCellY].getType() == CellType.PATH
										|| generatedCells[currentCellX - 2][currentCellY + 1].getType() == CellType.PATH
										|| generatedCells[currentCellX - 2][currentCellY - 1].getType() == CellType.PATH
										|| generatedCells[currentCellX - 3][currentCellY - 1].getType() == CellType.PATH
										|| generatedCells[currentCellX - 3][currentCellY + 1].getType() == CellType.PATH) {
									left = true;
								}
							}
							//Check right
							if(currentCellX >= 15 - 3) {
								right = true;
							}else {
								if(generatedCells[currentCellX + 2][currentCellY].getType() == CellType.PATH
										|| generatedCells[currentCellX + 3][currentCellY].getType() == CellType.PATH
										|| generatedCells[currentCellX + 2][currentCellY + 1].getType() == CellType.PATH
										|| generatedCells[currentCellX + 2][currentCellY - 1].getType() == CellType.PATH
										|| generatedCells[currentCellX + 3][currentCellY - 1].getType() == CellType.PATH
										|| generatedCells[currentCellX + 3][currentCellY + 1].getType() == CellType.PATH) {
									right = true;
								}
							}
							//System.out.println("up " + up + " down " + down + " left " + left + " right " + right);
							if(up == true && down == true && left == true && right == true) {
								Backtrack b = new Backtrack(currentCellX, currentCellY, generatedCells, badCells);
								if(b.getBadCell() != null) {
									badCells.add(b.getBadCell());
									currentCellX = b.getX();
									currentCellY = b.getY();
								}else {
									generated = true;
								}
								//badCells.add(generatedCells[currentCellX][currentCellY]);
								//badCells.add(generatedCells[wallKnockX][wallKnockY]);
							}
						//}
					}
				}//DOWN
			}else if(direction == 2) {
				
					//boolean valid = false;
					//It is below the chunk border
					if(currentCellY < 15 - 3) {
						//Check if there is a path 1 above the current tile
						//Check if there is a path 2 above the current tile
						//Check if there is a path 3 above the current tile
						if(//generatedCells[currentCellX][currentCellY + 1].getType() != CellType.PATH
								 generatedCells[currentCellX][currentCellY + 2].getType() != CellType.PATH
								&& generatedCells[currentCellX][currentCellY + 3].getType() != CellType.PATH
							//Check if there is a path 1 above and 1 to the left of the current tile
							//Check if there is a path 1 above and 1 to the right of the current tile
							//Check if there is a path 2 above and 1 to the left of the current tile
							//Check if there is a path 2 above and 1 to the right of the current tile
							&& generatedCells[currentCellX - 1][currentCellY + 2].getType() != CellType.PATH
									&& generatedCells[currentCellX + 1][currentCellY + 2].getType() != CellType.PATH
									&& generatedCells[currentCellX - 1][currentCellY + 3].getType() != CellType.PATH
									&& generatedCells[currentCellX + 1][currentCellY + 3].getType() != CellType.PATH) {
								currentCellY += 2;
								wallKnockX = currentCellX;
								wallKnockY = currentCellY - 1;
								generatedCells[currentCellX][currentCellY].setType(CellType.PATH);
								generatedCells[wallKnockX][wallKnockY].setType(CellType.PATH);
								path.add(generatedCells[currentCellX][currentCellY]);
								path.add(generatedCells[wallKnockX][wallKnockY]);
							//}
						}else {
							//if(valid == false) {
								boolean up = false;
								boolean down = false;
								boolean left = false;
								boolean right = false;
								//Check up
								if(currentCellY <= 3) {
									up = true;
								}else {
									if(generatedCells[currentCellX][currentCellY - 2].getType() == CellType.PATH
											|| generatedCells[currentCellX][currentCellY - 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY - 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY - 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY - 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY - 3].getType() == CellType.PATH) {
										up = true;
									}
								}
								//Check down
								if(currentCellY >= 15 - 3) {
									down = true;
								}else {
									if(generatedCells[currentCellX][currentCellY + 2].getType() == CellType.PATH
											|| generatedCells[currentCellX][currentCellY + 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY + 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY + 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY + 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY + 3].getType() == CellType.PATH) {
										down = true;
									}
								}
								//Check left
								if(currentCellX <= 3) {
									left = true;
								}else {
									if(generatedCells[currentCellX - 2][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX - 3][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX - 2][currentCellY + 1].getType() == CellType.PATH
											|| generatedCells[currentCellX - 2][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX - 3][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX - 3][currentCellY + 1].getType() == CellType.PATH) {
										left = true;
									}
								}
								//Check right
								if(currentCellX >= 15 - 3) {
									right = true;
								}else {
									if(generatedCells[currentCellX + 2][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX + 3][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX + 2][currentCellY + 1].getType() == CellType.PATH
											|| generatedCells[currentCellX + 2][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX + 3][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX + 3][currentCellY + 1].getType() == CellType.PATH) {
										right = true;
									}
								}
								//System.out.println("up " + up + " down " + down + " left " + left + " right " + right);
								if(up == true && down == true && left == true && right == true) {
									Backtrack b = new Backtrack(currentCellX, currentCellY, generatedCells, badCells);
									if(b.getBadCell() != null) {
										badCells.add(b.getBadCell());
										currentCellX = b.getX();
										currentCellY = b.getY();
									}else {
										generated = true;
									}
									//badCells.add(generatedCells[currentCellX][currentCellY]);
									//badCells.add(generatedCells[wallKnockX][wallKnockY]);
								}
						}
					}
					
				}else if(direction == 3) {
					
					//boolean valid = false;
					//It is below the chunk border
					if(currentCellX > 0 + 3) {
						//Check if there is a path 1 above the current tile
						//Check if there is a path 2 above the current tile
						//Check if there is a path 3 above the current tile
						if(//generatedCells[currentCellX - 1][currentCellY].getType() != CellType.PATH
								generatedCells[currentCellX - 2][currentCellY].getType() != CellType.PATH
								&& generatedCells[currentCellX - 3][currentCellY].getType() != CellType.PATH
							//Check if there is a path 1 above and 1 to the left of the current tile
							//Check if there is a path 1 above and 1 to the right of the current tile
							//Check if there is a path 2 above and 1 to the left of the current tile
							//Check if there is a path 2 above and 1 to the right of the current tile
							&& generatedCells[currentCellX - 2][currentCellY + 1].getType() != CellType.PATH
									&& generatedCells[currentCellX - 2][currentCellY - 1].getType() != CellType.PATH
									&& generatedCells[currentCellX - 3][currentCellY + 1].getType() != CellType.PATH
									&& generatedCells[currentCellX - 3][currentCellY - 1].getType() != CellType.PATH) {
								currentCellX -= 2;
								wallKnockY = currentCellY;
								wallKnockX = currentCellX + 1;
								generatedCells[currentCellX][currentCellY].setType(CellType.PATH);
								generatedCells[wallKnockX][wallKnockY].setType(CellType.PATH);
								path.add(generatedCells[currentCellX][currentCellY]);
								path.add(generatedCells[wallKnockX][wallKnockY]);
							//}
						}else {
							//if(valid == false) {
								boolean up = false;
								boolean down = false;
								boolean left = false;
								boolean right = false;
								//Check up
								if(currentCellY <= 3) {
									up = true;
								}else {
									if(generatedCells[currentCellX][currentCellY - 2].getType() == CellType.PATH
											|| generatedCells[currentCellX][currentCellY - 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY - 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY - 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY - 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY - 3].getType() == CellType.PATH) {
										up = true;
									}
								}
								//Check down
								if(currentCellY >= 15 - 3) {
									down = true;
								}else {
									if(generatedCells[currentCellX][currentCellY + 2].getType() == CellType.PATH
											|| generatedCells[currentCellX][currentCellY + 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY + 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY + 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY + 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY + 3].getType() == CellType.PATH) {
										down = true;
									}
								}
								//Check left
								if(currentCellX <= 3) {
									left = true;
								}else {
									if(generatedCells[currentCellX - 2][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX - 3][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX - 2][currentCellY + 1].getType() == CellType.PATH
											|| generatedCells[currentCellX - 2][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX - 3][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX - 3][currentCellY + 1].getType() == CellType.PATH) {
										left = true;
									}
								}
								//Check right
								if(currentCellX >= 15 - 3) {
									right = true;
								}else {
									if(generatedCells[currentCellX + 2][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX + 3][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX + 2][currentCellY + 1].getType() == CellType.PATH
											|| generatedCells[currentCellX + 2][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX + 3][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX + 3][currentCellY + 1].getType() == CellType.PATH) {
										right = true;
									}
								}
								//System.out.println("up " + up + " down " + down + " left " + left + " right " + right);
								if(up == true && down == true && left == true && right == true) {
									Backtrack b = new Backtrack(currentCellX, currentCellY, generatedCells, badCells);
									if(b.getBadCell() != null) {
										badCells.add(b.getBadCell());
										currentCellX = b.getX();
										currentCellY = b.getY();
									}else {
										generated = true;
									}
									//badCells.add(generatedCells[currentCellX][currentCellY]);
									//badCells.add(generatedCells[wallKnockX][wallKnockY]);
								}
							//}
						}
					}
					
				}else if(direction == 4) {
					
					//boolean valid = false;
					//It is below the chunk border
					if(currentCellX < 15 - 3) {
						//Check if there is a path 1 above the current tile
						//Check if there is a path 2 above the current tile
						//Check if there is a path 3 above the current tile
						if(//generatedCells[currentCellX + 1][currentCellY].getType() != CellType.PATH
								 generatedCells[currentCellX + 2][currentCellY].getType() != CellType.PATH
								&& generatedCells[currentCellX + 3][currentCellY].getType() != CellType.PATH
							//Check if there is a path 1 above and 1 to the left of the current tile
							//Check if there is a path 1 above and 1 to the right of the current tile
							//Check if there is a path 2 above and 1 to the left of the current tile
							//Check if there is a path 2 above and 1 to the right of the current tile
							&& generatedCells[currentCellX + 2][currentCellY + 1].getType() != CellType.PATH
									&& generatedCells[currentCellX + 2][currentCellY - 1].getType() != CellType.PATH
									&& generatedCells[currentCellX + 3][currentCellY + 1].getType() != CellType.PATH
									&& generatedCells[currentCellX + 3][currentCellY - 1].getType() != CellType.PATH) {
								currentCellX += 2;
								wallKnockY = currentCellY;
								wallKnockX = currentCellX - 1;
								generatedCells[currentCellX][currentCellY].setType(CellType.PATH);
								generatedCells[wallKnockX][wallKnockY].setType(CellType.PATH);
								path.add(generatedCells[currentCellX][currentCellY]);
								path.add(generatedCells[wallKnockX][wallKnockY]);
							//}
						}else {
							//if(valid == false) {
								boolean up = false;
								boolean down = false;
								boolean left = false;
								boolean right = false;
								//Check up
								if(currentCellY <= 3) {
									up = true;
								}else {
									if(generatedCells[currentCellX][currentCellY - 2].getType() == CellType.PATH
											|| generatedCells[currentCellX][currentCellY - 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY - 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY - 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY - 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY - 3].getType() == CellType.PATH) {
										up = true;
									}
								}
								//Check down
								if(currentCellY >= 15 - 3) {
									down = true;
								}else {
									if(generatedCells[currentCellX][currentCellY + 2].getType() == CellType.PATH
											|| generatedCells[currentCellX][currentCellY + 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY + 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY + 2].getType() == CellType.PATH
											|| generatedCells[currentCellX - 1][currentCellY + 3].getType() == CellType.PATH
											|| generatedCells[currentCellX + 1][currentCellY + 3].getType() == CellType.PATH) {
										down = true;
									}
								}
								//Check left
								if(currentCellX <= 3) {
									left = true;
								}else {
									if(generatedCells[currentCellX - 2][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX - 3][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX - 2][currentCellY + 1].getType() == CellType.PATH
											|| generatedCells[currentCellX - 2][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX - 3][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX - 3][currentCellY + 1].getType() == CellType.PATH) {
										left = true;
									}
								}
								//Check right
								if(currentCellX >= 15 - 3) {
									right = true;
								}else {
									if(generatedCells[currentCellX + 2][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX + 3][currentCellY].getType() == CellType.PATH
											|| generatedCells[currentCellX + 2][currentCellY + 1].getType() == CellType.PATH
											|| generatedCells[currentCellX + 2][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX + 3][currentCellY - 1].getType() == CellType.PATH
											|| generatedCells[currentCellX + 3][currentCellY + 1].getType() == CellType.PATH) {
										right = true;
									}
								}
								//System.out.println("up " + up + " down " + down + " left " + left + " right " + right);
								if(up == true && down == true && left == true && right == true) {
									Backtrack b = new Backtrack(currentCellX, currentCellY, generatedCells, badCells);
									if(b.getBadCell() != null) {
										badCells.add(b.getBadCell());
										currentCellX = b.getX();
										currentCellY = b.getY();
									}else {
										generated = true;
									}
									//badCells.add(generatedCells[currentCellX][currentCellY]);
									//badCells.add(generatedCells[wallKnockX][wallKnockY]);
								}
							//}
						}
					}
				}
			
		}
		if(northX != -1) generatedCells[northX][0].setType(CellType.PATH);
		if(northX != -1) generatedCells[northX][1].setType(CellType.PATH);
		if(northX != -1) generatedCells[northX][2].setType(CellType.PATH);
		if(eastY != -1) generatedCells[15][eastY].setType(CellType.PATH); 
		if(eastY != -1) generatedCells[14][eastY].setType(CellType.PATH); 
		if(eastY != -1) generatedCells[13][eastY].setType(CellType.PATH); 
		if(southX != -1) generatedCells[southX][15].setType(CellType.PATH);
		if(southX != -1) generatedCells[southX][14].setType(CellType.PATH);
		if(southX != -1) generatedCells[southX][13].setType(CellType.PATH);
		if(westY != -1) generatedCells[0][westY].setType(CellType.PATH);
		if(westY != -1) generatedCells[1][westY].setType(CellType.PATH);
		if(westY != -1) generatedCells[2][westY].setType(CellType.PATH);
		
		Random rnd = new Random();
		int monsterChance = rnd.nextInt(100 - 0 + 1) + 0;
		if(monsterChance <= 99 && GameInfo.ENABLE_ENEMIES) {
			int amountOfMonsters = 1;
			int monsterAmountChance = rnd.nextInt(100 - 0 + 1) + 0;
			if(monsterAmountChance < 40) {
				amountOfMonsters = 1;
			}else if(monsterAmountChance < 95){
				amountOfMonsters = 2;
			}else {
				amountOfMonsters = 3;
			}
			amountOfMonsters = rnd.nextInt(4 - 1 + 1) + 1;
			for(int a = 1; a <= amountOfMonsters; a++) {
				Cell cell = path.get(rnd.nextInt((path.size() - 1) - 0 + 1) + 0);
				int posX = cell.getX();
				int posY = cell.getY();
				Monster m = new Monster(maze, posX + 0.5, posY + 0.5, player, MonsterType.randomMonsterType());
				maze.monsters.add(m);
			}
		}
		for(Cell cell : path) {
			int chance = rnd.nextInt(500 - 0 + 1) + 0;
			if(chance < 1) {
				new Chest(player.getLevel(), cell.getX() + 0.5, cell.getY() + 0.5, player, maze);
			}
			chance = rnd.nextInt(100 - 0 + 1) + 0;
			if(chance <= 1) {
				maze.torches.add(new Torch(player, maze, cell.getX() + 0.25, cell.getY() + 0.25));
			}
		}
		
		return generatedCells;
	}
	
	private class Backtrack{
		private int x;
		private int y;
		private Cell badCell = null;

		public Backtrack(int currentCellX, int currentCellY, Cell[][] cells, ArrayList<Cell> badCells) {
			x = currentCellX;
			y = currentCellY;
			if(cells[x][y - 2].getType() == CellType.PATH && !badCells.contains(cells[x][y - 2])) {
				if(cells[x][y - 1].getType() == CellType.PATH) {
					badCell = cells[x][y];
					y -= 2;
				}
			}
			if(badCell == null && cells[x][y + 2].getType() == CellType.PATH && !badCells.contains(cells[x][y + 2])) {
				if(cells[x][y + 1].getType() == CellType.PATH) {
					badCell = cells[x][y];
					y += 2;
				}
			}
			if(badCell == null && cells[x - 2][y].getType() == CellType.PATH && !badCells.contains(cells[x - 2][y])) {
				if(cells[x - 1][y].getType() == CellType.PATH) {
					badCell = cells[x][y];
					x -= 2;
				}
			}
			if(badCell == null && cells[x + 2][y].getType() == CellType.PATH && !badCells.contains(cells[x + 2][y])) {
				if(cells[x + 1][y].getType() == CellType.PATH) {
					badCell = cells[x][y];
					x += 2;
				}
			}
		}
		
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public Cell getBadCell() {
			return badCell;
		}
	}
	
}
