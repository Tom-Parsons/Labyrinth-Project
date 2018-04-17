package tp.maze.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;

import tp.maze.entities.ItemEntity;
import tp.maze.entities.Monster;
import tp.maze.entities.MonsterType;
import tp.maze.entities.Player;
import tp.maze.game.Cell;
import tp.maze.game.CellType;
import tp.maze.game.Chunk;
import tp.maze.game.Maze;
import tp.maze.game.Torch;
import tp.maze.items.Armour;
import tp.maze.items.Chest;
import tp.maze.items.Damage;
import tp.maze.items.Inventory;
import tp.maze.items.Item;
import tp.maze.items.ItemType;
import tp.maze.items.Items;
import tp.maze.items.Potion;
import tp.maze.items.PotionType;
import tp.maze.items.Weapon;

public class FileHandler {

	private Player p;
	private Maze maze;
	
	public FileHandler(Player p, Maze maze) {
		this.p = p;
		this.maze = maze;
	}
	
	public void init(Player p, Maze maze) {
		this.p = p;
		this.maze = maze;
	}
	
	public static File SAVE_LOCATION = new File(System.getProperty("user.dir") + "/Save Files");
	
	public static boolean hasSaveFile() {
		File save1 = new File(SAVE_LOCATION.getAbsolutePath() + "/Save1.txt");
		File save2 = new File(SAVE_LOCATION.getAbsolutePath() + "/Save2.txt");
		File save3 = new File(SAVE_LOCATION.getAbsolutePath() + "/Save3.txt");
		if(save1.exists() || save2.exists() || save3.exists()) return true;
		return false;
	}
	public static boolean hasSave(int save) {
		File saveFile = new File(SAVE_LOCATION.getAbsolutePath() + "/Save" + save + ".txt");
		if(saveFile.exists()) return true;
		return false;
	}
	public static String saveLastModified(int save) {
		if(hasSave(save)) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
			return formatter.format(new File(SAVE_LOCATION.getAbsolutePath() + "/Save" + save + ".txt").lastModified());
		}
		return "ERROR";
	}
	
	private static boolean saving = false;
	public static boolean isSaving() {
		return saving;
	}
	
	public static void deleteSave(int save) {
		File output = new File(SAVE_LOCATION.getAbsolutePath() + "/Save" + save + ".txt");
		if(output.exists()) {
			output.delete();
		}
	}
	
	public ArrayList<String> GAME_DATA = new ArrayList<String>();
	
	public void saveGame(int save) {
		saving = true;
		for(Weapon w : Items.activeWeapons) {
			GAME_DATA.add("Weapon:ID=" + w.getID() + ",GameID=" + w.getGameID() + ",Level=" + w.getLevel());
		}
		for(Armour a : Items.activeArmour) {
			GAME_DATA.add("Armour:ID=" + a.getID() + ",GameID=" + a.getGameID() + ",Level=" + a.getLevel() + ",Health_Increase=" + a.getHealthIncrease());
		}
		for(Potion p : Items.activePotions) {
			GAME_DATA.add("Potion:ID=" + p.getID() + ",GameID=" + p.getGameID() + ",Level=" + p.getLevel() + ",Bonus=" + p.getBonus() + ",Potion_Type=" + p.getPotionType() + ",Uses=" + p.getUses());
		}
		for(Item i : Items.activeItems) {
			if(i.getType() == ItemType.ITEM) {
				GAME_DATA.add("Item:ID=" + i.getID() + ",GameID=" + i.getGameID());
			}
		}
		Potion activePotion = p.getActivePotion();
		String potionID = "NULL";
		if(activePotion != null) potionID = activePotion.getGameID();
		GAME_DATA.add("Player:Health=" + p.getHealth() + ",MaxHealth=" + p.getMaxHealth() + ",XPos=" + p.getXPos() + ",YPos=" + p.getYPos()
		+ ",Direction=" + p.getDirection() + ",Level=" + p.getLevel() + ",XP=" + p.getXP() + ",Speed=" + p.getSpeed()
		+ ",Active_Weapon=" + p.getActiveWeapon().getGameID() + ",Active_Armour=" + p.getActiveArmour().getGameID()
		+ ",Active_Potion=" + potionID);
		String inventoryData = "";
		int inventoryIndex = 0;
		for(Item i : p.getInventory().getContents()) {
			if(inventoryData.equals("")) {
				inventoryData = inventoryIndex + "=" + i.getGameID();
			}else {
				inventoryData = inventoryData + "," + inventoryIndex + "=" + i.getGameID();
			}
			inventoryIndex++;
		}
		GAME_DATA.add("Player_Inventory:" + inventoryData);
		for(Chunk c : maze.chunks) {
			String chunkData = "";
			for(int x = 0; x < 16; x++) {
				for(int y = 0; y < 16; y++) {
					Cell cell = c.getCells()[x][y];
					if(chunkData.equals("")) {
						chunkData = "Chunk_Loc=" + x + "-" + y + ",X=" + cell.getX() + ",Y=" + cell.getY() + ",Type=" + cell.getType();
					}else {
						chunkData = chunkData + ":Chunk_Loc=" + x + "-" + y + ",X=" + cell.getX() + ",Y=" + cell.getY() + ",Type=" + cell.getType() + ",Darkness=" + cell.getDarkness() + ",Discovered=" + cell.isDiscovered() + ",Has_Light=" + cell.doesHaveLight();
					}
				}
			}
			GAME_DATA.add("Chunk>" + c.getX() + "," + c.getY() + ":NorthX=" + c.getNorthX() + ",SouthX=" + c.getSouthX()
			+ ",EastY=" + c.getEastY() + ",WestY=" + c.getWestY() + ":" + chunkData);
		}
		for(Monster m : maze.monsters) {
			GAME_DATA.add("Monster:Health=" + m.getHealth() + ",MaxHealth=" + m.getMaxHealth() + ",XPos=" + m.getXPos()
			+ ",YPos=" + m.getYPos() + ",Direction=" + m.getDirection() + ",Level=" + m.getLevel() + ",Type=" + m.getType());
		}
		for(ItemEntity ie : maze.items) {
			GAME_DATA.add("ItemEntity:GameID=" + ie.getItem().getGameID() + ",XPos=" + ie.getXPos() + ",YPos=" + ie.getYPos());
		}
		for(Chest c : maze.chests) {
			GAME_DATA.add("Chest:Level=" + c.getLevel()+ ",X=" + c.getX() + ",Y=" + c.getY());
		}
		for(Torch t : maze.torches) {
			GAME_DATA.add("Torch:X=" + t.getX() + ",Y=" + t.getY());
		}
		if(!SAVE_LOCATION.exists()) SAVE_LOCATION.mkdir();
		File output = new File(SAVE_LOCATION.getAbsolutePath() + "/Save" + save + ".txt");
		if(output.exists()) {
			output.delete();
		}
		try {
			output.createNewFile();
		} catch (IOException e) {}
		try {
			FileOutputStream fos = new FileOutputStream(output);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			for(int i = 0; i < GAME_DATA.size(); i++) {
				bw.write(encrypt(GAME_DATA.get(i)));
				//bw.write(GAME_DATA.get(i));
				bw.newLine();
			}
			bw.close();
			osw.close();
			fos.close();
		} catch (IOException e) {}
		saving = false;
	}
	
	private int currentLine = 0;
	private int maxLines = 0;
	public int getCurrentLine() {
		return currentLine;
	}
	public int getMaxLines() {
		return maxLines;
	}
	
	private String encrypt(String s) {
		StringBuilder SB = new StringBuilder();
		SB.append(s);
		SB.reverse();
		byte[] bytesEncoded = Base64.getEncoder().encode(SB.toString().getBytes());
		return new String(bytesEncoded);
	}
	
	private String decrypt(String s) {
		byte[] bytesDecoded = Base64.getDecoder().decode(s.getBytes());
		StringBuilder SB = new StringBuilder();
		SB.append(new String(bytesDecoded));
		SB.reverse();
		return SB.toString();
	}
	
	public void loadGame(int save) {
		File file = new File(SAVE_LOCATION.getAbsolutePath() + "/Save" + save + ".txt");
		ArrayList<String> LOADED_GAME_DATA = new ArrayList<String>();
		if(file.exists()) {
			try{
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while((line = br.readLine()) != null) {
					LOADED_GAME_DATA.add(line);
				}
				fr.close();
				br.close();
			}catch(Exception ex) {}
		}
		maxLines = LOADED_GAME_DATA.size();
		GameInfo.gameState = GameInfo.GameState.LOADING;
		maze.chunks.clear();
		maze.items.clear();
		maze.monsters.clear();
		maze.particles.clear();
		maze.torches.clear();
		maze.chests.clear();
		Items.activeItems.clear();
		Items.activeArmour.clear();
		Items.activePotions.clear();
		Items.activeWeapons.clear();
		for(String s : LOADED_GAME_DATA) {
			s = decrypt(s);
			try {
				
				if(s.startsWith("Player:")) {
					String[] dataSplit = s.split(":");
					String[] playerDataSplit = dataSplit[1].split(",");
					for(String data : playerDataSplit) {
						if(data.contains("Health")) {
							p.setHealth(Double.parseDouble(data.split("=")[1]));
						}else if(data.contains("MaxHealth")) {
							p.setMaxHealth(Double.parseDouble(data.split("=")[1]));
						}else if(data.contains("XPos")) {
							p.setXPos(Double.parseDouble(data.split("=")[1]));
						}else if(data.contains("YPos")) {
							p.setYPos(Double.parseDouble(data.split("=")[1]));
						}else if(data.contains("Direction")) {
							p.setDirection(data.split("=")[1]);
						}else if(data.contains("Level")) {
							p.setLevel(Integer.valueOf(data.split("=")[1]));
						}else if(data.contains("XP")) {
							p.setXP(Integer.valueOf(data.split("=")[1]));
						}else if(data.contains("Speed")) {
							p.setSpeed(Double.parseDouble(data.split("=")[1]));
						}else if(data.contains("Active_Weapon")) {
							p.setActiveWeapon(Items.getWeaponFromGameID(data.split("=")[1]));
						}else if(data.contains("Active_Armour")) {
							p.setActiveArmour(Items.getArmourFromGameID(data.split("=")[1]));
						}else if(data.contains("Active_Potion")) {
							if(data.split("=")[1].equals("NULL")) {
								p.setActivePotion(null);
							}else {
								p.setActivePotion(Items.getPotionFromGameID(data.split("=")[1]));
							}
						}
					}
				}else if(s.startsWith("Player_Inventory:")) {
					String[] dataSplit = s.split(":");
					String[] inventoryDataSplit = dataSplit[1].split(",");
					Inventory inventory = null;
					try {
						inventory = new Inventory(15);
					} catch (Exception e) {e.printStackTrace();}
					for(String data : inventoryDataSplit) {
						inventory.setItem(Integer.valueOf(data.split("=")[0]), Items.getItemFromGameID(data.split("=")[1]));
					}
					try {
					p.getInventory().setContents(inventory.getContents());
					}catch(Exception ex) {}
				}else if(s.startsWith("Weapon:")) {
					String[] dataSplit = s.split(":");
					String[] weaponDataSplit = dataSplit[1].split(",");
					String ID = "";
					String GameID = "";
					int Level = 1;
					for(String data : weaponDataSplit) {
						if(data.contains("GameID")) {
							GameID = data.split("=")[1];
						}else if(data.contains("ID")) {
							ID = data.split("=")[1];
						}else if(data.contains("Level")) {
							Level = Integer.valueOf(data.split("=")[1]);
						}
					}
					Weapon loadedWeapon = new Weapon(ID, GameID, Items.getWeaponFromID(ID).getName(), Level, Items.getWeaponFromID(ID).getType(), Items.getWeaponFromID(ID).getRarity(), new Damage(Items.getWeaponFromID(ID).getDamage().getNeutralDamage() * Level * 0.4, Items.getWeaponFromID(ID).getDamage().getFireDamage() * Level * 0.4, 
							Items.getWeaponFromID(ID).getDamage().getWaterDamage() * Level * 0.4, Items.getWeaponFromID(ID).getDamage().getEarthDamage() * Level * 0.4, Items.getWeaponFromID(ID).getDamage().getAirDamage() * Level * 0.4));
					Items.activeWeapons.add(loadedWeapon);
					Items.activeItems.add(loadedWeapon);
				}else if(s.startsWith("Armour:")) {
					String[] dataSplit = s.split(":");
					String[] armourDataSplit = dataSplit[1].split(",");
					String ID = "";
					String GameID = "";
					int Level = 1;
					double HealthIncrease = 0;
					for(String data : armourDataSplit) {
						if(data.contains("GameID")) {
							GameID = data.split("=")[1];
						}else if(data.contains("ID")) {
							ID = data.split("=")[1];
						}else if(data.contains("Level")) {
							Level = Integer.valueOf(data.split("=")[1]);
						}else if(data.contains("Health_Increase")) {
							HealthIncrease = Double.parseDouble(data.split("=")[1]);
						}
					}
					Armour loadedArmour = new Armour(ID, GameID, Items.getArmourFromID(ID).getName(), Level, Items.getArmourFromID(ID).getType(), Items.getArmourFromID(ID).getRarity(), HealthIncrease, new Damage(Items.getArmourFromID(ID).getResistances().getNeutralDamage() * Level * 0.4, Items.getArmourFromID(ID).getResistances().getFireDamage() * Level * 0.4, 
							Items.getArmourFromID(ID).getResistances().getWaterDamage() * Level * 0.4, Items.getArmourFromID(ID).getResistances().getEarthDamage() * Level * 0.4, Items.getArmourFromID(ID).getResistances().getAirDamage() * Level * 0.4));
					Items.activeArmour.add(loadedArmour);
					Items.activeItems.add(loadedArmour);
				}else if(s.startsWith("Potion:")) {
					String[] dataSplit = s.split(":");
					String[] armourDataSplit = dataSplit[1].split(",");
					String ID = "";
					String GameID = "";
					int Level = 1;
					double Bonus = 0;
					int Uses = 0;
					PotionType Type = PotionType.HEALTH;
					for(String data : armourDataSplit) {
						if(data.contains("GameID")) {
							GameID = data.split("=")[1];
						}else if(data.contains("ID")) {
							ID = data.split("=")[1];
						}else if(data.contains("Level")) {
							Level = Integer.valueOf(data.split("=")[1]);
						}else if(data.contains("Bonus")) {
							Bonus = Double.parseDouble(data.split("=")[1]);
						}else if(data.contains("Uses")) {
							Uses = Integer.valueOf(data.split("=")[1]);
						}else if(data.contains("Potion_Type")) {
							Type = PotionType.valueOf(data.split("=")[1]);
						}
					}
					Potion loadedPotion = new Potion(ID, GameID, Items.getPotionFromID(ID).getName(), Level, ItemType.POTION, Type, Items.getPotionFromID(ID).getRarity(), (int) Bonus, Uses);
					Items.activePotions.add(loadedPotion);
					Items.activeItems.add(loadedPotion);
				}else if(s.startsWith("Item:")) {
					String[] dataSplit = s.split(":");
					String[] armourDataSplit = dataSplit[1].split(",");
					String ID = "";
					String GameID = "";
					for(String data : armourDataSplit) {
						if(data.contains("GameID")) {
							GameID = data.split("=")[1];
						}else if(data.contains("ID")) {
							ID = data.split("=")[1];
						}
					}
					Item loadedItem = new Item(ID, GameID, Items.getItemFromID(ID).getName(), Items.getItemFromID(ID).getLevel(), Items.getItemFromID(ID).getType(), Items.getItemFromID(ID).getRarity());
					Items.activeItems.add(loadedItem);
				}else if(s.startsWith("Chunk>")) {
					String[] dataSplit = s.split(":");
					String[] chunkLocation = dataSplit[0].split(">");
					int ChunkX = Integer.valueOf(chunkLocation[1].split(",")[0]);
					int ChunkY = Integer.valueOf(chunkLocation[1].split(",")[1]);
					String[] chunkEdgeLocations = dataSplit[1].split(",");
					int NorthX = Integer.valueOf(chunkEdgeLocations[0].split("=")[1]);
					int SouthX = Integer.valueOf(chunkEdgeLocations[1].split("=")[1]);
					int EastY = Integer.valueOf(chunkEdgeLocations[2].split("=")[1]);
					int WestY = Integer.valueOf(chunkEdgeLocations[3].split("=")[1]);
					Cell[][] ChunkCells = new Cell[16][16];
					for(int i = 2; i < dataSplit.length; i++) {
						String[] cellData = dataSplit[i].split(",");
						int chunkCellX = 0;
						int chunkCellY = 0;
						int X = 0;
						int Y = 0;
						CellType Type = CellType.WALL;
						int Darkness = 255;
						boolean Discovered = false;
						boolean HasLight = false;
						for(String data : cellData) {
							if(data.contains("Chunk_Loc")) {
								chunkCellX = Integer.valueOf(data.split("=")[1].split("-")[0]);
								chunkCellY = Integer.valueOf(data.split("=")[1].split("-")[1]);
							}else if(data.contains("X")) {
								X = Integer.valueOf(data.split("=")[1]);
							}else if(data.contains("Y")) {
								Y = Integer.valueOf(data.split("=")[1]);
							}else if(data.contains("Type")) {
								Type = CellType.valueOf(data.split("=")[1]);
							}else if(data.contains("Darkness")) {
								Darkness = Integer.valueOf(data.split("=")[1]);
							}else if(data.contains("Discovered")) {
								Discovered = Boolean.valueOf(data.split("=")[1]);
							}else if(data.contains("Has_Light")) {
								HasLight = Boolean.valueOf(data.split("=")[1]);
							}
						}
						Cell cell = new Cell(maze, X, Y, Type, false, null, p);
						cell.setDarkness(Darkness);
						cell.setDiscovered(Discovered);
						cell.setHasLight(HasLight);
						ChunkCells[chunkCellX][chunkCellY] = cell;
					}
					Chunk chunk = new Chunk(maze, ChunkX, ChunkY, NorthX, SouthX, EastY, WestY, p, ChunkCells);
					for(int newX = 0; newX < 16 ; newX++) {
						for(int newY = 0; newY < 16; newY++) {
							chunk.getCells()[newX][newY].setChunk(chunk);
						}
					}
					maze.addChunk(chunk);
				}else if(s.startsWith("Monster:")) {
					String[] dataSplit = s.split(":");
					String[] monsterDataSplit = dataSplit[1].split(",");
					double Health = 0;
					double MaxHealth = 0;
					double XPos = 0;
					double YPos = 0;
					String Direction = "North";
					int Level = 1;
					MonsterType Type = MonsterType.GOBLIN;
					for(String data : monsterDataSplit) {
						if(data.contains("MaxHealth")) {
							MaxHealth = Double.parseDouble(data.split("=")[1]);
						}else if(data.contains("Health")) {
							Health = Double.parseDouble(data.split("=")[1]);
						}else if(data.contains("XPos")) {
							XPos = Double.parseDouble(data.split("=")[1]);
						}else if(data.contains("YPos")) {
							YPos = Double.parseDouble(data.split("=")[1]);
						}else if(data.contains("Direction")) {
							Direction = data.split("=")[1];
						}else if(data.contains("Level")) {
							Level = Integer.valueOf(data.split("=")[1]);
						}else if(data.contains("Type")) {
							Type = MonsterType.valueOf(data.split("=")[1]);
						}
					}
					Monster m = new Monster(maze, Health, MaxHealth, XPos, YPos, Direction, Level, p, Type);
					maze.monsters.add(m);
				}else if(s.startsWith("ItemEntity:")) {
					String[] dataSplit = s.split(":");
					String[] itemEntityDataSplit = dataSplit[1].split(",");
					String GameID = "";
					double XPos = 0;
					double YPos = 0;
					for(String data : itemEntityDataSplit) {
						if(data.contains("GameID")) {
							GameID = data.split("=")[1];
						}else if(data.contains("XPos")) {
							XPos = Double.parseDouble(data.split("=")[1]);
						}else if(data.contains("YPos")) {
							YPos = Double.parseDouble(data.split("=")[1]);
						}
					}
					ItemEntity ie = new ItemEntity(maze, XPos, YPos, p, Items.getItemFromGameID(GameID));
					maze.items.add(ie);
				}else if(s.startsWith("Chest:")) {
					String[] dataSplit = s.split(":");
					String[] chestDataSplit = dataSplit[1].split(",");
					int Level = 1;
					double XPos = 0;
					double YPos = 0;
					for(String data : chestDataSplit) {
						if(data.contains("Level")) {
							Level = Integer.valueOf(data.split("=")[1]);
						}else if(data.contains("X")) {
							XPos = Double.parseDouble(data.split("=")[1]);
						}else if(data.contains("Y")) {
							YPos = Double.parseDouble(data.split("=")[1]);
						}
					}
					new Chest(Level, XPos, YPos, p, maze);
				}else if(s.startsWith("Torch:")) {
					String[] dataSplit = s.split(":");
					String[] chestDataSplit = dataSplit[1].split(",");
					double XPos = 0;
					double YPos = 0;
					for(String data : chestDataSplit) {
						if(data.contains("X")) {
							XPos = Double.parseDouble(data.split("=")[1]);
						}else if(data.contains("Y")) {
							YPos = Double.parseDouble(data.split("=")[1]);
						}
					}
					Torch t = new Torch(p, maze, XPos, YPos);
					maze.torches.add(t);
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("Error on line " + currentLine);
			}
			currentLine++;
		}
		p.setChunk(maze.getCell(p.getCellX(), p.getCellY()).getChunk());
		GameInfo.gameState = GameInfo.GameState.GAME;
	}
	
}
