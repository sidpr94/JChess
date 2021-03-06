package chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import chess.Alliance;
import chess.board.Board;
import chess.board.BoardUtil;
import chess.engine.NegaMax;
import chess.move.Move;
import chess.move.MoveState;
import chess.move.PawnPromotion;
import chess.pieces.Piece;
import chess.pieces.PieceType;

public class ChessPanels extends Observable{

	private final JFrame gameWindow;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	private Piece movePiece;
	private MoveState moveState;
	private MoveLog moveLog;
	private CapturedPiecePanel blackCapturePanel;
	private CapturedPiecePanel whiteCapturePanel;
	private boolean boardFlipped = false;
	private GameSetup gameSetup;

	public ChessPanels() {
		this.gameWindow = new JFrame("Sid's Chess App");
		this.gameSetup = new GameSetup(gameWindow);
		this.moveState = MoveState.CHOOSE;
		this.movePiece = null;
		this.addObserver(new EngineWatcher());

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
		//this.chessBoard = Board.createTestBoard();
		gameSetup.setVisible(true);
		boardFlipped = gameSetup.isBoardFlipped();
		moveLog.clearAllMoves();
		whiteCapturePanel.removeAllCapturedPieces();
		blackCapturePanel.removeAllCapturedPieces();
		updateBoard(Board.createStandardBoard());
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
		this.setChanged();
		this.notifyObservers(board);
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
			setBorder(BorderFactory.createMatteBorder(0, 10, 0, 0, new Color(22,21,18)));
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
		}


	}

	class TilePanel extends JPanel{

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
			PieceLabel pieceLabel = new PieceLabel();
			pieceLabel.setIcon(getBoard().getPiece(tileFile, tileRank));
			if(getBoard().getPiece(tileFile,tileRank).getPieceType() != PieceType.EMPTY){
				add(pieceLabel);
			}
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(SwingUtilities.isLeftMouseButton(e)) {
						if(!getBoard().getCurrentPlayer().isCheckMate() && moveLog.isLast() && !gameSetup.isPlayerAI(getBoard().getCurrentPlayer())) {
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
												Thread t = new Thread(new PromotionSelection(gameWindow, specialMove, newThread));
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
											movePiece = null;
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
			if(!gameSetup.isPlayerAI(getBoard().getCurrentPlayer())) {
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

		private int getTileFile() {
			return tileFile;
		}

		private int getTileRank() {
			return tileRank;
		}
	}

	private class EngineWatcher implements Observer{

		@Override
		public void update(Observable obs, Object obj) {
			// TODO Auto-generated method stub
			if(moveLog.isLast()) {
				if(getBoard().getCurrentPlayer().isCheckMate()) {
					Object[] options = {"New Game",
					"Exit","Cancel"};
					int value = JOptionPane.showOptionDialog(gameWindow, BoardUtil.oppositeColor(getBoard().getCurrentPlayerColor()).toString()+" wins by checkmate!", "Game Over!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if(value == JOptionPane.YES_OPTION) {
						updateBoard(Board.createStandardBoard());
						show();
					}else if (value == JOptionPane.NO_OPTION) {
						gameWindow.dispose();
					}
				}else if(getBoard().getCurrentPlayer().isStaleMate()) {
					Object[] options = {"New Game",
					"Exit","Cancel"};
					int value = JOptionPane.showOptionDialog(gameWindow, BoardUtil.oppositeColor(getBoard().getCurrentPlayerColor()).toString()+" draws by stalemate!", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if(value == JOptionPane.YES_OPTION) {
						updateBoard(Board.createStandardBoard());
						show();
					}else if (value == JOptionPane.NO_OPTION) {
						gameWindow.dispose();
					}
				}else if(moveLog.isFiftyMoveRule()) {
					Object[] options = {"New Game",
					"Exit","Cancel"};
					int value = JOptionPane.showOptionDialog(gameWindow, BoardUtil.oppositeColor(getBoard().getCurrentPlayerColor()).toString()+" draws by fifty move rule!", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if(value == JOptionPane.YES_OPTION) {
						updateBoard(Board.createStandardBoard());
						show();
					}else if (value == JOptionPane.NO_OPTION) {
						gameWindow.dispose();
					}
				}else if(getBoard().insufficientMaterial()) {
					Object[] options = {"New Game",
					"Exit","Cancel"};
					int value = JOptionPane.showOptionDialog(gameWindow,"Draw by insufficient material", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if(value == JOptionPane.YES_OPTION) {
						updateBoard(Board.createStandardBoard());
						show();
					}else if (value == JOptionPane.NO_OPTION) {
						gameWindow.dispose();
					}
				}else {
					if(gameSetup.isPlayerAI(getBoard().getCurrentPlayer())) {
						ChessEngine engine = new ChessEngine(getBoard());
						engine.execute();
					}
				}
			}
		}

	}
	
	private class ChessEngine extends SwingWorker<Move,Object> {
		
		private Board board;
		
		public ChessEngine(Board board) {
			this.board = board;
		};

		@Override
		public Move doInBackground() throws Exception {
			// TODO Auto-generated method stub

			NegaMax negaMax = new NegaMax(4);
			return negaMax.execute(board);
			
		}
		
		@Override
		public void done() {
			try {
				Move move = get();
				Board moveBoard = move.execute();
				whiteCapturePanel.setTextAdvantage(moveBoard.getCapturedBlackPieces(),moveBoard.getCapturedWhitePieces());
				blackCapturePanel.setTextAdvantage(moveBoard.getCapturedWhitePieces(),moveBoard.getCapturedBlackPieces());
				moveLog.addMove(move);
				ChessPanels.this.updateBoard(moveBoard);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}

}
