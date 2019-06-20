package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Alliance;
import chess.Direction;
import chess.board.Board;
import chess.board.BoardUtil;
import chess.move.AttackMove;
import chess.move.Move;
import chess.move.NormalMove;

public class Queen extends Piece {

	private static final Direction[] VALID_MOVES = {Direction.N,Direction.S,Direction.E,Direction.W,Direction.NE,Direction.SE,Direction.NW,Direction.SW};

	public Queen(Alliance pieceColor, int file, int rank) {
		super(pieceColor,file,rank,PieceType.QUEEN,false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Move> getLegalMoves(Board board) {
		// TODO Auto-generated method stub
		List<Move> legalMoves = new ArrayList<Move>();
		for(Direction d: VALID_MOVES) {
			for(int[] candidateMove : d.goDirection(this.getFile(),this.getRank())) {
				if(BoardUtil.isValidSquare(candidateMove[0], candidateMove[1])) {
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
		}

		return legalMoves;
	}

	@Override
	public Piece movePiece(Move move) {
		// TODO Auto-generated method stub
		Queen queen = new Queen(move.getMovePiece().getPieceColor(), move.getMoveFile(), move.getMoveRank());
		return queen;
	}

}
