package chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.move.Move;
import chess.move.MoveState;
import chess.pieces.Piece;

public class ChessPanels{
	
	private final JFrame gameWindow;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	private Piece movePiece;
	private MoveState moveState;
	
	public ChessPanels() {
		this.gameWindow = new JFrame("Sid's Chess App");
		this.chessBoard = Board.createStandardBoard();
		this.boardPanel = new BoardPanel();
		this.moveState = MoveState.CHOOSE;
		this.movePiece = null;
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
	
	private void updateBoard(Board board) {
		this.chessBoard = board;
	}
	
	private class BoardPanel extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private List<TilePanel> boardTiles;
		
		BoardPanel(){
			super(new GridBagLayout());
			setBackground(Color.BLACK);
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
			this.boardTiles = generateTiles();
			GridBagConstraints gb = new GridBagConstraints();
			for(TilePanel tile : boardTiles) {
				gb.gridx = BoardUtil.fToR[tile.getTileFile()];
				gb.gridy = BoardUtil.rToC[tile.getTileRank()];
				add(tile,gb);
			}
			validate();
			repaint();
		}
		

	}
	
	private class TilePanel extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private final int tileFile;
		private final int tileRank;
		
		private Color lightTileColor = new Color(222,227, 230);
		private Color darkTileColor = new Color(140, 162, 173);
		private Color borderColor = new Color(255, 0, 0);
		private Color legalMoveColor = new Color(1f,0f,0f,0.3f);
		
		TilePanel(int tileFile,int tileRank){	
			super(new BorderLayout());
			this.tileFile = tileFile;
			this.tileRank = tileRank;
			assignTileColor();
			add(pieceIcon(),BorderLayout.CENTER);
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
			        if(SwingUtilities.isLeftMouseButton(e)) {
			        	if(moveState == MoveState.CHOOSE) {
			        		movePiece = getBoard().getPiece(getTileFile(),getTileRank());
			        		moveState = MoveState.MOVE;
			        		boardPanel.drawBoard();
			        	}
			        }
			    }
			});
		}
		
		private void assignTileColor() {
			if((getTileRank() % 2) == 0) {
				this.setBackground((getTileFile() % 2) == 0 ? darkTileColor : lightTileColor);
			}else {
				this.setBackground((getTileFile() % 2) == 0 ? lightTileColor : darkTileColor);
			}
			setMoveTileColor();
			setLegalMoveColor();
		}
		
		private void setMoveTileColor() {
			if(movePiece != null) {
				if((movePiece.getFile() == this.getTileFile()) && (movePiece.getRank() == this.getTileRank()) && (moveState == MoveState.MOVE)) {
					this.setBorder(new LineBorder(borderColor,4));
				}
			}
		}
		
		private void setLegalMoveColor() {
			if(movePiece != null) {
				for(Move move : movePiece.getLegalMoves(getBoard())) {
					if((move.getMoveFile() == this.getTileFile()) && (move.getMoveRank() == this.getTileRank()) && (moveState == MoveState.MOVE)) {
						this.setBackground(legalMoveColor);
					}
				}
			}
			
		}
		
		@Override
        public Dimension getPreferredSize() {
            return new Dimension(80, 80);
        }
		
		public JLabel pieceIcon() {
			
			Piece piece = getBoard().getPiece(getTileFile(), getTileRank());
			ImageIcon pieceImage = new ImageIcon("images/"+piece.getPieceColor().getColorString()+piece.getPieceType().getPieceTypeString()+".png");
			
			JLabel label = new JLabel(pieceImage,SwingConstants.CENTER);
			label.setPreferredSize(new Dimension(80,80));
			
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

