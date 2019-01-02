package chess.pieces;

import chess.Color;

public enum PieceType {
	PAWN ("P"),
	ROOK ("R"),
	BISHOP ("B"),
	KNIGHT ("N"),
	QUEEN ("Q"),
	KING ("K"),
	EMPTY ("-");
	
	private String pieceTypeString;
	
	private PieceType(String type) {
		this.pieceTypeString = type;
	}
	
	public String getPieceNotation(Color color) {
		return color == Color.WHITE ? this.pieceTypeString : this.pieceTypeString.toLowerCase();
	}
	
	public String getPieceTypeString() {
		return pieceTypeString;
	}
}
