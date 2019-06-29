package chess.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JComponent;

import chess.gui.ChessPanels.TilePanel;

public class DropHandler implements DropTargetListener {
	
	
	private TilePanel tilePanel;

	public DropHandler(TilePanel tilePanel) {
		this.tilePanel = tilePanel;
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		if(dtde.isDataFlavorSupported(PieceDataFlavor.SHARED_INSTANCE)) {
			dtde.acceptDrag(DnDConstants.ACTION_MOVE);
		}else {
			dtde.rejectDrag();
		}
		
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub
		boolean success = false;
		if(dtde.isDataFlavorSupported(PieceDataFlavor.SHARED_INSTANCE)) {
			Transferable transferable = dtde.getTransferable();
			try {
				Object data = transferable.getTransferData(PieceDataFlavor.SHARED_INSTANCE);
				if(data instanceof PieceLabel) {
					PieceLabel pieceLabel = (PieceLabel) data;
					DropTargetContext dtc = dtde.getDropTargetContext();
					Component component = dtc.getComponent();
					if(component instanceof JComponent) {
						Container parent = pieceLabel.getParent();
						if(parent != null) {
							parent.remove(pieceLabel);
						}
						((JComponent) component).add(pieceLabel);
						success = true;
						dtde.acceptDrop(DnDConstants.ACTION_MOVE);
						tilePanel.invalidate();
						tilePanel.repaint();
					}else {
						success = false;
						dtde.rejectDrop();
					}
				}else {
					success = false;
					dtde.rejectDrop();
				}
			} catch (Exception e) {
				success = false;
				dtde.rejectDrop();
				e.printStackTrace();
			}
		}else {
			success =false;
			dtde.rejectDrop();
		}
		dtde.dropComplete(success);
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
	
	

}
