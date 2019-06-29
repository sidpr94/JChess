package chess.gui;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class DragGestureHandler implements DragGestureListener, DragSourceListener {
	
	private PieceLabel pieceLabel;
	private Container parent;

	public DragGestureHandler(PieceLabel pieceLabel) {
		this.pieceLabel = pieceLabel;
	}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
		// TODO Auto-generated method stub
		if(!dsde.getDropSuccess()) {
			parent.add(pieceLabel);
			parent.invalidate();
			parent.repaint();
		}
		
	}

	@Override
	public void dragEnter(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DragSourceEvent dse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		// TODO Auto-generated method stub
		System.out.println("Drag Gesture Recognized");
		parent = pieceLabel.getParent();
		parent.remove(pieceLabel);
		parent.invalidate();
		parent.repaint();
		Transferable transferable = new PieceTransferable(pieceLabel);
		DragSource ds = dge.getDragSource();

		ImageIcon imageIcon = (ImageIcon) pieceLabel.getIcon();
		BufferedImage image = (BufferedImage) imageIcon.getImage();
		System.out.println(parent);
		System.out.println(image.getWidth()+" "+image.getHeight());
		System.out.println(dge.getDragOrigin());
		System.out.println(pieceLabel.getLocation());
		System.out.println(DragSource.isDragImageSupported());
		ds.startDrag(dge, Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR),image,new Point(30,30),transferable,this);
	}

}
