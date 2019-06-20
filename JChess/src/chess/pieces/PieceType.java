package chess.pieces;

import chess.Alliance;

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
	
	public String getPiecePrint(Alliance color) {
		return color == Alliance.WHITE ? this.pieceTypeString : this.pieceTypeString.toLowerCase();
	}
	
	public String getPieceTypeString() {
		return pieceTypeString;
	}
	
	public String getPieceNotation() {
		return this.pieceTypeString.replace("P", "");
	}
}
