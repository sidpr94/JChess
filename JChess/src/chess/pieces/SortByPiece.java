package chess.pieces;

import java.util.Comparator;

public class SortByPiece implements Comparator<Piece>{
	
	public SortByPiece() {}

	@Override
	public int compare(Piece a, Piece b) {
		// TODO Auto-generated method stub
		int aValue = a.getPieceType().getMaterialCount();
		int bValue = b.getPieceType().getMaterialCount();
		if(a.getPieceType().equals(PieceType.BISHOP)) {
			aValue = 4;
		}
		if(b.getPieceType().equals(PieceType.KNIGHT)) {
			bValue = 4;
		}
		return aValue - bValue;
	}
	
}