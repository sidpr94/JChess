package chess.move;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
import chess.pieces.Bishop;
import chess.pieces.Knight;
import chess.pieces.NoPiece;
import chess.pieces.Piece;
import chess.pieces.PieceType;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class PawnPromotion extends Move implements Runnable{

	public Board moveBoard;
	JFrame frame;
	boolean isAttack;
	Object lock = new Object();
	boolean pieceChosen = false;
	Thread newThread;
	JFrame gameWindow;

	public PawnPromotion(int file, int rank, Piece piece, Board board,boolean isAttack) {
		super(file, rank, piece, board);
		frame = new JFrame("Promote Pawn To:");
		this.isAttack = isAttack;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		return moveBoard;
		// TODO Auto-generated method stub

	}

	private class buttonListener implements ActionListener {

		Piece chosenPiece;

		public buttonListener(Piece piece) {
			this.chosenPiece = piece;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					frame.dispose();
				}
				
			});
			if(isAttack) {
				Board currentBoard = getBoard();
				List<Piece> activePieces = currentBoard.getAllActivePieces();
				Builder builder = new Builder();
				for(Piece piece : activePieces) {
					if(piece.equals(getMovePiece())) {
						builder.setPiece(chosenPiece);
					}else if(piece.getFile() == getMoveFile() && piece.getRank() == getMoveRank()) {
						builder.setPiece(new NoPiece(getMovePiece().getFile(), getMovePiece().getRank()));	
						builder.setCapturedPiece(piece);
					}else {
						builder.setPiece(piece);
					}
				}
				builder.setMover(BoardUtil.oppositeColor(currentBoard.getCurrentPlayerColor()));
				builder.enPassantPawn(null);

				moveBoard = builder.execute();
			}else {
				Board currentBoard = getBoard();
				List<Piece> activePieces = currentBoard.getAllActivePieces();
				Builder builder = new Builder();
				for(Piece piece : activePieces) {
					if(piece.equals(getMovePiece())) {
						builder.setPiece(chosenPiece);
					}else if(piece.getFile() == getMoveFile() && piece.getRank() == getMoveRank()){
						builder.setPiece(new NoPiece(getMovePiece().getFile(), getMovePiece().getRank()));
					}else {
						builder.setPiece(piece);
					}
				}
				builder.setMover(BoardUtil.oppositeColor(currentBoard.getCurrentPlayerColor()));
				builder.enPassantPawn(null);
				
				moveBoard = builder.execute();

			}
			
			PawnPromotion.this.newThread.start();
			
			synchronized(lock) {
				lock.notifyAll();
			}
			
			pieceChosen = true;

		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JPanel buttons = new JPanel(new FlowLayout());

				ImageIcon queenImage = new ImageIcon("images/"+getMovePiece().getPieceColor().getColorString()+PieceType.QUEEN.getPieceTypeString()+".png");
				JButton queen = new JButton("Queen");
				queen.addActionListener(new buttonListener(new Queen(getMovePiece().getPieceColor(), getMoveFile(),getMoveRank())));
				queen.setIcon(queenImage);
				buttons.add(queen);

				ImageIcon rookImage = new ImageIcon("images/"+getMovePiece().getPieceColor().getColorString()+PieceType.ROOK.getPieceTypeString()+".png");
				JButton rook = new JButton("Rook");
				rook.addActionListener(new buttonListener(new Rook(getMovePiece().getPieceColor(), getMoveFile(), getMoveRank(), true)));
				rook.setIcon(rookImage);
				buttons.add(rook);

				ImageIcon knightImage = new ImageIcon("images/"+getMovePiece().getPieceColor().getColorString()+PieceType.KNIGHT.getPieceTypeString()+".png");
				JButton knight = new JButton("Knight");
				knight.addActionListener(new buttonListener(new Knight(getMovePiece().getPieceColor(), getMoveFile(), getMoveRank())));
				knight.setIcon(knightImage);
				buttons.add(knight);

				ImageIcon bishopImage = new ImageIcon("images/"+getMovePiece().getPieceColor().getColorString()+PieceType.BISHOP.getPieceTypeString()+".png");
				JButton bishop = new JButton("Bishop");
				bishop.addActionListener(new buttonListener(new Bishop(getMovePiece().getPieceColor(),getMoveFile(),getMoveRank())));
				bishop.setIcon(bishopImage);
				buttons.add(bishop);

				frame.add(buttons);
				frame.addWindowListener(new WindowListener() {

					@Override
					public void windowActivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowClosed(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowClosing(WindowEvent arg0) {
						// TODO Auto-generated method stub
						PawnPromotion.this.gameWindow.setEnabled(true);
						PawnPromotion.this.gameWindow.requestFocus();
						pieceChosen = true;
						synchronized(lock) {
							lock.notifyAll();
						}
					}

					@Override
					public void windowDeactivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowDeiconified(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowIconified(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowOpened(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});

				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.requestFocus();
			}
		});
		
		while(!pieceChosen) {
			synchronized(lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setNewThread(Thread thread) {
		this.newThread = thread;
	}

	public void setGameWindow(JFrame gameWindow) {
		// TODO Auto-generated method stub
		this.gameWindow = gameWindow;
		
	}


}
