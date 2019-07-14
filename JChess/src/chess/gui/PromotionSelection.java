package chess.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chess.move.PawnPromotion;
import chess.pieces.Bishop;
import chess.pieces.Knight;
import chess.pieces.Piece;
import chess.pieces.PieceType;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class PromotionSelection implements Runnable {
	private JFrame frame;
	private JFrame gameWindow;
	private Thread newThread;
	private Object lock = new Object();
	private boolean pieceChosen = false;
	private PawnPromotion move;
	
	public PromotionSelection(JFrame gameWindow,PawnPromotion move, Thread newThread) {
		this.frame = new JFrame("Promote Pawn To:");
		this.gameWindow = gameWindow;
		this.newThread = newThread;
		this.move = move;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JPanel buttons = new JPanel(new FlowLayout());

				ImageIcon queenImage = new ImageIcon("images/"+move.getMovePiece().getPieceColor().getColorString()+PieceType.QUEEN.getPieceTypeString()+".png");
				JButton queen = new JButton("Queen");
				queen.addActionListener(new buttonListener(new Queen(move.getMovePiece().getPieceColor(), move.getMoveFile(),move.getMoveRank())));
				queen.setIcon(queenImage);
				buttons.add(queen);

				ImageIcon rookImage = new ImageIcon("images/"+move.getMovePiece().getPieceColor().getColorString()+PieceType.ROOK.getPieceTypeString()+".png");
				JButton rook = new JButton("Rook");
				rook.addActionListener(new buttonListener(new Rook(move.getMovePiece().getPieceColor(), move.getMoveFile(), move.getMoveRank(), true)));
				rook.setIcon(rookImage);
				buttons.add(rook);

				ImageIcon knightImage = new ImageIcon("images/"+move.getMovePiece().getPieceColor().getColorString()+PieceType.KNIGHT.getPieceTypeString()+".png");
				JButton knight = new JButton("Knight");
				knight.addActionListener(new buttonListener(new Knight(move.getMovePiece().getPieceColor(), move.getMoveFile(), move.getMoveRank())));
				knight.setIcon(knightImage);
				buttons.add(knight);

				ImageIcon bishopImage = new ImageIcon("images/"+move.getMovePiece().getPieceColor().getColorString()+PieceType.BISHOP.getPieceTypeString()+".png");
				JButton bishop = new JButton("Bishop");
				bishop.addActionListener(new buttonListener(new Bishop(move.getMovePiece().getPieceColor(),move.getMoveFile(),move.getMoveRank())));
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
						PromotionSelection.this.gameWindow.setEnabled(true);
						PromotionSelection.this.gameWindow.requestFocus();
						PromotionSelection.this.pieceChosen = true;
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
			
			move.setChosenPiece(chosenPiece);
			
			synchronized(lock) {
				lock.notifyAll();
			}
			
			pieceChosen = true;
			PromotionSelection.this.newThread.start();

		}

	}

}
