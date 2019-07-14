package chess.board;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import chess.Alliance;
import chess.move.Move;
import chess.move.MoveType;
import chess.move.PawnPromotion;

public class BoardUtil {

	public static final int NUM_TILES = 64;

	public static final int RANKS = 8;

	public static final int FILES = 8;

	public static final int[] rToC = {7, 6, 5, 4, 3, 2, 1, 0};

	public static final int[] fToR = {0, 1, 2, 3, 4, 5, 6, 7};

	public static final int[] rToNo = {1, 2, 3, 4, 5, 6, 7, 8};

	public static final String[] fToA = {"a","b","c","d","e","f","g","h"};

	public BoardUtil() {};

	public static int getCoordinate(int file, int rank) {
		return (8*rank + file);
	}

	public static boolean isValidSquare(int file, int rank) {
		return ((file >= 0 & file < 8) & (rank >= 0 & rank < 8));
	}

	public static Alliance oppositeColor(Alliance color) {
		if(color.equals(Alliance.WHITE)) {
			return Alliance.BLACK;
		}else {
			return Alliance.WHITE;
		}
	}

	public static String getAlgebraicNotation(Move move) {
		String notation = "";
		notation = notation.concat(move.getMovePiece().getPieceType().getPieceNotation());
		Predicate<Move> pieceType = legalMove -> legalMove.getMovePiece().getPieceType() == move.getMovePiece().getPieceType();
		Predicate<Move>	notEqual = legalMove -> !legalMove.getMovePiece().equals(move.getMovePiece());
		Predicate<Move> sameMove = legalMove -> legalMove.getMoveFile() == move.getMoveFile() && legalMove.getMoveRank() == move.getMoveRank();
		Predicate<Move> sameFileRank = legalMove -> legalMove.getCurrentFile() == move.getCurrentFile() || legalMove.getCurrentRank() == move.getCurrentRank();
		List<Move> disamMoves = move.getBoard().getLegalMovesByColor(move.getMovePiece().getPieceColor()).stream().filter(pieceType.and(notEqual).and(sameMove).and(sameFileRank)).collect(Collectors.toList());
		boolean sameFile = disamMoves.stream().anyMatch(legalMove -> legalMove.getCurrentFile() == move.getCurrentFile());
		boolean sameRank = disamMoves.stream().anyMatch(legalMove -> legalMove.getCurrentRank() == move.getCurrentRank());
		if(sameFile & sameRank) {
			notation = notation.concat(fToA[move.getCurrentFile()]+rToNo[move.getCurrentRank()]);
		}else if(sameFile) {
			notation = notation.concat(Integer.toString(rToNo[move.getCurrentRank()]));
		}else if(sameRank) {
			notation = notation.concat(fToA[move.getCurrentFile()]);
		}
		if(move.getMoveType() == MoveType.Attack) {
			if(notation.equals("")) {
				notation = notation.concat(fToA[move.getMovePiece().getFile()]);
			}
			notation = notation.concat("x");
		}
		notation = notation.concat(fToA[move.getMoveFile()]+rToNo[move.getMoveRank()]);
		if(move.getClass().getName().endsWith("PawnPromotion")){
			PawnPromotion specialMove = (PawnPromotion) move;
			notation = notation.concat("="+specialMove.getPieceChosen().getPieceType().getPieceNotation());
		}
		if(move.getClass().getName().endsWith("ShortSideCastleMove")) {
			notation = "O-O";
		}
		if(move.getClass().getName().endsWith("LongSideCastleMove")) {
			notation = "O-O-O";
		}
		if(move.execute().getCurrentPlayer().isCheckMate()) {
			notation = notation.concat("#");
		}else if(move.execute().getCurrentPlayer().isInCheck()) {
			notation = notation.concat("+");
		}
		return notation;

	}
	
	public static List<Move> getValidMoves(List<Move> allMoves, Board board){
		for(Iterator<Move> iterator = allMoves.iterator(); iterator.hasNext();) {
			Move move = iterator.next();
			if(board.movesToCheck(move)) {
				iterator.remove();
			}
		}
		return allMoves;
		
	}
}
