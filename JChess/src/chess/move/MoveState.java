package chess.move;

import chess.board.Board;
import chess.pieces.Piece;
import chess.pieces.PieceType;

public enum MoveState {
	CHOOSE {
		@Override
		public MoveState nextState(int tileFile, int tileRank, Piece movePiece, Board board) {
			// TODO Auto-generated method stub
			if(movePiece == null || movePiece.getPieceType() == PieceType.EMPTY) {
				return CHOOSE;
			}else {
				return MOVE;
			}
		}
	},
	MOVE {

		@Override
		public MoveState nextState(int tileFile, int tileRank, Piece movePiece, Board board) {
			// TODO Auto-generated method stub
			if(movePiece == null) {
				return CHOOSE;
			}else {
				for(Move move : movePiece.getLegalMoves(board)) {
					if(tileFile == move.getMoveFile() && tileRank == move.getMoveRank()) {
						System.out.println("SUP");
						return DONE;
					}
				}
				return MOVE;
			}
		}
	},
	DONE {

		@Override
		public MoveState nextState(int tileFile, int tileRank, Piece movePiece, Board board) {
			// TODO Auto-generated method stub
			return CHOOSE;
		}
	};
	
	public abstract MoveState nextState(int tileFile, int tileRank, Piece movePiece,Board board);
}
