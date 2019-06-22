package chess.move;

import java.util.List;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
import chess.pieces.NoPiece;
import chess.pieces.Pawn;
import chess.pieces.Piece;

public class EnPassantMove extends Move {

	public EnPassantMove(int file, int rank, Piece piece, Board board) {
		super(file, rank, piece, board,MoveType.Normal);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		Board currentBoard = this.getBoard();
		List<Piece> activePieces = currentBoard.getAllActivePieces();
		Builder builder = new Builder();
		Pawn enPassantPawn = currentBoard.getEnPassantPawn();
		List<Piece> capturedPieces = this.getBoard().getAllCapturedPieces();
		builder.setCapturedPiece(enPassantPawn);
		capturedPieces.forEach(piece -> builder.setCapturedPiece(piece));
		for(Piece piece : activePieces) {
			if(piece.equals(this.getMovePiece())) {
				builder.setPiece(piece.movePiece(this));
			}else if(piece.getFile() == this.getMoveFile() && piece.getRank() == this.getMoveRank()) {
				builder.setPiece(new NoPiece(this.getMovePiece().getFile(), this.getMovePiece().getRank()));				
			}else if (piece.equals(enPassantPawn)) {
				builder.setPiece(new NoPiece(enPassantPawn.getFile(), enPassantPawn.getRank()));
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
