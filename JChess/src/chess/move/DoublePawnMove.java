package chess.move;

import java.util.List;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
import chess.pieces.Pawn;
import chess.pieces.Piece;

public class DoublePawnMove extends Move {

	public DoublePawnMove(int file, int rank, Pawn pawn, Board board) {
		super(file, rank, pawn, board);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		Board currentBoard = this.getBoard();
		List<Piece> activePieces = currentBoard.getActiveBlackPieces();
		Builder builder = new Builder();
		for(Piece piece : activePieces) {
			if(piece.equals(this.getMovePiece())) {
				builder.setPiece(piece.movePiece(this));
			}else {
				builder.setPiece(piece);
			}
		}
		builder.setMover(BoardUtil.oppositeColor(currentBoard.getCurrentPlayerColor()));
		
		builder.enPassantPawn((Pawn) this.getMovePiece());
		
		return builder.execute();
		// TODO Auto-generated method stub

	}

}