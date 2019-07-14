package chess.move;

import java.util.List;

import chess.board.Board;
import chess.board.BoardUtil;
import chess.board.Board.Builder;
import chess.pieces.NoPiece;
import chess.pieces.Piece;


public class PawnPromotion extends Move{

	private Piece chosenPiece;

	public PawnPromotion(int file, int rank, Piece piece, Board board,MoveType moveType) {
		super(file, rank, piece, board,moveType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Board execute() {
		Board moveBoard;
		if(getMoveType() == MoveType.Attack) {
			Board currentBoard = getBoard();
			List<Piece> activePieces = currentBoard.getAllActivePieces();
			List<Piece> capturedPieces = currentBoard.getAllCapturedPieces();
			Builder builder = new Builder();
			for(Piece piece : activePieces) {
				if(piece.equals(getMovePiece())) {
					builder.setPiece(chosenPiece);
				}else if(piece.getFile() == getMoveFile() && piece.getRank() == getMoveRank()) {
					builder.setPiece(new NoPiece(getMovePiece().getFile(), getMovePiece().getRank()));	
					builder.setCapturedPiece(piece);
				}else {
					builder.setPiece(piece);
				}
			}
			capturedPieces.forEach(piece -> builder.setCapturedPiece(piece));
			builder.setMover(BoardUtil.oppositeColor(currentBoard.getCurrentPlayerColor()));
			builder.enPassantPawn(null);

			moveBoard = builder.execute();
		}else {
			Board currentBoard = getBoard();
			List<Piece> activePieces = currentBoard.getAllActivePieces();
			List<Piece> capturedPieces = currentBoard.getAllCapturedPieces();
			Builder builder = new Builder();
			for(Piece piece : activePieces) {
				if(piece.equals(getMovePiece())) {
					builder.setPiece(chosenPiece);
				}else if(piece.getFile() == getMoveFile() && piece.getRank() == getMoveRank()){
					builder.setPiece(new NoPiece(getMovePiece().getFile(), getMovePiece().getRank()));
				}else {
					builder.setPiece(piece);
				}
			}
			capturedPieces.forEach(piece -> builder.setCapturedPiece(piece));
			builder.setMover(BoardUtil.oppositeColor(currentBoard.getCurrentPlayerColor()));
			builder.enPassantPawn(null);
			
			moveBoard = builder.execute();
		}
		return moveBoard;
		// TODO Auto-generated method stub

	}
	
	public Piece getPieceChosen() {
		return this.chosenPiece;
	}

	public void setChosenPiece(Piece chosenPiece) {
		// TODO Auto-generated method stub
		this.chosenPiece = chosenPiece;
	}
}
