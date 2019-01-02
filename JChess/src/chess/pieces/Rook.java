package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Color;
import chess.Direction;
import chess.board.Board;
import chess.move.AttackMove;
import chess.move.Move;
import chess.move.NormalMove;

public class Rook extends Piece {
	
	private static final Direction[] VALID_MOVES = {Direction.N,Direction.S,Direction.E,Direction.W};

	public Rook(Color pieceColor, int rank, int file, boolean hasMoved) {
		super(pieceColor,rank,file, PieceType.ROOK,hasMoved);
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
		
		return board.movesToCheck(legalMoves);
	}

	@Override
	public Rook movePiece(Move move) {
		// TODO Auto-generated method stub
		Rook rook = new Rook(move.getMovePiece().getPieceColor(), move.getMoveFile(), move.getMoveRank(), true);
		return rook;
	}
}
