package chess.pieces;

import java.util.List;

import chess.Color;
import chess.board.Board;
import chess.move.Move;

public class NoPiece extends Piece {

	public NoPiece(int file,int rank) {
		super(Color.EMPTY,file,rank,PieceType.EMPTY,false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Move> getLegalMoves(Board board) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Piece movePiece(Move move) {
		// TODO Auto-generated method stub
		return null;
	}

}
