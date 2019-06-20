package chess.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import chess.Alliance;
import chess.board.BoardUtil;
import chess.move.Move;
import chess.pieces.PieceType;

public class CapturedPiecePanel {
	
	private Alliance color;
	private JPanel panel;
	
	public CapturedPiecePanel(Alliance color) {
		this.color = color;
		panel = new JPanel();
		panel.add(createPieceJPanel(PieceType.PAWN));
	}
	
	private JPanel createPieceJPanel(PieceType type) {
		JPanel piecePanel = new JPanel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				BufferedImage image = null;
				try {
					image = ImageIO.read(new File("images/"+color.getColorString()+type.getPieceTypeString()+".png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				g2.drawImage(image, 0, 0, 10, 10, null);
			}
			
		};
		return piecePanel;
	}

	public JPanel getCapturedPiecePanel() {
		return panel;
	}
}
