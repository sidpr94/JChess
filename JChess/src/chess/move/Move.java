package chess.move;

import chess.board.Board;
import chess.pieces.Piece;

public abstract class Move {
	
	private int moveRank;
	private int moveFile;
	private Piece movingPiece;
	private Board board;
	
	public Move(int moveFile,int moveRank,Piece movingPiece,Board board) {
		// TODO Auto-generated constructor stub
		this.moveFile = moveFile;
		this.moveRank = moveRank;
		this.movingPiece = movingPiece;
		this.board = board;
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
	
	public abstract Board execute();

}