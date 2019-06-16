package chess.move;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
			chosenPiece = piece;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			synchronized(moveBoard) {
				frame.dispose();
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
					this.notifyAll();
				}
			}
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

				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.requestFocus();

			}

		});

		synchronized(moveBoard){
			while(moveBoard == null);
		}
	}
}
