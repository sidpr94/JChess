package chess;

import java.util.List;
import java.util.stream.Collectors;

import chess.Color;
import chess.board.Board;
import chess.pieces.King;
import chess.pieces.Piece;
import chess.pieces.PieceType;

public class Player {
	
	private Board board;
	private List<Piece> activePieces;
	private Color color;
	
	public Player(Board board,List<Piece> activePieces,Color color){
		this.board = board;
		this.activePieces = activePieces;
		this.color = color;
	}
	
	public boolean isCheckMate() {
		King king = getKing();
		return (king.getLegalMoves(board).isEmpty() && king.isInCheck(board));
	}
	
	public List<Piece> getActivePieces(){
		return activePieces;
	}
	
	public King getKing() {
		List<Piece> king = activePieces.stream().filter(piece -> piece.getPieceType() == PieceType.KING).collect(Collectors.toList());
		return (King) king.get(0);
	}
	
	public Color getColor() {
		return color;
	};
}


