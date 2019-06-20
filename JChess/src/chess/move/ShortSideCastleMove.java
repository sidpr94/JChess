package chess.move;

import java.util.List;

import chess.Alliance;
import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
import chess.pieces.NoPiece;
import chess.pieces.Piece;

public class ShortSideCastleMove extends Move {

	public ShortSideCastleMove(Piece piece, Board board) {
		super(piece.getFile()+2, piece.getRank(), piece, board,MoveType.Castle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		Board currentBoard = this.getBoard();
		List<Piece> activePieces = currentBoard.getAllActivePieces();
		List<Piece> capturedPieces = currentBoard.getAllCapturedPieces();
		Builder builder = new Builder();
		Piece rook = null;
		if(this.getMovePiece().getPieceColor() == Alliance.WHITE) {
			rook = currentBoard.getPiece(7, 0);
		}else {
			rook = currentBoard.getPiece(7, 7);
		}
		for(Piece piece : activePieces) {
			if(piece.equals(this.getMovePiece())) {
				builder.setPiece(piece.movePiece(this));
			}else if(piece.getFile() == this.getMoveFile() && piece.getRank() == this.getMoveRank()){
				builder.setPiece(new NoPiece(this.getMovePiece().getFile(), this.getMovePiece().getRank()));
			}
			else if(piece.equals(rook)){
				builder.setPiece(rook.movePiece(new NormalMove(rook.getFile()-2,rook.getRank(),rook,currentBoard)));
			}else if(piece.getFile() == rook.getFile()-2 && piece.getRank() == rook.getRank()) {
				builder.setPiece(new NoPiece(rook.getFile(),rook.getRank()));
			}else {
				builder.setPiece(piece);
			}
		}
		builder.setMover(BoardUtil.oppositeColor(currentBoard.getCurrentPlayerColor()));
		builder.enPassantPawn(null);
		builder.setCapturedPiece(capturedPieces);
		
		return builder.execute();
		// TODO Auto-generated method stub
		
	}

}
