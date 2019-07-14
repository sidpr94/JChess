package chess.move;

import chess.board.Board;

public class NullMove extends Move {

	public NullMove() {
		super(0, 0, null, null, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
