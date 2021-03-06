package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Alliance;
import chess.board.Board;
import chess.board.BoardUtil;
import chess.move.AttackMove;
import chess.move.LongSideCastleMove;
import chess.move.Move;
import chess.move.NormalMove;
import chess.move.ShortSideCastleMove;

public class King extends Piece {

	private static final int[][] VALID_MOVES = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};

	public King(Alliance pieceColor, int file, int rank,boolean hasMoved) {
		super(pieceColor,file,rank,PieceType.KING,hasMoved);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Move> getLegalMoves(Board board) {

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
		if(board.canShortSideCastle(this.getPieceColor())) {
			legalMoves.add(new ShortSideCastleMove(this, board));
		}
		if(board.canLongSideCastle(this.getPieceColor())) {
			legalMoves.add(new LongSideCastleMove(this, board));
		}
		// TODO Auto-generated method stub
		return legalMoves;
	}

	@Override
	public King movePiece(Move move) {
		// TODO Auto-generated method stub
		King king = new King(move.getMovePiece().getPieceColor(), move.getMoveFile(), move.getMoveRank(), true);
		return king;
	}

}
