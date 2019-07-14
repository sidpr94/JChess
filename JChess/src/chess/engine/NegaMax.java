package chess.engine;

import java.util.List;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.move.Move;
import chess.move.NullMove;
import chess.move.PawnPromotion;
import chess.pieces.Queen;

public class NegaMax {

	private int searchDepth;
	private BoardEvaluator evaluator;
	private int boardsEvaluated;

	public NegaMax(int searchDepth) {
		this.searchDepth = searchDepth;
		this.boardsEvaluated = 0;
		this.evaluator = new BoardEvaluator();

	}

	public Move execute(Board board) {
		final long startTime = System.currentTimeMillis();
		int currentValue = Integer.MIN_VALUE;
		Move bestMove = new NullMove();
		int moveCounter = 1;
		System.out.println(board.getCurrentPlayer().getColor()+" player thinking with depth "+searchDepth);
		List<Move> validMoves = BoardUtil.getValidMoves(board.getLegalMovesByColor(board.getCurrentPlayerColor()),board);
		int numMoves = validMoves.size();
		for(Move move : validMoves){
			int score = negaMax(move,searchDepth);
			System.out.println("analyzing move "+move+" "+moveCounter+"/"+numMoves+" scores "+score);
			if(score > currentValue) {
				currentValue = score;
				bestMove = move;
			}
			moveCounter++;
		}
		long executionTime = System.currentTimeMillis() - startTime;
		System.out.println(boardsEvaluated + " boards evaluated over "+executionTime+" ms");
		return bestMove;

	}
	
	public int negaMax(Move move,int searchDepth) {
		int max = Integer.MIN_VALUE;
		if(move.getClass().getName().endsWith("PawnPromotion")) {
			PawnPromotion specialMove = (PawnPromotion) move;
			specialMove.setChosenPiece(new Queen(move.getMovePiece().getPieceColor(), move.getMoveFile(),move.getMoveRank()));
		}
		if(searchDepth == 0) {
			this.boardsEvaluated++;
			return evaluator.evaluate(move.getBoard());
		}
		Board board = move.execute();
		for(Move legalMove : BoardUtil.getValidMoves(board.getLegalMovesByColor(board.getCurrentPlayerColor()),board)) {
			int score = -negaMax(legalMove,searchDepth - 1);
			if(score > max) {
				max = score;
			}
		}
		return max;
		
	}

}
