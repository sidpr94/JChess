package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Color;
import chess.Direction;
import chess.board.Board;
import chess.move.AttackMove;
import chess.move.Move;
import chess.move.NormalMove;

public class Bishop extends Piece {
	
	private static final Direction[] VALID_MOVES = {Direction.NE,Direction.SE,Direction.NW,Direction.SW};

	public Bishop(Color pieceColor, int file,int rank) {
		super(pieceColor,file,rank,PieceType.BISHOP,false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Move> getLegalMoves(Board board) {
		// TODO Auto-generated method stub
		List<Move> legalMoves = new ArrayList<Move>();
		for(Direction d: VALID_MOVES) {
			for(int[] candidateMove : d.goDirection(this.getFile(),this.getRank())) {
				if(!board.isSquareOccupied(candidateMove[0],candidateMove[1])) {
					legalMoves.add(new NormalMove(candidateMove[0],candidateMove[1],this, board));
				}else {
					Piece squarePiece = board.getPiece(candidateMove[0],candidateMove[1]);
					if(this.getPieceColor() != squarePiece.getPieceColor()) {
						legalMoves.add(new AttackMove(candidateMove[0],candidateMove[1],this, board));
					}
					break;
				}
				
			}
		}
		
		return legalMoves;
	}

	@Override
	public Bishop movePiece(Move move) {
		// TODO Auto-generated method stub
		Bishop bishop = new Bishop(move.getMovePiece().getPieceColor(), move.getMoveFile(), move.getMoveRank());
		return bishop;
	}

}
