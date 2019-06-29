package chess.gui;

import java.awt.datatransfer.DataFlavor;

public class PieceDataFlavor extends DataFlavor {
	
	public static final PieceDataFlavor SHARED_INSTANCE = new PieceDataFlavor();
	
	public PieceDataFlavor() {
		super(PieceLabel.class,null);
	}

}
