package chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;

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
	private TilePanel sourceTile;
	private JLabel pieceLabel;
	private JLayeredPane pane;

	public ChessPanels() {
		this.gameWindow = new JFrame("Sid's Chess App");
		this.moveState = MoveState.CHOOSE;
		this.movePiece = null;
		this.sourceTile = null;

		JPanel homeOfAllPanels = new JPanel(new BorderLayout());
		pane = new JLayeredPane();
		pane.setPreferredSize(new Dimension(630,631));
		pane.setBorder(BorderFactory.createEmptyBorder());
		this.boardPanel = new BoardPanel();
		pane.add(boardPanel,JLayeredPane.DEFAULT_LAYER);
		boardPanel.setBounds(0,0,640,640);
		this.blackCapturePanel = new CapturedPiecePanel();
		this.whiteCapturePanel = new CapturedPiecePanel();
		this.moveLog = new MoveLog(this);

		JPanel wardenOfTheEast = new JPanel(new BorderLayout());
		wardenOfTheEast.add(blackCapturePanel.getCapturedPiecePanel(), !boardFlipped ? BorderLayout.NORTH : BorderLayout.SOUTH);
		wardenOfTheEast.add(pane, BorderLayout.CENTER);
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
		gameWindow.setResizable(false);
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
			super(new GridLayout(8,8));
			setBackground(new Color(22,21,18));
			setBorder(BorderFactory.createEmptyBorder());
		}

		List<TilePanel> generateTiles() {
			List<TilePanel> tiles = new ArrayList<TilePanel>();
			for(int i = BoardUtil.RANKS-1; i > -1; i--) {
				for(int j = 0; j < BoardUtil.FILES; j++) {
					TilePanel tile = new TilePanel(!boardFlipped ? j : BoardUtil.rToC[j],!boardFlipped ? i : BoardUtil.rToC[i]);
					tiles.add(tile);
				}
			}
			return tiles;
		}

		void drawBoard() {
			removeAll();
			this.boardTiles = generateTiles();
			for(TilePanel tile : boardTiles) {
				this.add(tile);
			}
			revalidate();
			repaint();
			System.out.println(boardPanel.getWidth()+" "+boardPanel.getHeight());
			System.out.println(pane.getWidth()+" "+pane.getHeight());
			System.out.println(pane.getBounds().getWidth()+" "+pane.getBounds().getHeight());
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
			/*
			addMouseListener(new MouseAdapter() {
				@Override
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
			});*/
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


		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0.407f, 0.545f, 0.538f, 0.76f));
			int r = 30;
			int width = (this.getWidth() - r)/2;
			int height = (this.getHeight() - r)/2;
			this.setBorder(BorderFactory.createLineBorder(darkTileColor, 0));
			if(movePiece != null && movePiece.getPieceColor() == getBoard().getCurrentPlayerColor() && moveLog.isLast()) {
				if((movePiece.getFile() == this.getTileFile()) && (movePiece.getRank() == this.getTileRank())) {
					LineBorder tileBorder = (LineBorder) this.getBorder();
					this.setBorder((tileBorder.getLineColor() == borderColor ? BorderFactory.createEmptyBorder() : BorderFactory.createLineBorder(borderColor, 4)));
				}
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
			JLabel label = new JLabel();
			label.setPreferredSize(new Dimension(80,80));
			MoveMouseAdapter moveMouse = new MoveMouseAdapter();
			label.addMouseMotionListener(moveMouse);
			label.addMouseListener(moveMouse);
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
				label.setIcon(pieceImage);
				label.setVerticalAlignment(SwingConstants.CENTER);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setPreferredSize(new Dimension(80,80));
				label.setVerticalAlignment(SwingConstants.CENTER);
				//label.setOpaque(false);
				this.add(label, BorderLayout.CENTER);
			}else {
				this.add(label, BorderLayout.CENTER);
			}
		}

		private int getTileFile() {
			return tileFile;
		}

		private int getTileRank() {
			return tileRank;
		}

		private class MoveMouseAdapter extends MouseInputAdapter{
			@Override
			public void mousePressed(MouseEvent e) {
				if(!getBoard().getCurrentPlayer().isCheckMate() && moveLog.isLast()) {
					pieceLabel = (JLabel) e.getSource();
					sourceTile = (TilePanel) pieceLabel.getParent();
					Piece sourceTilePiece = getBoard().getPiece(sourceTile.getTileFile(), sourceTile.getTileRank());
					if(sourceTilePiece.getPieceColor() == getBoard().getCurrentPlayerColor()) {
						System.out.println("HELLO?!");
						sourceTile.remove(pieceLabel);
						movePiece = getBoard().getPiece(sourceTile.getTileFile(), sourceTile.getTileRank());		
						pane.add(pieceLabel,JLayeredPane.DRAG_LAYER);
						int x = sourceTile.getX()+e.getX()-pieceLabel.getWidth()/2;
						int y = sourceTile.getY()+e.getY()-pieceLabel.getHeight()/2;
						pieceLabel.setLocation(x,y);

						boardPanel.repaint();
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if(movePiece != null) {
					int pieceX = pieceLabel.getX()+e.getX()-pieceLabel.getWidth()/2;
					int pieceY = pieceLabel.getY()+e.getY()-pieceLabel.getHeight()/2;
					if(pieceX < boardPanel.getX()) {
						pieceX = -pieceLabel.getWidth()/2;
					}else if(pieceX > boardPanel.getX()+boardPanel.getWidth()) {
						pieceX = boardPanel.getWidth()-pieceLabel.getWidth()/2;
					}
					if(pieceY < boardPanel.getY()) {
						pieceY = -pieceLabel.getHeight()/2;
					}else if(pieceY > boardPanel.getY()+boardPanel.getHeight()) {
						pieceY = boardPanel.getHeight()-pieceLabel.getHeight()/2;
					}
					pieceLabel.setLocation(pieceX,pieceY);
					boardPanel.repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Point p = new Point(pieceLabel.getX()+e.getX(),pieceLabel.getY()+e.getY());
				System.out.println(p.getX()+" "+p.getY());
				System.out.println(boardPanel.getComponentAt(p));
				TilePanel hoverPanel = (TilePanel) boardPanel.getComponentAt(p);
				if(hoverPanel != sourceTile) {
					if(movePiece != null) {
						System.out.println("here");
						for(Move move : movePiece.getLegalMoves(getBoard())) {
							System.out.println("here");
							if(hoverPanel != null) {
								if(hoverPanel.getTileFile() == move.getMoveFile() && hoverPanel.getTileRank() == move.getMoveRank() && !getBoard().movesToCheck(move)) {
									System.out.println("here");
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
										pane.remove(pieceLabel);
										updateBoard(moveBoard);
									}
								}
							}else {
								pane.remove(pieceLabel);
								sourceTile.add(pieceLabel);
								pieceLabel.setLocation(sourceTile.getX(),sourceTile.getY());
								pane.repaint();
							}
						}
					}
				}else {
					System.out.println("slick back");
					pane.remove(pieceLabel);
					sourceTile.add(pieceLabel);
					pieceLabel.setLocation(sourceTile.getX(),sourceTile.getY());
					pane.repaint();
				}
				movePiece = null;
				/*
					if(!getBoard().getCurrentPlayer().isCheckMate() && moveLog.isLast()) {
						if(moveState == MoveState.CHOOSE) {
							movePiece = getBoard().getPiece(getTileFile(),getTileRank());
							System.out.println("Choose: "+movePiece);
							//							boardPanel.drawBoard();
							boardPanel.repaint();
						}else if(moveState == MoveState.MOVE) {
							System.out.println("Move: "+movePiece);
							if(!getBoard().getPiece(tileFile, tileRank).equals(movePiece) && getBoard().getPiece(tileFile, tileRank).getPieceType() != PieceType.EMPTY && getBoard().getPiece(tileFile, tileRank).getPieceColor() == getBoard().getCurrentPlayerColor()) {
								movePiece = getBoard().getPiece(getTileFile(), getTileRank());
							}else if(tileFile == movePiece.getFile() && tileRank == movePiece.getRank()) {
								movePiece = null;
							}
							//							boardPanel.drawBoard();
							boardPanel.repaint();
						}
						System.out.println();
						moveState = moveState.nextState(getTileFile(),getTileRank(),movePiece,getBoard());
						if(moveState == MoveState.DONE) {
							System.out.println("Done: "+movePiece);
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
						if(moveState == MoveState.MOVE && getBoard().getPiece(tileFile, tileRank).getPieceType() == PieceType.EMPTY) {
							movePiece = null;
						}
						moveState = moveState.nextState(tileFile, tileRank, movePiece, getBoard());			
					}
				 */
			}
		}

	}
}

