package chess.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import chess.Alliance;
import chess.move.Move;
import chess.move.NormalMove;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.NoPiece;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.PieceType;
import chess.pieces.Queen;
import chess.pieces.Rook;
import chess.pieces.SortByPiece;
import chess.Player;

public class Board {

	private final List<Piece> board;
	private final List<Piece> allActivePieces;
	private final List<Piece> activeWhitePieces;
	private final List<Piece> activeBlackPieces;
	private final List<Piece> allCapturedPieces;
	private final List<Piece> capturedWhitePieces;
	private final List<Piece> capturedBlackPieces;
	private final List<Move> whiteLegalMoves;
	private final List<Move> blackLegalMoves;
	private final List<Move> allLegalMoves;
	private final Player whitePlayer;
	private final Player blackPlayer;
	private final Pawn enPassantPawn;
	private final Alliance currentPlayerColor;

	private Board(Builder builder) {
		this.board = createGameBoard(builder);
		activeWhitePieces = setActivePieces(builder.boardConfig,Alliance.WHITE);
		activeBlackPieces = setActivePieces(builder.boardConfig,Alliance.BLACK);
		allActivePieces = Stream.concat(Stream.concat(activeWhitePieces.stream(), activeBlackPieces.stream()),setActivePieces(builder.boardConfig,Alliance.EMPTY).stream()).collect(Collectors.toList());
		this.allCapturedPieces = builder.capturedPieces;
		this.capturedWhitePieces = this.allCapturedPieces.stream().filter(piece -> piece.getPieceColor() == Alliance.WHITE).collect(Collectors.toList());
		Collections.sort(capturedWhitePieces, new SortByPiece());
		this.capturedBlackPieces = this.allCapturedPieces.stream().filter(piece -> piece.getPieceColor() == Alliance.BLACK).collect(Collectors.toList());
		Collections.sort(capturedBlackPieces, new SortByPiece());
		this.whitePlayer = new Player(this,Alliance.WHITE);
		this.blackPlayer = new Player(this,Alliance.BLACK);
		this.enPassantPawn = builder.enPassantPawn;
		whiteLegalMoves = setLegalMoves(activeWhitePieces);
		blackLegalMoves = setLegalMoves(activeBlackPieces);
		allLegalMoves = Stream.concat(whiteLegalMoves.stream(), blackLegalMoves.stream()).collect(Collectors.toList());
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

		builder.setPiece(new Rook(Alliance.WHITE,0, 0, false));
		builder.setPiece(new Knight(Alliance.WHITE,1, 0));
		builder.setPiece(new Bishop(Alliance.WHITE,2, 0));
		builder.setPiece(new Queen(Alliance.WHITE,3, 0));
		builder.setPiece(new King(Alliance.WHITE,4, 0, false));
		builder.setPiece(new Bishop(Alliance.WHITE,5, 0));
		builder.setPiece(new Knight(Alliance.WHITE,6, 0));
		builder.setPiece(new Rook(Alliance.WHITE,7, 0, false));

		for(int i = 0; i < BoardUtil.FILES; i++) {
			builder.setPiece(new Pawn(Alliance.WHITE,i, 1, false));
		}

		for(int i = 2; i < 6;i++) {
			for(int j = 0; j < BoardUtil.FILES;j++) {
				builder.setPiece(new NoPiece(j,i));
			}
		}

		for(int i = 0; i < BoardUtil.FILES; i++) {
			builder.setPiece(new Pawn(Alliance.BLACK,i, 6, false));
		}

		builder.setPiece(new Rook(Alliance.BLACK,0, 7, false));
		builder.setPiece(new Knight(Alliance.BLACK,1, 7));
		builder.setPiece(new Bishop(Alliance.BLACK,2, 7));
		builder.setPiece(new Queen(Alliance.BLACK,3, 7));
		builder.setPiece(new King(Alliance.BLACK,4, 7, false));
		builder.setPiece(new Bishop(Alliance.BLACK,5, 7));
		builder.setPiece(new Knight(Alliance.BLACK,6, 7));
		builder.setPiece(new Rook(Alliance.BLACK,7, 7, false));

		builder.setMover(Alliance.WHITE);
		builder.enPassantPawn(null);

		return builder.execute();

	}

	public static Board createTestBoard() {
		Builder builder = new Builder();

		for(int i = 0; i < BoardUtil.RANKS;i++) {
			for(int j = 0; j < BoardUtil.FILES;j++) {
				if(i == 0 && j == 0) {
					builder.setPiece(new King(Alliance.WHITE,0, 0, true));
				}else if(i == 5 && j == 7) {
					builder.setPiece(new Bishop(Alliance.WHITE,7,5));
				}else if(i == 4 && j == 2) {
					builder.setPiece(new King(Alliance.BLACK,2,4,true));
				}else if(i == 5 && j == 5){
					builder.setPiece(new Bishop(Alliance.WHITE,5,5));
				}else if(i == 6 && j == 6){
					builder.setPiece(new Queen(Alliance.BLACK,6,6));
				}else {
					builder.setPiece(new NoPiece(j,i));
				}
			}
		}

		builder.setMover(Alliance.WHITE);
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

	public boolean canShortSideCastle(Alliance color) {
		int rank = 0;
		if(color == Alliance.BLACK) {
			rank = 7;
		}
		List<Piece> activePieces = getPieceByColor(color);
		King king = (King) activePieces.stream().filter(piece -> piece.getPieceType() == PieceType.KING).collect(Collectors.toList()).get(0);
		Piece piece = getPiece(BoardUtil.FILES-1,rank);
		boolean rookHasMoved = piece.hasMoved();
		boolean isRook = piece.getPieceType() == PieceType.ROOK;
		boolean kingHasMoved = king.hasMoved();
		boolean pieceInBetween = false;
		for(int i = king.getFile()+1; i < BoardUtil.FILES-1;i++) {
			if(!(getPiece(i,rank).getPieceType() == PieceType.EMPTY)) {
				pieceInBetween = true;
			}
		}
		return (isRook) && (!rookHasMoved) && (!kingHasMoved) && (!pieceInBetween);
	}

	public boolean canLongSideCastle(Alliance color) {
		int rank = 0;
		if(color == Alliance.BLACK) {
			rank = 7;
		}
		Player player = getPlayerByColor(color);
		King king = player.getKing();
		Piece piece = getPiece(0,rank);
		boolean rookHasMoved = piece.hasMoved();
		boolean isRook = piece.getPieceType().equals(PieceType.ROOK);
		boolean kingHasMoved = king.hasMoved();
		boolean pieceInBetween = false;
		for(int i = king.getFile()-1; i > 0;i--) {
			if(getPiece(i,rank).getPieceType() != PieceType.EMPTY) {
				pieceInBetween = true;
			}
		}
		return (isRook) && (!rookHasMoved) && (!kingHasMoved) && (!pieceInBetween);
	}

	public boolean movesToCheck(Move move) {
		boolean passesCheck = false;
		King king = move.getBoard().getCurrentPlayer().getKing();
		if(move.getClass().getName().endsWith("ShortSideCastleMove")){
			NormalMove nextToKing = new NormalMove(king.getFile()+1, king.getRank(), king, move.getBoard());
			Player nextToKingPlayer = new Player(nextToKing.execute(),move.getBoard().getCurrentPlayerColor());
			passesCheck = nextToKingPlayer.isInCheck();
		}else if(move.getClass().getName().endsWith("LongSideCastleMove")) {
			NormalMove nextToKing = new NormalMove(king.getFile()-1, king.getRank(), king, move.getBoard());
			Player nextToKingPlayer = new Player(nextToKing.execute(),move.getBoard().getCurrentPlayerColor());
			passesCheck = nextToKingPlayer.isInCheck();
		}
		if(move.getClass().getName().endsWith("PawnPromotion")) {
			return passesCheck || move.getBoard().getCurrentPlayer().isInCheck();
		}else {
			Board moveBoard = move.execute();
			Player player = new Player(moveBoard,move.getBoard().getCurrentPlayerColor());
			return player.isInCheck() || passesCheck;
		}
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

	public List<Piece> getCapturedWhitePieces(){
		return capturedWhitePieces;
	}

	public List<Piece> getCapturedBlackPieces(){
		return  capturedBlackPieces;
	}

	public Player getWhitePlayer() {
		return whitePlayer;
	}

	public Player getBlackPlayer() {
		return blackPlayer;
	}

	public List<Move> getWhiteLegalMoves(){
		return whiteLegalMoves;
	}

	public List<Move> getBlackLegalMoves(){
		return blackLegalMoves;
	}

	public List<Move> getAllLegalMoves(){
		return allLegalMoves;
	}

	public Alliance getCurrentPlayerColor() {
		return currentPlayerColor;
	}

	public Player getCurrentPlayer() {
		if(currentPlayerColor == Alliance.WHITE) {
			return getWhitePlayer();
		}else {
			return getBlackPlayer();
		}
	}

	public Pawn getEnPassantPawn() {
		return enPassantPawn;
	}
	public List<Piece> getPieceByColor(Alliance color){
		return allActivePieces.stream().filter(piece -> piece.getPieceColor() == color).collect(Collectors.toList());
	}

	public Player getPlayerByColor(Alliance color) {
		if(color == Alliance.WHITE) {
			return getWhitePlayer();
		}else {
			return getBlackPlayer();
		}
	}

	public List<Move> getLegalMovesByColor(Alliance color){
		if(color == Alliance.WHITE) {
			return getWhiteLegalMoves();
		}else {
			return getBlackLegalMoves();
		}
	}

	public List<Piece> setActivePieces(HashMap<Integer,Piece> boardConfig,Alliance color){
		return boardConfig.values().stream().filter(piece -> piece.getPieceColor() == color).collect(Collectors.toList());
	}

	public List<Move> setLegalMoves(List<Piece> activePieces){
		List<Move> moves = new ArrayList<Move>();
		for(Piece piece : activePieces) {
			moves.addAll(piece.getLegalMoves(this));
		}
		return moves;

	}

	public boolean insufficientMaterial() {
		boolean insufficientMaterial = false;
		boolean whiteKingOnly = getActiveWhitePieces().stream().allMatch(piece -> piece.getPieceType() == PieceType.KING);
		boolean whiteKingKnightOnly = getActiveWhitePieces().stream().allMatch(piece -> piece.getPieceType() == PieceType.KING || piece.getPieceType() == PieceType.KNIGHT) && (getActiveWhitePieces().size() == 2);
		boolean whiteKingBishopOnly = getActiveWhitePieces().stream().allMatch(piece -> piece.getPieceType() == PieceType.KING || piece.getPieceType() == PieceType.BISHOP);

		List<Piece> whiteBishops = getActiveWhitePieces().stream().filter(piece -> piece.getPieceType() == PieceType.BISHOP).collect(Collectors.toList());

		boolean blackKingOnly = getActiveBlackPieces().stream().allMatch(piece -> piece.getPieceType() == PieceType.KING);
		boolean blackKingKnightOnly = getActiveBlackPieces().stream().allMatch(piece -> piece.getPieceType() == PieceType.KING || piece.getPieceType() == PieceType.KNIGHT) && (getActiveBlackPieces().size() == 2);
		boolean blackKingBishopOnly = getActiveBlackPieces().stream().allMatch(piece -> piece.getPieceType() == PieceType.KING || piece.getPieceType() == PieceType.BISHOP);
		List<Piece> blackBishops = getActiveBlackPieces().stream().filter(piece -> piece.getPieceType() == PieceType.BISHOP).collect(Collectors.toList());

		if((whiteKingOnly && blackKingOnly) || (whiteKingKnightOnly && blackKingKnightOnly) || (whiteKingOnly && blackKingKnightOnly) || (whiteKingKnightOnly && blackKingOnly)) {
			insufficientMaterial = true;
		}
		int whiteCounter = 0;
		for(Piece whiteBishop : whiteBishops) {
			if((whiteBishop.getRank() % 2) == 0) {
				whiteCounter = whiteCounter + (whiteBishop.getFile() % 2) == 0 ? 0 : 1;
			}else {
				whiteCounter = whiteCounter + (whiteBishop.getFile() % 2) == 0 ? 1 : 0;
			}
		}
		if(!whiteBishops.isEmpty()) {
			if(whiteCounter == 0 || whiteCounter == whiteBishops.size()) {
				if(blackKingOnly) {
					insufficientMaterial = true;
				}
			}
		}
		int blackCounter = 0;
		for(Piece blackBishop : blackBishops) {
			if((blackBishop.getRank() % 2) == 0) {
				blackCounter = blackCounter + (blackBishop.getFile() % 2) == 0 ? 0 : 1;
			}else {
				blackCounter = blackCounter + (blackBishop.getFile() % 2) == 0 ? 1 : 0;
			}
		}
		if(!blackBishops.isEmpty()) {
			if(blackCounter == 0 || blackCounter == blackBishops.size()) {
				if(whiteKingOnly) {
					insufficientMaterial = true;
				}
			}
		}
		if(whiteKingBishopOnly && blackKingBishopOnly) {
			if(whiteCounter == 0 && blackCounter == 0) {
				insufficientMaterial = true;
			}else if(whiteCounter == whiteBishops.size() && blackCounter == blackBishops.size()) {
				insufficientMaterial = true;
			}
		}
		return insufficientMaterial;
	}

	public static class Builder {

		HashMap<Integer,Piece> boardConfig = new HashMap<Integer,Piece>();
		List<Piece> capturedPieces = new ArrayList<Piece>();
		Alliance mover;
		Pawn enPassantPawn;
		public Builder() {};

		public void setPiece(Piece piece) {
			boardConfig.put(BoardUtil.getCoordinate(piece.getFile(), piece.getRank()),piece);
		}

		public void setMover(Alliance color) {
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
				System.out.print(piece.getPieceType().getPiecePrint(piece.getPieceColor())+" ");
			}
			System.out.println();
		}
	}

	@Override
	public boolean equals(Object object) {
		Board board = (Board) object;
		boolean isEqual = true;
		for(int j = 7; j > -1; j--) {
			for(int i =0; i < 8; i++) {
				Piece thisPiece = this.getPiece(i, j);
				Piece comparePiece = board.getPiece(i, j);
				if(thisPiece.getPieceType() != comparePiece.getPieceType() || 
						thisPiece.getPieceColor() != comparePiece.getPieceColor() ||
						this.getAllLegalMoves().size() != board.getAllLegalMoves().size()) {
					isEqual = false;
				}
			}
		}
		return isEqual;
	}
}
