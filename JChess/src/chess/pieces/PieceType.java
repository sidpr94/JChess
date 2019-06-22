package chess.pieces;

import chess.Alliance;

public enum PieceType {
	PAWN ("P",1),
	ROOK ("R",5),
	BISHOP ("B",3),
	KNIGHT ("N",3),
	QUEEN ("Q",9),
	KING ("K",0),
	EMPTY ("-",0);
	
	private String pieceTypeString;
	private int material;
	
	private PieceType(String type,int material) {
		this.pieceTypeString = type;
		this.material = material;
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
	
	public int getMaterialCount() {
		return this.material;
	}
}
