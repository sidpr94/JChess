package chess.gui;

import javax.swing.SwingUtilities;

public class Chess {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ChessPanels chess = new ChessPanels();
				chess.show();	
			}
			
		});
	}
}
