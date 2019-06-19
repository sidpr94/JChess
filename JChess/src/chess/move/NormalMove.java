package chess.move;

import java.util.List;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
import chess.pieces.NoPiece;
import chess.pieces.Piece;

public class NormalMove extends Move {
	
	public NormalMove(int moveFile, int moveRank, Piece movingPiece,Board board) {
		super(moveFile, moveRank, movingPiece,board,MoveType.Normal);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		// TODO Auto-generated method stub
		Board currentBoard = this.getBoard();
		List<Piece> activePieces = currentBoard.getAllActivePieces();
		Builder builder = new Builder();
		for(Piece piece : activePieces) {
			if(piece.equals(this.getMovePiece())) {
				builder.setPiece(piece.movePiece(this));
			}else if(piece.getFile() == this.getMoveFile() && piece.getRank() == this.getMoveRank()){
				builder.setPiece(new NoPiece(this.getMovePiece().getFile(), this.getMovePiece().getRank()));
			}else {
				builder.setPiece(piece);
			}
		}
		builder.setMover(BoardUtil.oppositeColor(currentBoard.getCurrentPlayerColor()));
		builder.enPassantPawn(null);
		
		return builder.execute();
	}

}
