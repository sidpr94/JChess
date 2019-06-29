package chess.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class PieceTransferable implements Transferable {

	private PieceLabel pieceLabel;
	private DataFlavor[] flavors = new DataFlavor[] {PieceDataFlavor.SHARED_INSTANCE};

	public PieceTransferable(PieceLabel pieceLabel) {
		this.pieceLabel = pieceLabel;
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		Object data = null;
		if(isDataFlavorSupported(flavor)) {
			data = pieceLabel;
		}else {
			throw new UnsupportedFlavorException(flavor);
		}
		return data;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// TODO Auto-generated method stub
		boolean supported = false;
		for(DataFlavor flav : getTransferDataFlavors()) {
			if(flav.equals(flavor)) {
				supported = true;
				break;
			}
		}
		return supported;
	}

}
