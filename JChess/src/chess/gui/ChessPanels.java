package chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.pieces.Piece;

public class ChessPanels{
	
	private final JFrame gameWindow;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	
	public ChessPanels() {
		this.gameWindow = new JFrame("Sid's Chess App");
		this.chessBoard = Board.createStandardBoard();
		this.boardPanel = new BoardPanel();
		gameWindow.add(boardPanel);
	}
	
	public void show() {
		getBoardPanel().drawBoard();
		gameWindow.pack();
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
	}
	
	private BoardPanel getBoardPanel() {
		return boardPanel;
	}
	
	private Board getBoard() {
		return chessBoard;
	}
	
	private class BoardPanel extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private final List<TilePanel> boardTiles;
		
		BoardPanel(){
			super(new GridBagLayout());
			setBackground(Color.BLACK);
			this.boardTiles = generateTiles();
		}
		
		List<TilePanel> generateTiles() {
			List<TilePanel> tiles = new ArrayList<TilePanel>();
			for(int i = 7; i > -1; i--) {
				for(int j = 0; j < BoardUtil.FILES; j++) {
					TilePanel tile = new TilePanel(j,i);
					tiles.add(tile);
				}
			}
			return tiles;
		}
		
		void drawBoard() {
			removeAll();
			GridBagConstraints gb = new GridBagConstraints();
			for(TilePanel tile : boardTiles) {
				gb.gridx = BoardUtil.fToR[tile.getTileFile()];
				gb.gridy = BoardUtil.rToC[tile.getTileRank()];
				add(tile,gb);
			}
		}
		

	}
	
	private class TilePanel extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private final int tileFile;
		private final int tileRank;
		
		private Color lightTileColor = new Color(217, 202, 169);
		private Color darkTileColor = new Color(10, 15, 12);
		
		TilePanel(int tileFile,int tileRank){	
			super(new BorderLayout());
			this.tileFile = tileFile;
			this.tileRank = tileRank;
			assignTileColor();
			add(pieceIcon(),BorderLayout.CENTER);
		}
		
		private void assignTileColor() {
			if((getTileRank() % 2) == 0) {
				this.setBackground((getTileFile() % 2) == 0 ? darkTileColor : lightTileColor);
			}else {
				this.setBackground((getTileFile() % 2) == 0 ? lightTileColor : darkTileColor);
			}
		}
		
		@Override
        public Dimension getPreferredSize() {
            return new Dimension(80, 80);
        }
		
		public JLabel pieceIcon() {
			
			Piece piece = getBoard().getPiece(getTileFile(), getTileRank());
			System.out.println((new File("BB.png")).exists());
			ImageIcon pieceImage = new ImageIcon(piece.getPieceColor().getColorString()+piece.getPieceType().getPieceTypeString()+".png");
			
			JLabel label = new JLabel(pieceImage,SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			
			return label;
		}
		
		private int getTileFile() {
			return tileFile;
		}
		
		private int getTileRank() {
			return tileRank;
		}
	}
}

