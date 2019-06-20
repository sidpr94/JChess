
package chess;

import java.util.List;
import java.util.stream.Collectors;

import chess.Alliance;
import chess.board.Board;
import chess.move.Move;
import chess.pieces.King;
import chess.pieces.Piece;
import chess.pieces.PieceType;

public class Player {
	
	private Board board;
	private List<Piece> activePieces;
	private Alliance color;
	
	public Player(Board board,Alliance color){
		this.board = board;
		this.activePieces = board.getPieceByColor(color);
		this.color = color;
	}
	
	public List<Piece> getActivePieces(){
		return activePieces;
	}
	
	public King getKing() {
		List<Piece> king = activePieces.stream().filter(piece -> piece.getPieceType() == PieceType.KING).collect(Collectors.toList());
		return (King) king.get(0);
	}
	
	public boolean isInCheck() {
		List<Move> enemyMoves;
		King king = getKing();
		boolean isInCheck = false;
		if(getColor() == Alliance.WHITE) {
			enemyMoves = board.getBlackLegalMoves();
		}else {
			enemyMoves = board.getWhiteLegalMoves();		
		}
		for(Move move : enemyMoves) {
			if(move.getMoveFile() == king.getFile() && move.getMoveRank() == king.getRank()) {
				isInCheck = true;
			}
		}
		return isInCheck;
	}
	
	public Alliance getColor() {
		return color;
	};
	
	public boolean hasNoLegalMoves() {
		List<Move> moves;
		if(getColor() == Alliance.WHITE) {
			moves = board.getWhiteLegalMoves();
		}else {
			moves = board.getBlackLegalMoves();		
		}
		boolean noLegalMoves = true;
		for(Move move : moves) {
			if(!board.movesToCheck(move)) {
				noLegalMoves = false;
			}
		}
		return noLegalMoves;
	}
	public boolean isCheckMate() {
		return hasNoLegalMoves() && isInCheck();
	}
	
	public boolean isStaleMate() {
		
		return hasNoLegalMoves() && !isInCheck();
	}
	
}


