package chess.pieces;

import java.util.List;

import chess.Color;
import chess.board.Board;
import chess.move.Move;

public abstract class Piece {
	
	private final Color pieceColor;
	private final int rank;
	private final int file;
	private final PieceType pieceType;
	private final boolean hasMoved;
	
	public Piece(Color pieceColor,int file,int rank,PieceType pieceType,boolean hasMoved){
		this.pieceColor = pieceColor;
		this.rank = rank;
		this.file = file;
		this.pieceType = pieceType;
		this.hasMoved = hasMoved;
	}
	
	public abstract List<Move> getLegalMoves(Board board);
	
	public int getRank() {
		return rank;
	}
	
	public int getFile() {
		return file;
	}
	
	public Color getPieceColor() {
		return pieceColor;
	}
	
	public PieceType getPieceType() {
		return pieceType;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}

	public abstract Piece movePiece(Move move);
}
