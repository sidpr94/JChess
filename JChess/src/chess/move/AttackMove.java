package chess.move;

import java.util.List;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
import chess.pieces.Piece;

public class AttackMove extends Move {

	public AttackMove(int file, int rank, Piece piece, Board board) {
		super(file, rank, piece, board);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		
		Board currentBoard = this.getBoard();
		List<Piece> activePieces = currentBoard.getAllActivePieces();
		Builder builder = new Builder();
		for(Piece piece : activePieces) {
			if(piece.equals(this.getMovePiece())) {
				builder.setPiece(piece.movePiece(this));
			}else if (piece.equals(currentBoard.getPiece(getMoveFile(), getMoveRank()))) {
				builder.setCapturedPiece(piece);
			}else {
				builder.setPiece(piece);
			}
		}
		builder.setMover(BoardUtil.oppositeColor(currentBoard.getCurrentPlayerColor()));
		builder.enPassantPawn(null);
		
		return builder.execute();
		// TODO Auto-generated method stub
		
	}

}
