package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Alliance;
import chess.board.Board;
import chess.board.BoardUtil;
import chess.move.AttackMove;
import chess.move.Move;
import chess.move.NormalMove;

public class Knight extends Piece {
	
	private static final int[][] VALID_MOVES = {{2,1},{1,2},{2,-1},{1,-2},{-2,1},{-2,-1},{-1,2},{-1,-2}};
			
	public Knight(Alliance pieceColor, int file, int rank) {
		super(pieceColor,file,rank,PieceType.KNIGHT,false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Move> getLegalMoves(Board board) {
		// TODO Auto-generated method stub
		List<Move> legalMoves = new ArrayList<Move>();
		
		for(int[] validMove : VALID_MOVES) {
			int file = this.getFile() + validMove[0];
			int rank = this.getRank() + validMove[1];
			if(BoardUtil.isValidSquare(file, rank)) {
				if(!board.isSquareOccupied(file,rank)) {
					legalMoves.add(new NormalMove(file,rank,this, board));
				}else {
					Piece squarePiece = board.getPiece(file,rank);
					if(this.getPieceColor() != squarePiece.getPieceColor()) {
						legalMoves.add(new AttackMove(file,rank,this, board));
					}
				}
			}
		}
		return legalMoves;
	}

	@Override
	public Knight movePiece(Move move) {
		// TODO Auto-generated method stub
		Knight knight = new Knight(move.getMovePiece().getPieceColor(), move.getMoveFile(), move.getMoveRank());
		return knight;
	}
}
