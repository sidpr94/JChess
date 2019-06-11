package chess.move;

import java.util.List;

import chess.Color;
import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
import chess.pieces.NoPiece;
import chess.pieces.Piece;

public class LongSideCastleMove extends Move{

	public LongSideCastleMove(Piece piece, Board board) {
		super(piece.getFile(),piece.getRank(), piece, board);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		Board currentBoard = this.getBoard();
		List<Piece> activePieces = currentBoard.getAllActivePieces();
		Builder builder = new Builder();
		Piece rook = null;
		if(this.getMovePiece().getPieceColor() == Color.WHITE) {
			rook = currentBoard.getPiece(0, 0);
		}else {
			rook = currentBoard.getPiece(0, 7);
		}
		
		for(Piece piece : activePieces) {
			if(piece.equals(this.getMovePiece())) {
				builder.setPiece(piece.movePiece(new NormalMove(this.getMoveFile()-2,this.getMoveRank(),this.getMovePiece(),currentBoard)));
			}else if(piece.getFile() == this.getMoveFile() && piece.getRank() == this.getMoveRank()){
				builder.setPiece(new NoPiece(this.getMovePiece().getFile(), this.getMovePiece().getRank()));
			}
			else if(piece.equals(rook)){
				builder.setPiece(piece.movePiece(new NormalMove(piece.getFile()+3,piece.getRank(),piece,currentBoard)));
				builder.setPiece(new NoPiece(rook.getFile(),rook.getRank()));
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
