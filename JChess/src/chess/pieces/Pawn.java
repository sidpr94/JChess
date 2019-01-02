package chess.pieces;

import java.util.ArrayList;
import java.util.List;

import chess.Color;
import chess.board.Board;
import chess.board.BoardUtil;
import chess.move.AttackMove;
import chess.move.DoublePawnMove;
import chess.move.EnPassantMove;
import chess.move.Move;
import chess.move.NormalMove;
import chess.move.PawnPromotion;

public class Pawn extends Piece {
	
	private static int[] VALID_MOVE = {0,1};
	private static int[][] VALID_ATTACK_MOVES = {{1,1},{-1,1}};
	
	public Pawn(Color pieceColor, int file,int rank,boolean hasMoved) {
		super(pieceColor,file,rank,PieceType.PAWN,hasMoved);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Move> getLegalMoves(Board board) {
		// TODO Auto-generated method stub
		int file;
		int rank;
		int rank2;
		
		List<Move> legalMoves = new ArrayList<Move>();
		if(this.getPieceColor().equals(Color.WHITE)) {
			file = this.getFile() + VALID_MOVE[0];
			rank = this.getRank() + VALID_MOVE[1];
			rank2 = this.getRank() + 2*VALID_MOVE[1];
		}else {
			file = this.getFile() + VALID_MOVE[0];
			rank = this.getRank() - VALID_MOVE[1];
			rank2 = this.getRank() - 2*VALID_MOVE[1];
			
		}
		if(rank == 0 || rank == 7) {
			legalMoves.add(new PawnPromotion(file,rank,this,board));
		}else {
			if(BoardUtil.isValidSquare(file, rank)) {
				if(!board.isSquareOccupied(file,rank)) {
					legalMoves.add(new NormalMove(file,rank,this, board));
					if(!hasMoved()){
						legalMoves.add(new DoublePawnMove(file,rank2,this, board));
					}
				}
				for(int[] attackMove : VALID_ATTACK_MOVES) {
					int attackFile;
					int attackRank;
					if(this.getPieceColor().equals(Color.WHITE)) {
						attackFile = this.getFile() + attackMove[0];
						attackRank = this.getRank() + attackMove[1];
					}else {
						attackFile = this.getFile() + attackMove[0];
						attackRank = this.getRank() - attackMove[1];
						
					}
					if(board.isSquareOccupied(attackFile, attackRank) && (board.getPiece(attackFile, attackRank).getPieceColor() != this.getPieceColor())) {
						legalMoves.add(new AttackMove(attackFile,attackRank,this,board));
					}
					if(canEnPassant(board) && (board.getPiece(this.getFile(),attackRank).equals(board.getEnPassantPawn()))) {
						legalMoves.add(new EnPassantMove(attackFile,attackRank,this, board)); //fix
					}
				}
			}
		}
		return board.movesToCheck(legalMoves);
	}
	
	private boolean canEnPassant(Board board) {
		// TODO Auto-generated method stub
		boolean canEnPassant = false;
		
		Pawn pawn = board.getEnPassantPawn();
		
		if(!pawn.equals(null)) {
			int square1 = BoardUtil.getCoordinate(pawn.getFile()+1, pawn.getRank());
			int square2 = BoardUtil.getCoordinate(pawn.getFile()-1, pawn.getRank());
			int thisSquare = BoardUtil.getCoordinate(this.getFile(), this.getRank());
			
			if(thisSquare == square1 || thisSquare == square2) {
				canEnPassant = true;
			}
		}
		return canEnPassant;
		
	}

	@Override
	public Pawn movePiece(Move move) {
		// TODO Auto-generated method stub
		Pawn pawn = new Pawn(move.getMovePiece().getPieceColor(),move.getMoveFile(),move.getMoveRank(),true);
		return pawn;
	}

}
