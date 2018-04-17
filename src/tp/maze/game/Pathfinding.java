package tp.maze.game;

import java.util.ArrayList;
import java.util.HashMap;

public class Pathfinding {

	private Maze maze;
	private Cell startCell, endCell;
	private int range;
	
	private ArrayList<Cell> path = new ArrayList<Cell>();
	
	public Pathfinding(Maze maze, Cell startCell, Cell endCell, int range) {
		this.maze = maze;
		this.startCell = startCell;
		this.endCell = endCell;
		this.range = range; 
		//findPath();
		//checkPath(startCell);
		findBreadthPath();
	}
	
	private boolean foundPath = false;
	private ArrayList<Cell> beenTo = new ArrayList<Cell>();
	private ArrayList<Cell> badCells = new ArrayList<Cell>();
	private int currentX;
	private int currentY;
	
	private boolean tryToFind = true;
	private boolean reachedEnd = false;
	private ArrayList<Cell> actualPath = new ArrayList<Cell>();
	private ArrayList<Cell> checkingPath = new ArrayList<Cell>();
	
	/**
	 * Deprecated method - alternate, less efficient pathfinding
	 * @param location
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private void checkPath(Cell location) {
		if(!tryToFind) return;
		int x = location.getX();
		int y = location.getY();
		if(x > startCell.getX() + 10 || x < startCell.getX() - 10 || y > startCell.getY() + 10 || y < startCell.getY() - 10) {
			return;
		}
		for(int newX = x - 1; newX <= x + 1; newX++) {
			for(int newY = y - 1; newY <= y + 1; newY++) {
				if(!(newX == x && newY == y)) {
					Cell newCell = maze.getCell(newX, newY);
					if(newCell.getType() == CellType.PATH && !checkingPath.contains(newCell)) {
						if(!reachedEnd) {
							if(newX == endCell.getX() && newY == endCell.getY()) {
								actualPath.add(newCell);
								reachedEnd = true;
							}else {
								checkingPath.add(newCell);
								checkPath(newCell);
							}
						}
						if(reachedEnd) {
							if(newCell.getType() == CellType.PATH){
								ArrayList<Cell> actualPathUpdate = new ArrayList<Cell>();
								for(Cell c : actualPath) {
									if(isNextTo(newCell, c)) {
										actualPathUpdate.add(newCell);
									}
								}
								for(Cell c : actualPathUpdate) {
									actualPath.add(c);
								}
							}
						}
					}else {
						if(newX == endCell.getX() && newY == endCell.getY()) {
							actualPath.clear();
							tryToFind = false;
						}
					}
				}
			}
		}
	}
	
	private int restartAmount = 0;
	private boolean restarted = false;
	public boolean hasRestarted() {
		return restarted;
	}
	
	private void PutCellIntoQueue(Cell U) {
		cellQueue.add(U);
	}
	private Cell GetCellFromQueue() {
		Cell c = cellQueue.get(0);
		cellQueue.remove(c);
		return c;
	}
	
	private ArrayList<Cell> cellQueue = new ArrayList<Cell>();
	private HashMap<Cell, Cell> cellParent = new HashMap<Cell, Cell>();
	
	private void findBreadthPath() {
		Cell EC = endCell;
		Cell SC = startCell;
		startCell = EC;
		endCell = SC;
		cellQueue.add(startCell);
		while(cellQueue.size() > 0 && !foundPath && !restarted) {
			Cell V = GetCellFromQueue();
			if(V.getType() == CellType.PATH) {
				for(int x = V.getX() - 1;  x <= V.getX() + 1; x++) {
					for(int y = V.getY() - 1; y <= V.getY() + 1; y++) {
						if(!(x == V.getX() - 1 && y == V.getY() - 1) && !(x == V.getX() - 1 && y == V.getY() + 1) 
								&& !(x == V.getX() + 1 && y == V.getY() - 1) && !(x == V.getX() + 1 && y == V.getY() + 1)) {
							if(x > startCell.getX() + 7 || x < startCell.getX() - 7 || y > startCell.getY() + 7 || y < startCell.getY() - 7) {
								restartAmount++;
								if(restartAmount > 4) {
									restarted = true;
								}
							}else {
								Cell U = maze.getCell(x, y);
								if(U.getType() == CellType.PATH) {
									if(!checkingPath.contains(U) && foundPath == false) {
										PutCellIntoQueue(U);
										checkingPath.add(U);
										cellParent.put(U, V);
										if(U.getX() == endCell.getX() && U.getY() == endCell.getY()) {
											foundPath = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(foundPath) {
			Cell C = endCell;
			//path.add(C);
			while(!(C.getX() == startCell.getX() && C.getY() == startCell.getY())) {
				C = cellParent.get(C);
				path.add(C);
			}
		}
	}
	
	private boolean isNextTo(Cell cell1, Cell cell2) {
		for(int x = cell1.getX() - 1;  x <= cell1.getX() + 1; x++) {
			for(int y = cell1.getY() - 1; y <= cell1.getY() + 1; y++) {
				if(x == cell2.getX() && y == cell2.getY()) {
					return true;
				}
			}
		}
		return false;
	}
	@SuppressWarnings("unused")
	private boolean isDirectlyNextTo(Cell cell1, Cell cell2) {
		for(int x = cell1.getX() - 1;  x <= cell1.getX() + 1; x++) {
			for(int y = cell1.getY() - 1; y <= cell1.getY() + 1; y++) {
				if(!(x == cell1.getX() - 1 && y == cell1.getY() - 1) && !(x == cell1.getX() - 1 && y == cell1.getY() + 1) 
						&& !(x == cell1.getX() + 1 && y == cell1.getY() - 1) && !(x == cell1.getX() + 1 && y == cell1.getY() + 1)) {
					if(x == cell2.getX() && y == cell2.getY()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Deprecated method - Depth first search algorithm
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private void findPath() {
		currentX = startCell.getX();
		currentY = startCell.getY();
		while(!foundPath) {
			boolean checkUp = true;
			boolean checkDown = true;
			boolean checkLeft = true;
			boolean checkRight = true;
			if(maze.getCell(currentX, currentY - 1) == null) checkUp = false;
			if(maze.getCell(currentX, currentY + 1) == null) checkDown = false;
			if(maze.getCell(currentX - 1, currentY) == null) checkLeft = false;
			if(maze.getCell(currentX + 1, currentY) == null) checkRight = false;
			if(checkUp && currentY - 1 >= startCell.getY() - (range + 1) && maze.getCell(currentX, currentY - 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY - 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY - 1)) && !badCells.contains(maze.getCell(currentX, currentY - 1))) {
				currentY--;
				beenTo.add(maze.getCell(currentX, currentY));
				if(currentX == endCell.getX() && currentY == endCell.getY()) {
					foundPath = true;
					for(Cell c : beenTo) {
						path.add(c);
					}
				}
			}else if(checkDown && currentY + 1 <= startCell.getY() + (range + 1) && maze.getCell(currentX, currentY + 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY + 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY + 1)) && !badCells.contains(maze.getCell(currentX, currentY + 1))) {
				currentY++;
				beenTo.add(maze.getCell(currentX, currentY));
				if(currentX == endCell.getX() && currentY == endCell.getY()) {
					foundPath = true;
					for(Cell c : beenTo) {
						path.add(c);
					}
				}
			}else if(checkLeft && currentX - 1 >= startCell.getX() - (range + 1) && maze.getCell(currentX - 1, currentY).getType() == CellType.PATH && !(currentX - 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX - 1, currentY)) && !badCells.contains(maze.getCell(currentX - 1, currentY))) {
				currentX--;
				beenTo.add(maze.getCell(currentX, currentY));
				if(currentX == endCell.getX() && currentY == endCell.getY()) {
					foundPath = true;
					for(Cell c : beenTo) {
						path.add(c);
					}
				}
			}else if(checkRight && currentX + 1 <= startCell.getX() + (range + 1) && maze.getCell(currentX + 1, currentY).getType() == CellType.PATH && !(currentX + 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX + 1, currentY)) && !badCells.contains(maze.getCell(currentX + 1, currentY))) {
				currentX++;
				beenTo.add(maze.getCell(currentX, currentY));
				if(currentX == endCell.getX() && currentY == endCell.getY()) {
					foundPath = true;
					for(Cell c : beenTo) {
						path.add(c);
					}
				}
			}else {
				boolean up = false;
				boolean down = false;
				boolean left = false;
				boolean right = false;
				if(checkUp && maze.getCell(currentX, currentY - 1).getType() == CellType.PATH) {
					if(badCells.contains(maze.getCell(currentX, currentY - 1))) {
						up = true;
					}else if(beenTo.contains(maze.getCell(currentX, currentY - 1))) {
						up = true;
					}else if(currentY - 1 <= startCell.getY() - (range + 1)) {
						up = true;
					}
				}else {
					up = true;
				}
				if(checkDown && maze.getCell(currentX, currentY + 1).getType() == CellType.PATH) {
					if(badCells.contains(maze.getCell(currentX, currentY + 1))) {
						down = true;
					}else if(beenTo.contains(maze.getCell(currentX, currentY + 1))) {
						down = true;
					}else if(currentY + 1 >= startCell.getY() - (range + 1)) {
						down = true;
					}
				}else {
					down = true;
				}
				if(checkLeft && maze.getCell(currentX - 1, currentY).getType() == CellType.PATH) {
					if(badCells.contains(maze.getCell(currentX - 1, currentY))) {
						left = true;
					}else if(beenTo.contains(maze.getCell(currentX - 1, currentY))) {
						left = true;
					}else if(currentX - 1 <= startCell.getX() - (range + 1)) {
						left = true;
					}
				}else {
					left = true;
				}
				if(checkRight && maze.getCell(currentX + 1, currentY).getType() == CellType.PATH) {
					if(badCells.contains(maze.getCell(currentX + 1, currentY))) {
						right = true;
					}else if(beenTo.contains(maze.getCell(currentX + 1, currentY))) {
						right = true;
					}else if(currentX + 1 >= startCell.getX() - (range + 1)) {
						right = true;
					}
				}else {
					right = true;
				}
				if(up == true && down == true && left == true && right == true) {
					backtrack();
				}
			}	
		}
	}
	
	private void backtrack() {		
		boolean back = false;
		while(!back) {
			if(beenTo.contains(maze.getCell(currentX, currentY - 1))) {
				beenTo.remove(maze.getCell(currentX, currentY));
				badCells.add(maze.getCell(currentX, currentY));
				currentY--;
				if(currentY - 1 >= startCell.getY() - (range + 1) && maze.getCell(currentX, currentY - 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY - 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY - 1)) && !badCells.contains(maze.getCell(currentX, currentY - 1))) {
					back = true;
					return;
				}else if(currentY + 1 <= startCell.getY() + (range + 1) && maze.getCell(currentX, currentY + 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY + 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY + 1)) && !badCells.contains(maze.getCell(currentX, currentY + 1))) {
					back = true;
					return;
				}else if(currentX - 1 >= startCell.getX() - (range + 1) && maze.getCell(currentX - 1, currentY).getType() == CellType.PATH && !(currentX - 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX - 1, currentY)) && !badCells.contains(maze.getCell(currentX - 1, currentY))) {
					back = true;
					return;
				}else if(currentX + 1 <= startCell.getX() + (range + 1) && maze.getCell(currentX + 1, currentY).getType() == CellType.PATH && !(currentX + 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX + 1, currentY)) && !badCells.contains(maze.getCell(currentX + 1, currentY))) {
					back = true;
					return;
				}
			}else if(beenTo.contains(maze.getCell(currentX, currentY + 1))) {
				beenTo.remove(maze.getCell(currentX, currentY));
				badCells.add(maze.getCell(currentX, currentY));
				currentY++;
				if(currentY - 1 >= startCell.getY() - (range + 1) && maze.getCell(currentX, currentY - 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY - 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY - 1)) && !badCells.contains(maze.getCell(currentX, currentY - 1))) {
					back = true;
					return;
				}else if(currentY + 1 <= startCell.getY() + (range + 1) && maze.getCell(currentX, currentY + 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY + 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY + 1)) && !badCells.contains(maze.getCell(currentX, currentY + 1))) {
					back = true;
					return;
				}else if(currentX - 1 >= startCell.getX() - (range + 1) && maze.getCell(currentX - 1, currentY).getType() == CellType.PATH && !(currentX - 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX - 1, currentY)) && !badCells.contains(maze.getCell(currentX - 1, currentY))) {
					back = true;
					return;
				}else if(currentX + 1 <= startCell.getX() + (range + 1) && maze.getCell(currentX + 1, currentY).getType() == CellType.PATH && !(currentX + 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX + 1, currentY)) && !badCells.contains(maze.getCell(currentX + 1, currentY))) {
					back = true;
					return;
				}
			}else if(beenTo.contains(maze.getCell(currentX - 1, currentY))) {
				beenTo.remove(maze.getCell(currentX, currentY));
				badCells.add(maze.getCell(currentX, currentY));
				currentX--;
				if(currentY - 1 >= startCell.getY() - (range + 1) && maze.getCell(currentX, currentY - 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY - 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY - 1)) && !badCells.contains(maze.getCell(currentX, currentY - 1))) {
					back = true;
					return;
				}else if(currentY + 1 <= startCell.getY() + (range + 1) && maze.getCell(currentX, currentY + 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY + 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY + 1)) && !badCells.contains(maze.getCell(currentX, currentY + 1))) {
					back = true;
					return;
				}else if(currentX - 1 >= startCell.getX() - (range + 1) && maze.getCell(currentX - 1, currentY).getType() == CellType.PATH && !(currentX - 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX - 1, currentY)) && !badCells.contains(maze.getCell(currentX - 1, currentY))) {
					back = true;
					return;
				}else if(currentX + 1 <= startCell.getX() + (range + 1) && maze.getCell(currentX + 1, currentY).getType() == CellType.PATH && !(currentX + 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX + 1, currentY)) && !badCells.contains(maze.getCell(currentX + 1, currentY))) {
					back = true;
					return;
				}
			}else if(beenTo.contains(maze.getCell(currentX + 1, currentY))) {
				beenTo.remove(maze.getCell(currentX, currentY));
				badCells.add(maze.getCell(currentX, currentY));
				currentX++;
				if(currentY - 1 >= startCell.getY() - (range + 1) && maze.getCell(currentX, currentY - 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY - 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY - 1)) && !badCells.contains(maze.getCell(currentX, currentY - 1))) {
					back = true;
					return;
				}else if(currentY + 1 <= startCell.getY() + (range + 1) && maze.getCell(currentX, currentY + 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY + 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY + 1)) && !badCells.contains(maze.getCell(currentX, currentY + 1))) {
					back = true;
					return;
				}else if(currentX - 1 >= startCell.getX() - (range + 1) && maze.getCell(currentX - 1, currentY).getType() == CellType.PATH && !(currentX - 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX - 1, currentY)) && !badCells.contains(maze.getCell(currentX - 1, currentY))) {
					back = true;
					return;
				}else if(currentX + 1 <= startCell.getX() + (range + 1) && maze.getCell(currentX + 1, currentY).getType() == CellType.PATH && !(currentX + 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX + 1, currentY)) && !badCells.contains(maze.getCell(currentX + 1, currentY))) {
					back = true;
					return;
				}
			}else {
				beenTo.remove(maze.getCell(currentX, currentY));
				badCells.add(maze.getCell(currentX, currentY));
				currentX = startCell.getX();
				currentY = startCell.getY();
				if(currentY - 1 >= startCell.getY() - (range + 1) && maze.getCell(currentX, currentY - 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY - 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY - 1)) && !badCells.contains(maze.getCell(currentX, currentY - 1))) {
					
				}else if(currentY + 1 <= startCell.getY() + (range + 1) && maze.getCell(currentX, currentY + 1).getType() == CellType.PATH && !(currentX == startCell.getX() && currentY + 1 == startCell.getY()) && !beenTo.contains(maze.getCell(currentX, currentY + 1)) && !badCells.contains(maze.getCell(currentX, currentY + 1))) {
					
				}else if(currentX - 1 >= startCell.getX() - (range + 1) && maze.getCell(currentX - 1, currentY).getType() == CellType.PATH && !(currentX - 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX - 1, currentY)) && !badCells.contains(maze.getCell(currentX - 1, currentY))) {
					
				}else if(currentX + 1 <= startCell.getX() + (range + 1) && maze.getCell(currentX + 1, currentY).getType() == CellType.PATH && !(currentX + 1 == startCell.getX() && currentY == startCell.getY()) && !beenTo.contains(maze.getCell(currentX + 1, currentY)) && !badCells.contains(maze.getCell(currentX + 1, currentY))) {
					
				}else {
					beenTo.clear();
					foundPath = true;
				}
				back = true;
				return;
			}
		}
	}
	
	public ArrayList<Cell> getPath(){
		//return actualPath;
		return path;
	}
	
}
