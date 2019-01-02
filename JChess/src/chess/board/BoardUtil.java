package chess.board;

import chess.Color;

public class BoardUtil {
	
	public static final int NUM_TILES = 64;
	
	public static final int RANKS = 8;
	
	public static final int FILES = 8;
	
	public static final int[] rToC = {7, 6, 5, 4, 3, 2, 1, 0};
	
	public static final int[] fToR = {0, 1, 2, 3, 4, 5, 6, 7};

	public BoardUtil() {};
	
	public static int getCoordinate(int file, int rank) {
		return (8*rank + file);
	}
	
	public static boolean isValidSquare(int file, int rank) {
		return ((file >= 0 & file < 8) & (rank >= 0 & rank < 8));
	}
	
	public static Color oppositeColor(Color color) {
		if(color.equals(Color.WHITE)) {
			return Color.BLACK;
		}else {
			return Color.WHITE;
		}
	}
	
}
