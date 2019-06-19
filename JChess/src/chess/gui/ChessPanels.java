package chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.move.Move;
import chess.move.MoveState;
import chess.move.PawnPromotion;
import chess.pieces.Piece;
import chess.pieces.PieceType;

public class ChessPanels{

	private final JFrame gameWindow;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	private Piece movePiece;
	private MoveState moveState;
	private MoveLog moveLog;

	public ChessPanels() {
		this.gameWindow = new JFrame("Sid's Chess App");
		this.chessBoard = Board.createStandardBoard();
		//this.chessBoard = Board.createTestBoard();
		JPanel homeOfAllPanels = new JPanel(new BorderLayout());
		this.boardPanel = new BoardPanel();
		this.moveState = MoveState.CHOOSE;
		this.movePiece = null;
		this.moveLog = new MoveLog();
		homeOfAllPanels.add(boardPanel, BorderLayout.CENTER);
		homeOfAllPanels.add(moveLog.getPane(),BorderLayout.EAST);
		gameWindow.add(homeOfAllPanels);
	}

	public void show() {
		getBoardPanel().drawBoard();
		gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
		getBoardPanel().drawBoard();
		if(board.getCurrentPlayer().isCheckMate()) {
			Object[] options = {"New Game",
			"Exit"};
			int value = JOptionPane.showOptionDialog(gameWindow, BoardUtil.oppositeColor(board.getCurrentPlayerColor()).toString()+" wins by checkmate!", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if(value == JOptionPane.YES_OPTION) {
				this.chessBoard = Board.createStandardBoard();
				getBoardPanel().drawBoard();
			}else {
				gameWindow.dispose();
			}
		}else if(board.getCurrentPlayer().isStaleMate()) {
			Object[] options = {"New Game",
			"Exit"};
			int value = JOptionPane.showOptionDialog(gameWindow, BoardUtil.oppositeColor(board.getCurrentPlayerColor()).toString()+" draws by stalemate!", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if(value == JOptionPane.YES_OPTION) {
				this.chessBoard = Board.createStandardBoard();
				getBoardPanel().drawBoard();
			}else {
				gameWindow.dispose();
			}
		}
	}

	private class BoardPanel extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private List<TilePanel> boardTiles;

		BoardPanel(){
			super(new GridBagLayout());
			setBackground(new Color(38,36,33));
		}

		List<TilePanel> generateTiles() {
			List<TilePanel> tiles = new ArrayList<TilePanel>();
			for(int i = (BoardUtil.RANKS-1); i > -1; i--) {
				for(int j = 0; j < BoardUtil.FILES; j++) {
					TilePanel tile = new TilePanel(j,i);
					tile.drawTile();
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

		TilePanel(int tileFile,int tileRank){	
			super(new BorderLayout());
			this.tileFile = tileFile;
			this.tileRank = tileRank;
			assignTileColor();
			pieceIcon();
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						if(!getBoard().getCurrentPlayer().isCheckMate()) {
							if(moveState == MoveState.CHOOSE) {
								movePiece = getBoard().getPiece(getTileFile(),getTileRank());
								boardPanel.drawBoard();
							}else if(moveState == MoveState.MOVE) {
								if(!getBoard().getPiece(tileFile, tileRank).equals(movePiece) && getBoard().getPiece(tileFile, tileRank).getPieceType() != PieceType.EMPTY && getBoard().getPiece(tileFile, tileRank).getPieceColor() == getBoard().getCurrentPlayerColor()) {
									movePiece = getBoard().getPiece(getTileFile(), getTileRank());
								}else if(tileFile == movePiece.getFile() && tileRank == movePiece.getRank()) {
									movePiece = null;
								}
								boardPanel.drawBoard();
							}
							moveState = moveState.nextState(getTileFile(),getTileRank(),movePiece,getBoard());
							if(moveState == MoveState.DONE) {
								if(getBoard().getCurrentPlayer().getColor() == movePiece.getPieceColor()) {
									for(Move move : movePiece.getLegalMoves(getBoard())) {
										if(tileFile == move.getMoveFile() && tileRank == move.getMoveRank() && !getBoard().movesToCheck(move)) {
											if(move.getClass().getName().endsWith("PawnPromotion")){
												PawnPromotion specialMove = (PawnPromotion) move;
												Thread newThread = new Thread(new Runnable() {

													@Override
													public void run() {
														// TODO Auto-generated method stub
														gameWindow.setEnabled(true);
														updateBoard(specialMove.execute());
														moveLog.addMove(specialMove);
													}
												});
												specialMove.setNewThread(newThread);
												specialMove.setGameWindow(gameWindow);
												Thread t = new Thread(specialMove);
												t.start();
												gameWindow.setEnabled(false);
											}else {
												updateBoard(move.execute());
												moveLog.addMove(move);
											}
										}
										//	Board.printBoard(getBoard());
									}
								}
							}
							moveState = moveState.nextState(tileFile, tileRank, movePiece, getBoard());			
						}
					}
				}
			});
		}

		private void drawTile() {
			setMoveTileColor();	
		}

		private void assignTileColor() {
			if((getTileRank() % 2) == 0) {
				this.setBackground((getTileFile() % 2) == 0 ? darkTileColor : lightTileColor);
			}else {
				this.setBackground((getTileFile() % 2) == 0 ? lightTileColor : darkTileColor);
			}
		}

		private void setMoveTileColor() {
			if(movePiece != null && movePiece.getPieceColor() == getBoard().getCurrentPlayerColor()) {
				if((movePiece.getFile() == this.getTileFile()) && (movePiece.getRank() == this.getTileRank())) {
					this.setBorder(new LineBorder(borderColor,4));
				}
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(80, 80);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0.407f, 0.545f, 0.538f, 0.76f));
			int r = 30;
			int width = (this.getWidth() - r)/2;
			int height = (this.getHeight() - r)/2;
			if(movePiece != null && movePiece.getPieceColor() == getBoard().getCurrentPlayerColor()) {
				for(Move move : movePiece.getLegalMoves(getBoard())) {
					if((move.getMoveFile() == this.getTileFile()) && (move.getMoveRank() == this.getTileRank()) && !getBoard().movesToCheck(move)) {
						g2.fillOval(width,height,r,r);
					}
				}
			}
			g2.setColor(this.getBackground() == this.darkTileColor ? lightTileColor : darkTileColor);
			g2.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
			if(getTileFile() == 7) {
				g2.drawString(Integer.toString(BoardUtil.rToNo[getTileRank()]), this.getWidth()-10, 15);
			}
			if(getTileRank() == 0) {
				g2.drawString(BoardUtil.fToA[getTileFile()], this.getWidth()-15, this.getHeight()-5);				
			}
		}

		private void pieceIcon() {

			Piece piece = getBoard().getPiece(getTileFile(), getTileRank());
			ImageIcon pieceImage = null;
			if(piece.getPieceType() != PieceType.EMPTY) {
				pieceImage = new ImageIcon("images/"+piece.getPieceColor().getColorString()+piece.getPieceType().getPieceTypeString()+".png");
				JLabel label = new JLabel(pieceImage,SwingConstants.CENTER);
				label.setPreferredSize(new Dimension(80,80));
				label.setVerticalAlignment(SwingConstants.CENTER);
				label.setOpaque(false);
				this.add(label, BorderLayout.CENTER);
			}
		}

		private int getTileFile() {
			return tileFile;
		}

		private int getTileRank() {
			return tileRank;
		}

	}
}

