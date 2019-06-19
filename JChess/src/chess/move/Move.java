package chess.move;

import chess.board.Board;
import chess.pieces.Piece;

public abstract class Move {
	
	private int moveRank;
	private int moveFile;
	private Piece movingPiece;
	private Board board;
	private MoveType moveType;
	
	public Move(int moveFile,int moveRank,Piece movingPiece,Board board,MoveType moveType) {
		// TODO Auto-generated constructor stub
		this.moveFile = moveFile;
		this.moveRank = moveRank;
		this.movingPiece = movingPiece;
		this.board = board;
		this.moveType = moveType;
	}
	
	public int getMoveFile() {
		return moveFile;
	}
	public int getMoveRank() {
		return moveRank;
	}
	
	public int getCurrentFile() {
		return movingPiece.getFile();
	}
	
	public int getCurrentRank() {
		return movingPiece.getRank();
	}
	
	public Piece getMovePiece() {
		return movingPiece;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public MoveType getMoveType() {
		return moveType;
	}
	
	public abstract Board execute();

}