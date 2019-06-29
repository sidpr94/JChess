package chess.gui;

import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import chess.pieces.Piece;
import chess.pieces.PieceType;

public class PieceLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DragGestureHandler dragGestureHandler;
	private DragGestureRecognizer dgr;
	
	public PieceLabel() {
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		//setOpaque(false);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(80,80);
	}

	public void setIcon(Piece piece) {
		if(piece.getPieceType() != PieceType.EMPTY) {
			BufferedImage image = null;
			try {
				image = ImageIO.read(new File("images/"+piece.getPieceColor().getColorString()+piece.getPieceType().getPieceTypeString()+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageIcon pieceImage = new ImageIcon();
			pieceImage.setImage(image);
			setIcon(pieceImage);
			setVerticalAlignment(SwingConstants.CENTER);
			setHorizontalAlignment(SwingConstants.CENTER);	
		}
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		if(dgr == null) {
			dragGestureHandler = new DragGestureHandler(this);
			dgr = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, dragGestureHandler);
		}
	}
	
	@Override
	public void removeNotify() {
		super.addNotify();
		if(dgr != null) {
			dgr.removeDragGestureListener(dragGestureHandler);
			dragGestureHandler = null;
		}
	}
}
