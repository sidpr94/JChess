package chess.engine;

import java.util.List;

import chess.Alliance;
import chess.Player;
import chess.board.Board;
import chess.board.BoardUtil;
import chess.move.Move;
import chess.pieces.PieceType;

public class BoardEvaluator {
	
	public BoardEvaluator() {
		
	}
	
	
	public int evaluate(Board board) {
		
		return getMaterialScore(board)*mobilityScore(board)*getWhoToMove(board.getCurrentPlayer());
	}
	
	private int getMaterialScore(Board board) {
		
		int whiteQueens = (int) board.getActiveWhitePieces().stream().filter(piece -> piece.getPieceType() == PieceType.QUEEN).count();
		int whiteRooks = (int) board.getActiveWhitePieces().stream().filter(piece -> piece.getPieceType() == PieceType.ROOK).count();
		int whiteBishops = (int) board.getActiveWhitePieces().stream().filter(piece -> piece.getPieceType() == PieceType.BISHOP).count();
		int whiteKnights = (int) board.getActiveWhitePieces().stream().filter(piece -> piece.getPieceType() == PieceType.KNIGHT).count();
		int whitePawns = (int) board.getActiveWhitePieces().stream().filter(piece -> piece.getPieceType() == PieceType.PAWN).count();
		
		int blackQueens = (int) board.getActiveBlackPieces().stream().filter(piece -> piece.getPieceType() == PieceType.QUEEN).count();
		int blackRooks = (int) board.getActiveBlackPieces().stream().filter(piece -> piece.getPieceType() == PieceType.ROOK).count();
		int blackBishops = (int) board.getActiveBlackPieces().stream().filter(piece -> piece.getPieceType() == PieceType.BISHOP).count();
		int blackKnights = (int) board.getActiveBlackPieces().stream().filter(piece -> piece.getPieceType() == PieceType.KNIGHT).count();
		int blackPawns = (int) board.getActiveBlackPieces().stream().filter(piece -> piece.getPieceType() == PieceType.PAWN).count();
		
		int queenAdvantage = PieceType.QUEEN.getMaterialCount()*(whiteQueens - blackQueens);
		int rookAdvantage = PieceType.ROOK.getMaterialCount()*(whiteRooks - blackRooks);
		int bishopAdvantage = PieceType.BISHOP.getMaterialCount()*(whiteBishops - blackBishops);
		int knightAdvantage = PieceType.KNIGHT.getMaterialCount()*(whiteKnights - blackKnights);
		int pawnAdvantage = PieceType.PAWN.getMaterialCount()*(whitePawns - blackPawns);
		
		return 100*(queenAdvantage + rookAdvantage + bishopAdvantage + knightAdvantage + pawnAdvantage);
		
	}
	
	private int getWhoToMove(Player player) {
		if(player.getColor() == Alliance.WHITE) {
			return 1;
		}
		return -1;
		
	}
	
	private int mobilityScore(Board board) {
		List<Move> whiteLegalMoves = BoardUtil.getValidMoves(board.getWhiteLegalMoves(), board);
		List<Move> blackLegalMoves = BoardUtil.getValidMoves(board.getBlackLegalMoves(), board);
		return 50*(whiteLegalMoves.size() - blackLegalMoves.size());
	}

}
