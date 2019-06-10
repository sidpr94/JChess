package chess.move;

public enum MoveState {
	CHOOSE {
		@Override
		public MoveState previousState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MoveState nextState() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	MOVE,
	DONE;
	
	public abstract MoveState previousState();
	public abstract MoveState nextState();
	
}
