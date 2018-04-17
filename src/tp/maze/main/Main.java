package tp.maze.main;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		Game game = new Game();
		
		game.setPreferredSize(new Dimension(GameInfo.WIDTH * GameInfo.SCALE, GameInfo.HEIGHT * GameInfo.SCALE));
		game.setMaximumSize(new Dimension(GameInfo.WIDTH * GameInfo.SCALE, GameInfo.HEIGHT * GameInfo.SCALE));
		game.setMinimumSize(new Dimension(GameInfo.WIDTH * GameInfo.SCALE, GameInfo.HEIGHT * GameInfo.SCALE));
		
		JFrame frame;
		
		frame = new JFrame(GameInfo.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		GameInfo.FRAME = frame;
		
		game.START();
	}
	
}
