package chess.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import chess.pieces.Piece;

public class CapturedPiecePanel {

	private JPanel panel;
	private JTextField text;

	public CapturedPiecePanel() {
		panel = new JPanel(new FlowLayout(FlowLayout.LEADING,0,5)) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(80*8, 40);
			}
		};
		panel.setBackground(new Color(22,21,18));
		panel.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 0, new Color(22,21,18)));
		text = new JTextField();
		text.setEditable(false);
		text.setBackground(new Color(22,21,18));
		text.setForeground(new Color(222,227, 230));
		text.setFont(new Font(Font.DIALOG,Font.PLAIN,12));
		text.setBorder(BorderFactory.createEmptyBorder());
	}

	public JPanel getCapturedPiecePanel() {
		return panel;
	}

	public void addCapturedPieces(List<Piece> capturedPieces) {
		panel.removeAll();
		for(Piece piece : capturedPieces) {
			BufferedImage thumbImage = null;
			BufferedImage image = null;
			try {
				image = ImageIO.read(new File("images/"+piece.getPieceColor().getColorString()+piece.getPieceType().getPieceTypeString()+".png"));
				thumbImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = thumbImage.createGraphics();
				g2d.drawImage(image.getScaledInstance(30, 30, Image.SCALE_SMOOTH), 0, 0, 30, 30, null);
				g2d.dispose();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageIcon pieceImage = new ImageIcon();
			pieceImage.setImage(thumbImage);
			JLabel label = new JLabel(pieceImage,SwingConstants.CENTER);
			label.setPreferredSize(new Dimension(30,30));
			label.setVerticalAlignment(SwingConstants.CENTER);
			label.setOpaque(false);
			panel.add(label);
		}
		panel.add(text);
		
		panel.revalidate();
	}

	public void removeAllCapturedPieces() {
		panel.removeAll();
		panel.repaint();
	}
	
	public void setTextAdvantage(List<Piece> capturedPieces, List<Piece> capturedEnemyPieces) {
		int material = capturedPieces.stream().mapToInt(piece -> piece.getPieceType().getMaterialCount()).sum();
		int enemyMaterial = capturedEnemyPieces.stream().mapToInt(piece -> piece.getPieceType().getMaterialCount()).sum();
		if(material > enemyMaterial) {
			text.setText("+"+(material-enemyMaterial));
		}else {
			text.setText("");
		}
		panel.repaint();
	}
}
