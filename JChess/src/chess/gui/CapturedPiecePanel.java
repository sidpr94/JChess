package chess.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import chess.Alliance;
import chess.pieces.PieceType;

public class CapturedPiecePanel {

	private Alliance color;
	private JPanel panel;
	private JPanel pawnPanel;
	private JPanel bishopPanel;
	private JPanel knightPanel;
	private JPanel rookPanel;
	private JPanel queenPanel;

	public CapturedPiecePanel(Alliance color) {
		this.color = color;
		panel = new JPanel(new FlowLayout()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(80*4, 40);
			}
			
		};
		panel.setBackground(new Color(48,46,43));
		if(color == Alliance.WHITE) {
			panel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
		}else {
			panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
		}
		pawnPanel = createPieceJPanel(PieceType.PAWN);
		bishopPanel = createPieceJPanel(PieceType.BISHOP);
		knightPanel = createPieceJPanel(PieceType.KNIGHT);
		rookPanel = createPieceJPanel(PieceType.ROOK);
		queenPanel = createPieceJPanel(PieceType.QUEEN);
		panel.add(pawnPanel);
		panel.add(bishopPanel);
		panel.add(knightPanel);
		panel.add(rookPanel);
		panel.add(queenPanel);
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
				g2.drawImage(image, 0, 0, 40, 40, null);			
			}
			
			@Override
			public boolean isOpaque() {
				return false;
			}
			
			@Override
			public Border getBorder() {
				return BorderFactory.createLineBorder(Color.BLACK);
			}			
		};
		return piecePanel;
	}

	public JPanel getCapturedPiecePanel() {
		return panel;
	}
}
