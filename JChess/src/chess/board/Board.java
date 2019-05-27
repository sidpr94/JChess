package chess.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import chess.Color;
import chess.move.Move;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.NoPiece;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.PieceType;
import chess.pieces.Queen;
import chess.pieces.Rook;
import chess.Player;

public class Board {
	
	private final List<Piece> board;
	private final List<Piece> allActivePieces;
	private final List<Piece> activeWhitePieces;
	private final List<Piece> activeBlackPieces;
	private final List<Piece> allCapturedPieces;
	//private final List<Piece> capturedWhitePieces;
	//private final List<Piece> capturedBlackPieces;
	private final Player whitePlayer;
	private final Player blackPlayer;
	private final Pawn enPassantPawn;
	private final Color currentPlayerColor;
	
	private Board(Builder builder) {
		this.board = createGameBoard(builder);
		activeWhitePieces = setActivePieces(builder.boardConfig,Color.WHITE);
		activeBlackPieces = setActivePieces(builder.boardConfig,Color.BLACK);
		allActivePieces = Stream.concat(activeWhitePieces.stream(), activeBlackPieces.stream()).collect(Collectors.toList());
		this.allCapturedPieces = builder.capturedPieces;
		//this.capturedWhitePieces = this.allCapturedPieces.stream().filter(piece -> piece.getPieceColor() == Color.WHITE).collect(Collectors.toList());
		//this.capturedBlackPieces = this.allCapturedPieces.stream().filter(piece -> piece.getPieceColor() == Color.BLACK).collect(Collectors.toList());
		this.whitePlayer = new Player(this, activeWhitePieces, Color.WHITE);
		this.blackPlayer = new Player(this,activeBlackPieces,Color.BLACK);
		this.enPassantPawn = builder.enPassantPawn;
		this.currentPlayerColor = builder.mover;
	}
	
	private static List<Piece> createGameBoard(Builder builder) {
		// TODO Auto-generated method stub
		Piece[] pieces = new Piece[BoardUtil.NUM_TILES];
		for(int i = 0; i < BoardUtil.NUM_TILES; i++) {
			pieces[i] = builder.boardConfig.get(i);
		}
		return Collections.unmodifiableList(Arrays.asList(pieces));
	}
	
	public static Board createStandardBoard() {
		// TODO Auto-generated method stub
		Builder builder = new Builder();
		
		builder.setPiece(new Rook(Color.WHITE,0, 0, false));
		builder.setPiece(new Knight(Color.WHITE,1, 0));
		builder.setPiece(new Bishop(Color.WHITE,2, 0));
		builder.setPiece(new Queen(Color.WHITE,3, 0));
		builder.setPiece(new King(Color.WHITE,4, 0, false));
		builder.setPiece(new Bishop(Color.WHITE,5, 0));
		builder.setPiece(new Knight(Color.WHITE,6, 0));
		builder.setPiece(new Rook(Color.WHITE,7, 0, false));
		
		for(int i = 0; i < BoardUtil.FILES; i++) {
			builder.setPiece(new Pawn(Color.WHITE,i, 1, false));
		}
		
		for(int i = 2; i < 6;i++) {
			for(int j = 0; j < BoardUtil.FILES;j++) {
				builder.setPiece(new NoPiece(j,i));
			}
		}
		
		for(int i = 0; i < BoardUtil.FILES; i++) {
			builder.setPiece(new Pawn(Color.BLACK,i, 6, false));
		}
		
		builder.setPiece(new Rook(Color.BLACK,0, 7, false));
		builder.setPiece(new Knight(Color.BLACK,1, 7));
		builder.setPiece(new Bishop(Color.BLACK,2, 7));
		builder.setPiece(new Queen(Color.BLACK,3, 7));
		builder.setPiece(new King(Color.BLACK,4, 7, false));
		builder.setPiece(new Bishop(Color.BLACK,5, 7));
		builder.setPiece(new Knight(Color.BLACK,6, 7));
		builder.setPiece(new Rook(Color.BLACK,7, 7, false));
		
		builder.setMover(Color.WHITE);
		builder.enPassantPawn(null);
		
		return builder.execute();
		
	}

	public boolean isSquareOccupied(int file, int rank) {
		// TODO Auto-generated method stub
		return getPiece(file,rank).getPieceType() != PieceType.EMPTY ? true : false;
	}

	public Piece getPiece(int file, int rank) {
		// TODO Auto-generated method stub
		return board.get(BoardUtil.getCoordinate(file, rank));
	}
	
	public boolean canShortSideCastle(Color color) {
		int rank = 0;
		if(color == Color.BLACK) {
			rank = 7;
		}
		List<Piece> activePieces = getPieceByColor(color);
		King king = (King) activePieces.stream().filter(piece -> piece.getPieceType() == PieceType.KING).collect(Collectors.toList()).get(0);
		Piece piece = getPiece(BoardUtil.FILES-1,rank);
		boolean rookHasMoved = piece.hasMoved();
        boolean isRook = piece.getPieceType() == PieceType.ROOK;
		boolean kingHasMoved = king.hasMoved();
		boolean pieceInBetween = false;
		boolean kingPassesCheck = false;
		for(int i = king.getFile()+1; i < BoardUtil.FILES-1;i++) {
			if(!(getPiece(i,rank).getPieceType() == PieceType.EMPTY)) {
				pieceInBetween = true;
			}
			//King kingCheck = new King(color, i, rank, false);
			/*if(kingCheck.isInCheck(this)) {
				kingPassesCheck = true;
			}*/
		}
		return (isRook) && (!rookHasMoved) && (!kingHasMoved) && (!pieceInBetween) && (!kingPassesCheck);
	}
	
	public boolean canLongSideCastle(Color color) {
		int rank = 0;
		if(color == Color.BLACK) {
			rank = 7;
		}
		List<Piece> activePieces = getPieceByColor(color);
		King king = (King) activePieces.stream().filter(piece -> piece.getPieceType() == PieceType.KING).collect(Collectors.toList()).get(0);
		Piece piece = getPiece(0,rank);
		boolean rookHasMoved = piece.hasMoved();
		boolean isRook = piece.getPieceType().equals(PieceType.ROOK);
		boolean kingHasMoved = king.hasMoved();
		boolean pieceInBetween = false;
		boolean kingPassesCheck = false;
		for(int i = king.getFile()-1; i > 0;i--) {
			if(!(getPiece(i,rank).getPieceType() == PieceType.EMPTY)) {
				pieceInBetween = true;
			}
			/*
			King kingCheck = new King(color, i, rank, false);
			if(kingCheck.isInCheck(this)) {
				kingPassesCheck = true;
			}*/
		}
		return (isRook) && (!rookHasMoved) && (!kingHasMoved) && (!pieceInBetween) && (!kingPassesCheck);
	}
	
	public List<Move> movesToCheck(List<Move> legalMoves) {
		for(Move move : legalMoves) {
			Board moveBoard = move.execute();
			Player currentPlayer = moveBoard.getCurrentPlayer();
			if(currentPlayer.getKing().isInCheck(moveBoard)) {
				legalMoves.remove(move);
			};
		}
		return legalMoves;
	}
	
	public List<Piece> getAllActivePieces(){
		return allActivePieces;
	}
	
	public List<Piece> getActiveWhitePieces(){
		return activeWhitePieces;
	}
	
	public List<Piece> getActiveBlackPieces(){
		return activeBlackPieces;
	}
	
	public List<Piece> getAllCapturedPieces(){
		return allCapturedPieces;
	}
	
	/*
	public List<Piece> getCapturedWhitePieces(){
		return capturedWhitePieces;
	}
	
	public List<Piece> getCapturedBlackPieces(){
		return  capturedBlackPieces;
	}
	*/
	
	public Player getWhitePlayer() {
		return whitePlayer;
	}
	
	public Player getBlackPlayer() {
		return blackPlayer;
	}
	
	public Color getCurrentPlayerColor() {
		return currentPlayerColor;
	}
	
	public Player getCurrentPlayer() {
		if(currentPlayerColor == Color.WHITE) {
			return getWhitePlayer();
		}else {
			return getBlackPlayer();
		}
	}
	
	public Pawn getEnPassantPawn() {
		return enPassantPawn;
	}
	public List<Piece> getPieceByColor(Color color){
		return allActivePieces.stream().filter(piece -> piece.getPieceColor() == color).collect(Collectors.toList());
	}
	
	public List<Piece> setActivePieces(HashMap<Integer,Piece> boardConfig,Color color){
		return boardConfig.values().stream().filter(piece -> piece.getPieceColor() == color).collect(Collectors.toList());
	}
	
	public static class Builder {
		
		HashMap<Integer,Piece> boardConfig = new HashMap<Integer,Piece>();
		List<Piece> capturedPieces;
		Color mover;
		Pawn enPassantPawn;
		public Builder() {};
		
		public void setPiece(Piece piece) {
			boardConfig.put(BoardUtil.getCoordinate(piece.getFile(), piece.getRank()),piece);
		}
		
		public void setMover(Color color) {
			this.mover = color;
		}
		
		public void enPassantPawn(Pawn pawn) {
			this.enPassantPawn = pawn;
		}
		
		public void setCapturedPiece(Piece piece) {
			this.capturedPieces.add(piece);
		}
		
		public Board execute() {
			return new Board(this);
		}
	}
	
	public static void printBoard(Board board) {
		for(int j = 7; j > -1; j--) {
			for(int i =0; i < 8; i++) {
				Piece piece = board.getPiece(i, j);
				System.out.print(piece.getPieceType().getPieceNotation(piece.getPieceColor())+" ");
			}
			System.out.println();
		}
	}
}
