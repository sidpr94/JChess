package chess.move;

import chess.pieces.Piece;

public class MoveStateMachine {
	
	private Piece movePiece;
	private MoveState moveState;
	
	public MoveStateMachine(Piece movePiece) {
		this.movePiece = movePiece;
		this.moveState = MoveState.CHOOSE;
	}
	
	public MoveStateMachine(Piece movePiece,MoveState moveState) {
		this.movePiece = movePiece;
		this.moveState = moveState;
	}
	
	public enum MoveState{
	
		CHOOSE{
			public MoveState nextState() {
				return MOVE;
			}
		},
		MOVE {
			@Override
			public MoveState nextState() {
				// TODO Auto-generated method stub
				return DONE;
			}
		},
		DONE {
			@Override
			public MoveState nextState() {
				// TODO Auto-generated method stub
				return CHOOSE;
			}
		};
		
		public abstract MoveState nextState();
	}
	
	private Piece getMovePiece() {
		return movePiece;
	}
	
	public MoveState getMoveState() {
		return moveState;
	}
}
