package chess.gui;

import java.io.File;

public class Chess {
	
	public static void main(String[] args) {
		//ChessPanels chess = new ChessPanels();
		//chess.show();
		File file = new File("BB.png");
		System.out.println(file.getAbsolutePath());
		System.out.println(file.exists());
		
	}
}
