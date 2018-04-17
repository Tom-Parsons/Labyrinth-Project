package tp.maze.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import tp.maze.entities.ItemEntity;
import tp.maze.entities.Monster;
import tp.maze.entities.MonsterType;
import tp.maze.entities.Player;
import tp.maze.game.Maze;
import tp.maze.game.Particle;
import tp.maze.game.PauseMenu;
import tp.maze.game.Torch;
import tp.maze.game.UserInterface;
import tp.maze.items.Item;
import tp.maze.items.ItemType;
import tp.maze.items.Items;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public boolean running = false;
	private Thread thread;
	
	public void START() {start();}
	
	private synchronized void start() {
		if(running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop() {
		if(!running)
			return;
		
		if(GameInfo.gameState == GameInfo.GameState.GAME) FH.saveGame(SAVE_FILE);
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	public void StopProgram() {
		stop();
	}
	
	public static BufferedImage gravelPath = null;
	
	public void init() {
		requestFocus();
		addKeyListener(new KeyInput(this));
		addMouseListener(new MouseInput(this));
		
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			gravelPath = loader.loadImage("/path.png");
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		Menu.setupMenu(this);
		
		GameInfo.gameState = GameInfo.GameState.MENU;
	}
	
	private UserInterface userInterface;
	public static FileHandler FH = null;
	public static int SAVE_FILE = 0;
	
	public void initializeGame(int save) {
		SAVE_FILE = save;
		GameInfo.PLAYER = new Player(null, 7, 7);
		GameInfo.MAZE = new Maze(GameInfo.PLAYER);
		GameInfo.PLAYER.init(GameInfo.MAZE);
		tp.maze.game.Map.init(GameInfo.PLAYER, GameInfo.MAZE);
		userInterface = new UserInterface(GameInfo.PLAYER, GameInfo.MAZE);
		FH = new FileHandler(GameInfo.PLAYER, GameInfo.MAZE);
		FH.saveGame(SAVE_FILE);
		GameInfo.gameState = GameInfo.GameState.GAME;
	}
	public void initializeLoad(int save) {
		FH = new FileHandler(GameInfo.PLAYER, GameInfo.MAZE);
		GameInfo.gameState = GameInfo.GameState.LOADING;
		SAVE_FILE = save;
		GameInfo.PLAYER = new Player(null, 7, 7);
		GameInfo.MAZE = new Maze(GameInfo.PLAYER);
		GameInfo.PLAYER.init(GameInfo.MAZE);
		tp.maze.game.Map.init(GameInfo.PLAYER, GameInfo.MAZE);
		userInterface = new UserInterface(GameInfo.PLAYER, GameInfo.MAZE);
		FH.init(GameInfo.PLAYER, GameInfo.MAZE);
		FH.loadGame(SAVE_FILE);
		GameInfo.gameState = GameInfo.GameState.GAME;
	}
	
	@Override
	public void run() {
		init();
		long lastTime = System.nanoTime();
		double ns = 1000000000 / GameInfo.TICKS;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " Ticks, FPS: " + frames);
				updates = 0;
				frames = 0;
			}
			
		}
	}
	
	public static void Save() {
		FH = new FileHandler(GameInfo.PLAYER, GameInfo.MAZE);
		FH.saveGame(SAVE_FILE);
	}
	
	private int saveTicks = 0;
	
	private void tick() {
		if(GameInfo.gameState == GameInfo.GameState.GAME) {
			if(!GameInfo.PAUSED && GameInfo.PLAYER.getOpenInventory() == null) {
				if(!GameInfo.PLAYER.isDead()) {
					if(Key_W_Down) {
						GameInfo.PLAYER.setVelY(-GameInfo.PLAYER.getSpeed());
					}else if(Key_S_Down) {
						GameInfo.PLAYER.setVelY(GameInfo.PLAYER.getSpeed());
					}else {
						GameInfo.PLAYER.setVelY(0);
					}
					if(Key_A_Down) {
						GameInfo.PLAYER.setVelX(-GameInfo.PLAYER.getSpeed());
					}else if(Key_D_Down) {
						GameInfo.PLAYER.setVelX(GameInfo.PLAYER.getSpeed());
					}else {
						GameInfo.PLAYER.setVelX(0);
					}
					if(Key_Space_Down) GameInfo.PLAYER.attack();
					GameInfo.PLAYER.tick();
					GameInfo.MAZE.tick();
					EventLog.tick();
				}
				userInterface.tick();
			}else if(GameInfo.PAUSED) {
				PauseMenu.tick();
			}
			saveTicks++;
			if(saveTicks >= 60 * 60) {
				saveTicks = 0;
				Save();
			}
			
			//ZOOMING MAP
			if(GameInfo.currentTileSize < GameInfo.maxTileSize) {
				GameInfo.currentTileSize += (int)Math.ceil((GameInfo.maxTileSize - GameInfo.currentTileSize) / 10);
			}else if(GameInfo.currentTileSize > GameInfo.maxTileSize) {
				GameInfo.currentTileSize -= (int)Math.ceil((GameInfo.currentTileSize - GameInfo.maxTileSize) / 10);
			}
		}else if(GameInfo.gameState == GameInfo.GameState.MENU || GameInfo.gameState == GameInfo.GameState.LOADING) {
			if(SAVE_FILE == 0) Menu.tick();
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		/////////////DRAW/////////////
		
			//Background
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			if(GameInfo.gameState == GameInfo.GameState.GAME) {
				GameInfo.MAZE.render(g);
				GameInfo.PLAYER.render(g);
				userInterface.render(g);
				EventLog.render(g);
				tp.maze.game.Map.render(g);
				if(GameInfo.PAUSED) {
					//RENDER PAUSE SCREEN
					PauseMenu.render(g);
				}
			}else if(GameInfo.gameState == GameInfo.GameState.MENU || GameInfo.gameState == GameInfo.GameState.LOADING) {
				Menu.render(g);
			}
		
		//////////////////////////////
		g.dispose();
		bs.show();
	}
	
	public void rightMouseClicked(MouseEvent e) {
		if(GameInfo.PLAYER.getOpenInventory() != null) {
			if(userInterface.getHoveringItem() != -1) {
				if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == -1) {
					//RIGHT CLICKING ITEM
				}
			}
		}
	}
	
	/**
	 * Handling left clicks
	 * @param e The event
	 */
	public void leftMouseClicked(MouseEvent e) {
		if(GameInfo.gameState == GameInfo.GameState.MENU) {
			if(Menu.menuState == Menu.MenuState.MAIN) {
				try {
					if(GameInfo.FRAME.getMousePosition().getX() > Menu.exitButton.getX() 
							&& GameInfo.FRAME.getMousePosition().getX() < Menu.exitButton.getX() + Menu.exitButton.getWidth() 
							&& GameInfo.FRAME.getMousePosition().getY() > Menu.exitButton.getY() + 25
							&& GameInfo.FRAME.getMousePosition().getY() < Menu.exitButton.getY() + Menu.exitButton.getHeight() + 25) {
						stop();
						return;
					}
				}catch(Exception ex) {}
			}
			Menu.leftClick(e);
		}else if(GameInfo.gameState == GameInfo.GameState.GAME) {
			if(GameInfo.PAUSED) {
				PauseMenu.leftClick(e);
				return;
			}
			if(GameInfo.PLAYER.getOpenInventory() != null) {
				if(userInterface.getHoveringItem() != -1) {
					if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == -1) {
						try {
							if(userInterface.getHoveringItem() < 100) {
								if(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() != ItemType.AIR 
										&& !GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getID().equals("Fists")) {
									GameInfo.PLAYER.getOpenInventory().setSelectedItem(userInterface.getHoveringItem());
								}
							}else if(userInterface.getHoveringItem() == 100){
								if(!GameInfo.PLAYER.getActiveWeapon().getID().equals("Fists")) {
									GameInfo.PLAYER.getOpenInventory().setSelectedItem(userInterface.getHoveringItem());								
								}
							}else if(userInterface.getHoveringItem() == 200) {
								if(!GameInfo.PLAYER.getActiveArmour().getID().equals("Clothes")) {
									GameInfo.PLAYER.getOpenInventory().setSelectedItem(userInterface.getHoveringItem());								
								}
							}else if(userInterface.getHoveringItem() == 300) {
								if(GameInfo.PLAYER.getActivePotion() != null) {
									GameInfo.PLAYER.getOpenInventory().setSelectedItem(userInterface.getHoveringItem());								
								}
							}
						} catch (Exception e1) {}
					}else {
						try {
							Item item = null;
							if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() < 100){
								if(userInterface.getHoveringItem() < 100) {
									item = GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem());
								}else if(userInterface.getHoveringItem() == 100){
									item = GameInfo.PLAYER.getActiveWeapon();
									if(item == null) {
										item = Items.AIR;
									}
								}else if(userInterface.getHoveringItem() == 200) {
									item = GameInfo.PLAYER.getActiveArmour();
									if(item == null) {
										item = Items.AIR;
									}
								}else if(userInterface.getHoveringItem() == 300) {
									item = GameInfo.PLAYER.getActivePotion();
									if(item == null) {
										item = Items.AIR;
									}
								}
							}else if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == 100){
								item = GameInfo.PLAYER.getActiveWeapon();
							}else if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == 200) {
								item = GameInfo.PLAYER.getActiveArmour();
							}else if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == 300) {
								item = GameInfo.PLAYER.getActivePotion();
							}
							if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() < 100) {
								if(userInterface.getHoveringItem() < 100) {
									GameInfo.PLAYER.getOpenInventory().setItem(userInterface.getHoveringItem(), GameInfo.PLAYER.getOpenInventory().getItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem()));
									GameInfo.PLAYER.getOpenInventory().setItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem(), item);
								}else if(userInterface.getHoveringItem() == 100){
									try {
										if(GameInfo.PLAYER.getOpenInventory().getItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem()).getType() == ItemType.WEAPON) {
											GameInfo.PLAYER.setActiveWeapon(Items.getWeaponFromGameID(GameInfo.PLAYER.getOpenInventory().getItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem()).getGameID()));
											if(!item.getID().equals("Fists")) {
												GameInfo.PLAYER.getOpenInventory().setItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem(), item);
											}else {
												GameInfo.PLAYER.getOpenInventory().setItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem(), Items.AIR);
											}
										}
									}catch(Exception ex) {}
								}else if(userInterface.getHoveringItem() == 200){
									try {
										if(GameInfo.PLAYER.getOpenInventory().getItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem()).getType() == ItemType.ARMOUR) {
											GameInfo.PLAYER.setActiveArmour(Items.getArmourFromGameID(GameInfo.PLAYER.getOpenInventory().getItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem()).getGameID()));
											if(!item.getID().equals("Clothes")) {
												GameInfo.PLAYER.getOpenInventory().setItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem(), item);
											}else {
												GameInfo.PLAYER.getOpenInventory().setItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem(), Items.AIR);
											}
										}
									}catch(Exception ex) {}
								}else if(userInterface.getHoveringItem() == 300){
									try {
										if(GameInfo.PLAYER.getOpenInventory().getItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem()).getType() == ItemType.POTION) {
											GameInfo.PLAYER.setActivePotion(Items.getPotionFromGameID(GameInfo.PLAYER.getOpenInventory().getItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem()).getGameID()));
											if(item != null) {
												GameInfo.PLAYER.getOpenInventory().setItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem(), item);
											}else {
												GameInfo.PLAYER.getOpenInventory().setItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem(), Items.AIR);
											}
										}
									}catch(Exception ex) {}
								}
							}else if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == 100){
								if(userInterface.getHoveringItem() != 100 && userInterface.getHoveringItem() != 200 && userInterface.getHoveringItem() != 300) {
									try {
										if(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() == ItemType.AIR) {
											GameInfo.PLAYER.setActiveWeapon(Items.FISTS);
										}else {
											GameInfo.PLAYER.setActiveWeapon(Items.getWeaponFromGameID(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getGameID()));
										}
										GameInfo.PLAYER.getOpenInventory().setItem(userInterface.getHoveringItem(), item);
									}catch(Exception ex) {}
								}
							}else if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == 200){
								if(userInterface.getHoveringItem() != 200 && userInterface.getHoveringItem() != 100 && userInterface.getHoveringItem() != 300) {
									try {
										if(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() == ItemType.AIR) {
											GameInfo.PLAYER.setActiveArmour(Items.CLOTHES);
										}else {
											if(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() == ItemType.ARMOUR) {
												GameInfo.PLAYER.setActiveArmour(Items.getArmourFromGameID(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getGameID()));
											}
										}
										if(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() == ItemType.ARMOUR || GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() == ItemType.AIR) {
											GameInfo.PLAYER.getOpenInventory().setItem(userInterface.getHoveringItem(), item);
										}
									}catch(Exception ex) {}
								}
							}else if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == 300){
								if(userInterface.getHoveringItem() != 200 && userInterface.getHoveringItem() != 100 && userInterface.getHoveringItem() != 300) {
									try {
										if(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() == ItemType.AIR) {
											GameInfo.PLAYER.setActivePotion(null);
										}else {
											if(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() == ItemType.POTION) {
												GameInfo.PLAYER.setActivePotion(Items.getPotionFromGameID(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getGameID()));
											}
										}
										if(GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() == ItemType.POTION || GameInfo.PLAYER.getOpenInventory().getItem(userInterface.getHoveringItem()).getType() == ItemType.AIR) {
											GameInfo.PLAYER.getOpenInventory().setItem(userInterface.getHoveringItem(), item);
										}
									}catch(Exception ex) {}
								}
							}
							GameInfo.PLAYER.getOpenInventory().setSelectedItem(-1);
						} catch (Exception e2) {e2.printStackTrace();}
					}
				}else {
					if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() != -1) {
						try {
							if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() < 100) {
								new ItemEntity(GameInfo.MAZE, GameInfo.PLAYER.getXPos() + 0.165, GameInfo.PLAYER.getYPos() + 0.165, GameInfo.PLAYER, GameInfo.PLAYER.getOpenInventory().getItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem()));
								GameInfo.PLAYER.getOpenInventory().setItem(GameInfo.PLAYER.getOpenInventory().getSelectedItem(), Items.AIR);
							}else if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == 100){
								new ItemEntity(GameInfo.MAZE, GameInfo.PLAYER.getXPos() + 0.165, GameInfo.PLAYER.getYPos() + 0.165, GameInfo.PLAYER, GameInfo.PLAYER.getActiveWeapon());
								GameInfo.PLAYER.setActiveWeapon(Items.FISTS);
							}else if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == 200){
								new ItemEntity(GameInfo.MAZE, GameInfo.PLAYER.getXPos() + 0.165, GameInfo.PLAYER.getYPos() + 0.165, GameInfo.PLAYER, GameInfo.PLAYER.getActiveArmour());
								GameInfo.PLAYER.setActiveArmour(Items.CLOTHES);
							}else if(GameInfo.PLAYER.getOpenInventory().getSelectedItem() == 300){
								new ItemEntity(GameInfo.MAZE, GameInfo.PLAYER.getXPos() + 0.165, GameInfo.PLAYER.getYPos() + 0.165, GameInfo.PLAYER, GameInfo.PLAYER.getActivePotion());
								GameInfo.PLAYER.setActivePotion(null);
							}
							GameInfo.PLAYER.getOpenInventory().setSelectedItem(-1);
						} catch (Exception e1) {}
					}
				}
			}
		}
	}
	
	public static boolean Key_W_Down = false;
	public static boolean Key_A_Down = false;
	public static boolean Key_S_Down = false;
	public static boolean Key_D_Down = false;
	public static boolean Key_Space_Down = false;
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_B) {
			if(GameInfo.maxTileSize <= 150) GameInfo.maxTileSize += 5;
		}else if(key == KeyEvent.VK_V) {
			if(GameInfo.maxTileSize >= 10) GameInfo.maxTileSize -= 5;
		}else if(key == KeyEvent.VK_K) {
			GameInfo.maxTileSize = -75;
		}else if(key == KeyEvent.VK_F1) {
			GameInfo.USER_INTERFACE = !GameInfo.USER_INTERFACE;
			EventLog.newEvent("User Interface: " + GameInfo.USER_INTERFACE);
		}else if(key == KeyEvent.VK_F2) {
			GameInfo.LIGHTING = !GameInfo.LIGHTING;
			EventLog.newEvent("Lighting: " + GameInfo.LIGHTING);
		}else if(key == KeyEvent.VK_F3) {
			GameInfo.PLAYER.setStatViewMode(!GameInfo.PLAYER.isStatViewMode());
		}else if(key == KeyEvent.VK_F4) {
			GameInfo.ENABLE_ENEMIES = !GameInfo.ENABLE_ENEMIES;
			GameInfo.MAZE.monsters.clear();
			EventLog.newEvent("Enemy Spawning: " + GameInfo.ENABLE_ENEMIES);
		}else if(key == KeyEvent.VK_F5) {
			GameInfo.BOUNDING_BOXES = !GameInfo.BOUNDING_BOXES;
			EventLog.newEvent("Bounding Boxes: " + GameInfo.BOUNDING_BOXES);
		}else if(key == KeyEvent.VK_F6) {
			GameInfo.COLLISIONS = !GameInfo.COLLISIONS;
			EventLog.newEvent("Collisions: " + GameInfo.COLLISIONS);
		}else if(key == KeyEvent.VK_F7) {
			GameInfo.MAZE.items.clear();
			EventLog.newEvent("Cleared items on the ground");
		}else if(key == KeyEvent.VK_F8) {
			GameInfo.ENEMY_AI = !GameInfo.ENEMY_AI;
			EventLog.newEvent("Enemy AI: " + GameInfo.ENEMY_AI);
		}
		
		if(key == KeyEvent.VK_D) {
			Key_D_Down = true;
		}else if(key == KeyEvent.VK_A) {
			Key_A_Down = true;
		}else if(key == KeyEvent.VK_W) {
			Key_W_Down = true;
		}else if(key == KeyEvent.VK_S) {
			Key_S_Down = true;
		}else if(key == KeyEvent.VK_SHIFT) {
			GameInfo.PLAYER.setSprinting(true);
		}else if(key == KeyEvent.VK_M) {
			if(GameInfo.PLAYER.hasMap() || GameInfo.PLAYER.hasEnchantedMap()) {
				GameInfo.PLAYER.setViewingMap(!GameInfo.PLAYER.isViewingMap());
			}else {
				GameInfo.PLAYER.setViewingMap(false);
			}
		}else if(key == KeyEvent.VK_SPACE) {
			Key_Space_Down = true;
		}else if(key == KeyEvent.VK_Q) {
			GameInfo.PLAYER.usePotion();
		}else if(key == KeyEvent.VK_U) {
			Random rnd = new Random();
			for(int x = 0; x < 10; x++) {
				new Particle(GameInfo.MAZE, GameInfo.PLAYER, GameInfo.PLAYER.getXPos() + 0.165, GameInfo.PLAYER.getYPos() + 0.165,  -0.05 + (0.05 - -0.05) * rnd.nextDouble(), -0.05 + (0.05 - -0.05) * rnd.nextDouble(), 5, new Color(rnd.nextInt(255 - 0 + 1) + 0, rnd.nextInt(255 - 0 + 1) + 0, rnd.nextInt(255 - 0 + 1) + 0, 255));
			}
		}else if(key == KeyEvent.VK_R) {
			Items.spawnRandomItem(GameInfo.MAZE, GameInfo.PLAYER, GameInfo.PLAYER.getXPos() + 0.165, GameInfo.PLAYER.getYPos() + 0.165, GameInfo.PLAYER.getLevel());
		}else if(key == KeyEvent.VK_E) {
			if(GameInfo.PLAYER.isNearItem()) {
				for(ItemEntity item : GameInfo.MAZE.items) {
					if(item.getXPos() > GameInfo.PLAYER.getXPos() - 0.5 && item.getXPos() < GameInfo.PLAYER.getXPos() + 0.5 && item.getYPos() > GameInfo.PLAYER.getYPos() - 0.5 && item.getYPos() < GameInfo.PLAYER.getYPos() + 0.5) {
						if(GameInfo.PLAYER.getInventory().addItem(item.getItem())) {
							String firstChar = item.getItem().getName().toLowerCase().substring(0, 1);
							if(firstChar.equals("a") || firstChar.equals("e") || firstChar.equals("i") || firstChar.equals("o") || firstChar.equals("u")) {
								EventLog.newEvent("Picked up an " + item.getItem().getName());
							}else {
								EventLog.newEvent("Picked up a " + item.getItem().getName());
							}
							item.die();
						}
						break;
					}
				}
			}
		}else if(key == KeyEvent.VK_I) {
			if(GameInfo.PLAYER.getOpenInventory() == null) {
				GameInfo.PLAYER.openInventory(GameInfo.PLAYER.getInventory());
			}else {
				GameInfo.PLAYER.closeInventory();
			}
		}else if(key == KeyEvent.VK_UP) {
			Monster m = new Monster(GameInfo.MAZE, GameInfo.PLAYER.getCellX(), GameInfo.PLAYER.getCellY(), GameInfo.PLAYER, MonsterType.GOBLIN);
			GameInfo.MAZE.monsters.add(m);
		}else if(key == KeyEvent.VK_T) {
			GameInfo.MAZE.torches.add(new Torch(GameInfo.PLAYER, GameInfo.MAZE, GameInfo.PLAYER.getXPos(), GameInfo.PLAYER.getYPos()));
		}else if(key == KeyEvent.VK_X) {
			GameInfo.PLAYER.setLevel(Integer.MAX_VALUE / 2);
			GameInfo.PLAYER.setHealth(GameInfo.PLAYER.getMaxHealth());
		}else if(key == KeyEvent.VK_ESCAPE) {
			GameInfo.PAUSED = !GameInfo.PAUSED;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_D) {
			Key_D_Down = false;
		}else if(key == KeyEvent.VK_A) {
			Key_A_Down = false;
		}else if(key == KeyEvent.VK_W) {
			Key_W_Down = false;
		}else if(key == KeyEvent.VK_S) {
			Key_S_Down = false;
		}else if(key == KeyEvent.VK_SPACE){
			Key_Space_Down = false;
		}else if(key == KeyEvent.VK_SHIFT) {
			GameInfo.PLAYER.setSprinting(false);
		}
		
	}
	
}
