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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import chess.Alliance;
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
	private CapturedPiecePanel blackCapturePanel;
	private CapturedPiecePanel whiteCapturePanel;
	private boolean boardFlipped = false;

	public ChessPanels() {
		this.gameWindow = new JFrame("Sid's Chess App");
		this.moveState = MoveState.CHOOSE;
		this.movePiece = null;

		JPanel homeOfAllPanels = new JPanel(new BorderLayout());
		this.boardPanel = new BoardPanel();
		this.blackCapturePanel = new CapturedPiecePanel();
		this.whiteCapturePanel = new CapturedPiecePanel();
		this.moveLog = new MoveLog(this);

		JPanel wardenOfTheEast = new JPanel(new BorderLayout());
		wardenOfTheEast.add(blackCapturePanel.getCapturedPiecePanel(), !boardFlipped ? BorderLayout.NORTH : BorderLayout.SOUTH);
		wardenOfTheEast.add(boardPanel, BorderLayout.CENTER);
		wardenOfTheEast.add(whiteCapturePanel.getCapturedPiecePanel(), !boardFlipped ? BorderLayout.SOUTH : BorderLayout.NORTH);
		homeOfAllPanels.add(wardenOfTheEast, BorderLayout.CENTER);
		homeOfAllPanels.add(moveLog.getPane(),BorderLayout.EAST);

		gameWindow.add(homeOfAllPanels);
	}

	public void show() {
		moveLog.clearAllMoves();
		whiteCapturePanel.removeAllCapturedPieces();
		blackCapturePanel.removeAllCapturedPieces();
		this.chessBoard = Board.createStandardBoard();
		//this.chessBoard = Board.createTestBoard();
		Object[] options = {"White","Black"};
		int value = JOptionPane.showOptionDialog(gameWindow, "Choose a color!", "Game Set-Up", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		if(value == JOptionPane.YES_OPTION) {
			boardFlipped = false;
		}else if(value == JOptionPane.NO_OPTION) {
			boardFlipped = true;
		}else {
			gameWindow.dispose();
		}
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

	public void updateBoard(Board board) {
		this.chessBoard = board;
		getBoardPanel().drawBoard();
		if(moveLog.isLast()) {
			if(board.getCurrentPlayer().isCheckMate()) {
				Object[] options = {"New Game",
				"Exit"};
				int value = JOptionPane.showOptionDialog(gameWindow, BoardUtil.oppositeColor(board.getCurrentPlayerColor()).toString()+" wins by checkmate!", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if(value == JOptionPane.YES_OPTION) {
					this.chessBoard = Board.createStandardBoard();
					show();
				}else {
					gameWindow.dispose();
				}
			}else if(board.getCurrentPlayer().isStaleMate()) {
				Object[] options = {"New Game",
				"Exit"};
				int value = JOptionPane.showOptionDialog(gameWindow, BoardUtil.oppositeColor(board.getCurrentPlayerColor()).toString()+" draws by stalemate!", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if(value == JOptionPane.YES_OPTION) {
					show();
				}else {
					gameWindow.dispose();
				}
			}else if(moveLog.isFiftyMoveRule()) {
				Object[] options = {"New Game",
				"Exit"};
				int value = JOptionPane.showOptionDialog(gameWindow, BoardUtil.oppositeColor(board.getCurrentPlayerColor()).toString()+" draws by fifty move rule!", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if(value == JOptionPane.YES_OPTION) {
					show();
				}else {
					gameWindow.dispose();
				}
			}else if(getBoard().insufficientMaterial()) {
				Object[] options = {"New Game",
				"Exit"};
				int value = JOptionPane.showOptionDialog(gameWindow,"Draw by insufficient material", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if(value == JOptionPane.YES_OPTION) {
					show();
				}else {
					gameWindow.dispose();
				}
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
			setBackground(new Color(22,21,18));
			setBorder(BorderFactory.createMatteBorder(0, 10, 0, 0, new Color(22,21,18)));
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
				gb.gridx = !boardFlipped ? BoardUtil.fToR[tile.getTileFile()] : BoardUtil.rToC[tile.getTileFile()];
				gb.gridy = !boardFlipped ? BoardUtil.rToC[tile.getTileRank()] : BoardUtil.fToR[tile.getTileRank()];
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
						if(!getBoard().getCurrentPlayer().isCheckMate() && moveLog.isLast()) {
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
														Board moveBoard = specialMove.execute();
														if(move.getMovePiece().getPieceColor() == Alliance.WHITE) {
															whiteCapturePanel.addCapturedPieces(moveBoard.getCapturedBlackPieces());
														}else {
															blackCapturePanel.addCapturedPieces(moveBoard.getCapturedWhitePieces());
														}
														whiteCapturePanel.setTextAdvantage(moveBoard.getCapturedBlackPieces(),moveBoard.getCapturedWhitePieces());
														blackCapturePanel.setTextAdvantage(moveBoard.getCapturedWhitePieces(),moveBoard.getCapturedBlackPieces());
														moveLog.addMove(specialMove);
														updateBoard(moveBoard);
													}
												});
												specialMove.setNewThread(newThread);
												specialMove.setGameWindow(gameWindow);
												Thread t = new Thread(specialMove);
												t.start();
												gameWindow.setEnabled(false);
											}else {
												Board moveBoard = move.execute();
												if(move.getMovePiece().getPieceColor() == Alliance.WHITE) {
													whiteCapturePanel.addCapturedPieces(moveBoard.getCapturedBlackPieces());
												}else {
													blackCapturePanel.addCapturedPieces(moveBoard.getCapturedWhitePieces());
												}
												whiteCapturePanel.setTextAdvantage(moveBoard.getCapturedBlackPieces(),moveBoard.getCapturedWhitePieces());
												blackCapturePanel.setTextAdvantage(moveBoard.getCapturedWhitePieces(),moveBoard.getCapturedBlackPieces());
												moveLog.addMove(move);
												updateBoard(moveBoard);
											}
											//Board.printBoard(getBoard());
											//System.out.println("");
										}
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
			if(movePiece != null && movePiece.getPieceColor() == getBoard().getCurrentPlayerColor() && moveLog.isLast()) {
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
			if(movePiece != null && movePiece.getPieceColor() == getBoard().getCurrentPlayerColor() && moveLog.isLast()) {
				for(Move move : movePiece.getLegalMoves(getBoard())) {
					if((move.getMoveFile() == this.getTileFile()) && (move.getMoveRank() == this.getTileRank()) && !getBoard().movesToCheck(move)) {
						g2.fillOval(width,height,r,r);
					}
				}
			}
			g2.setColor(this.getBackground() == this.darkTileColor ? lightTileColor : darkTileColor);
			g2.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
			if(!boardFlipped ? getTileFile() == 7 : getTileFile() == 0) {
				g2.drawString(Integer.toString(BoardUtil.rToNo[getTileRank()]), this.getWidth()-10, 15);
			}
			if(!boardFlipped ? getTileRank() == 0 : getTileRank() == 7) {
				g2.drawString(BoardUtil.fToA[getTileFile()], this.getWidth()-15, this.getHeight()-5);				
			}
		}

		private void pieceIcon() {

			Piece piece = getBoard().getPiece(getTileFile(), getTileRank());
			if(piece.getPieceType() != PieceType.EMPTY) {
				BufferedImage image = null;
				try {
					image = ImageIO.read(new File("images/"+piece.getPieceColor().getColorString()+piece.getPieceType().getPieceTypeString()+".png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ImageIcon pieceImage = new ImageIcon();
				pieceImage.setImage(image);
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

