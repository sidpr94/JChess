package chess;

import java.util.ArrayList;
import java.util.List;

import chess.board.BoardUtil;

public enum Direction {
	N(0,1),
	S(0,-1),
	E(1,0),
	W(-1,0),
	NE(1,1),
	NW(1,-1),
	SE(-1,1),
	SW(-1,-1);
	
	private final int x;
	private final int y;
	
	private Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public List<int []> goDirection(int f,int r) {
		List<int []> coordinates = new ArrayList<int []>();
		int file = f + this.x;
		int rank = r + this.y;
		while(BoardUtil.isValidSquare(file,rank)) {
			int [] coordinate = {file,rank};
			coordinates.add(coordinate);
			file = file + this.x;
			rank = rank + this.y;
		}
		return coordinates;
	}
}
