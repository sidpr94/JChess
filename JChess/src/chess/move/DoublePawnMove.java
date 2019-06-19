package chess.move;

import java.util.List;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
import chess.pieces.NoPiece;
import chess.pieces.Pawn;
import chess.pieces.Piece;

public class DoublePawnMove extends Move {

	public DoublePawnMove(int file, int rank, Pawn pawn, Board board) {
		super(file, rank, pawn, board,MoveType.Normal);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		Board currentBoard = this.getBoard();
		List<Piece> activePieces = currentBoard.getAllActivePieces();
		Builder builder = new Builder();
		for(Piece piece : activePieces) {
			if(piece.equals(this.getMovePiece())) {
				Pawn movedPawn = (Pawn) piece.movePiece(this);
				builder.setPiece(movedPawn);
				builder.enPassantPawn(movedPawn);
			}else if(piece.getFile() == this.getMoveFile() && piece.getRank() == this.getMoveRank()){
				builder.setPiece(new NoPiece(this.getMovePiece().getFile(), this.getMovePiece().getRank()));
			}	
			else {
				builder.setPiece(piece);
			}
		}
		builder.setMover(BoardUtil.oppositeColor(currentBoard.getCurrentPlayerColor()));
		
		return builder.execute();
		// TODO Auto-generated method stub

	}

}
