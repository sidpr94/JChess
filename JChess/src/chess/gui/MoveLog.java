package chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import chess.move.Move;
import chess.board.Board;
import chess.board.BoardUtil;

public class MoveLog {

	private final JPanel pane;
	private final JScrollPane scrollPane;
	private final JTable table;
	private final List<Board> boardStates = new ArrayList<Board>();

	public MoveLog() {
		table = new JTable(new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		((DefaultTableModel) table.getModel()).setColumnIdentifiers(new Object[] {"Row number","White Moves","Black Moves"});
		pane = new JPanel(new BorderLayout());

		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(80*4,(80*6)-40));
		scrollPane.getViewport().setBackground(new Color(38,36,33));
		scrollPane.setHorizontalScrollBar(null);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		table.setBackground(new Color(38,36,33));
		table.setForeground(new Color(222,227, 230));
		table.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		table.setTableHeader(null);
		table.setShowGrid(false);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setBackground(new Color(48,46,43));
		renderer.setForeground(new Color(140, 162, 173));
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumn("Row number").setCellRenderer(renderer);
		table.getColumn("Row number").setMaxWidth(60);
		table.setRowHeight(30);
		table.setIntercellSpacing(new Dimension(5,0));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		buttonPanel.setPreferredSize(new Dimension(80*4,40));
		buttonPanel.setBackground(new Color(48,46,43));

		JButton firstMove = new customButton("First");
		buttonPanel.add(firstMove);

		JButton previousMove = new customButton("Previous");
		buttonPanel.add(previousMove);

		JButton nextMove = new customButton("Next");
		buttonPanel.add(nextMove);

		JButton lastMove = new customButton("Last");
		buttonPanel.add(lastMove);
		
		pane.add(scrollPane,BorderLayout.CENTER);
		pane.add(buttonPanel,BorderLayout.SOUTH);
		pane.setBorder(BorderFactory.createMatteBorder(40, 10, 40, 10, new Color(22,21,18)));
	};

	public JPanel getPane() {
		return this.pane;
	}

	public void addMove(Move move,Board moveBoard) {
		// TODO Auto-generated method stub
		boardStates.add(moveBoard);
		String moveNotation = BoardUtil.getAlgebraicNotation(move);
		if(move.getMovePiece().getPieceColor() == chess.Alliance.WHITE) {
			((DefaultTableModel) table.getModel()).addRow(new Object[] {table.getModel().getRowCount()+1,moveNotation,""});
		}else {
			table.getModel().setValueAt(moveNotation, table.getModel().getRowCount()-1, 2);
		}
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				JScrollBar bar = scrollPane.getVerticalScrollBar();
				bar.setValue(bar.getMaximum());
				scrollPane.revalidate();
			}

		});
	}

	public void clearAllMoves() {
		boardStates.clear();
		DefaultTableModel dtm = (DefaultTableModel) table.getModel();
		dtm.setRowCount(0);
		pane.repaint();
	}

	private class customButton extends JButton{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String arrow;
		customButton(String arrow){
			super();
			this.arrow = arrow;
			this.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if(arrow == "Previous") {
						
					}else if(arrow == "Next") {
						
					}else if(arrow == "First") {
						
					}else if(arrow == "Last") {
						
					}
				}
				
			});
		}

		@Override
		public Icon getIcon() {
			BufferedImage thumbImage = null;
			BufferedImage image = null;
			try {
				image = ImageIO.read(new File("images/"+arrow+".png"));
				thumbImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = thumbImage.createGraphics();
				g2d.drawImage(image.getScaledInstance(30, 30, Image.SCALE_SMOOTH), 0, 0, 30, 30, null);
				g2d.dispose();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageIcon icon = new ImageIcon();
			icon.setImage(thumbImage);
			return icon;
		}
		
		@Override
		public boolean isBorderPainted() {
			return false;
		}
		
		@Override
		public boolean isContentAreaFilled() {
			return false;
		}
		
	}

}
