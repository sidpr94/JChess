package chess.move;

import java.util.List;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
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
		for(Piece piece : activePieces) {
			if(piece.equals(this.getMovePiece())) {
				builder.setPiece(piece.movePiece(new NormalMove(this.getMoveFile()-2,this.getMoveRank(),this.getMovePiece(),currentBoard)));
			}else if(piece.equals(currentBoard.getPiece(7, 0))){
				builder.setPiece(piece.movePiece(new NormalMove(piece.getFile()+3,piece.getRank(),piece,currentBoard)));
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
