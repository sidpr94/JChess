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
	private final JButton firstMove;
	private final JButton previousMove;
	private final JButton nextMove;
	private final JButton lastMove;
	private ChessPanels chessPanels;
	private Board currentBoard;
	private boolean isLast;
	private int boardIndex;
	private final List<Board> boards = new ArrayList<Board>();

	public MoveLog(ChessPanels chessPanels) {

		this.chessPanels = chessPanels;

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
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
		buttonPanel.setPreferredSize(new Dimension(80*4,40));
		buttonPanel.setBackground(new Color(48,46,43));

		firstMove = new customButton("First");
		firstMove.setEnabled(false);
		buttonPanel.add(firstMove);

		previousMove = new customButton("Previous");
		previousMove.setEnabled(false);
		buttonPanel.add(previousMove);

		nextMove = new customButton("Next");
		nextMove.setEnabled(false);
		buttonPanel.add(nextMove);

		lastMove = new customButton("Last");
		lastMove.setEnabled(false);
		buttonPanel.add(lastMove);

		pane.add(scrollPane,BorderLayout.CENTER);
		pane.add(buttonPanel,BorderLayout.SOUTH);
		pane.setBorder(BorderFactory.createMatteBorder(40, 10, 40, 10, new Color(22,21,18)));

		isLast = true;
	};

	public JPanel getPane() {
		return this.pane;
	}

	public void addMove(Move move) {
		// TODO Auto-generated method stub
		boards.add(move.getBoard());
		currentBoard = move.execute();
		boardIndex = boards.size()-1;
		String moveNotation = BoardUtil.getAlgebraicNotation(move);
		if(move.getMovePiece().getPieceColor() == chess.Alliance.WHITE) {
			((DefaultTableModel) table.getModel()).addRow(new Object[] {table.getModel().getRowCount()+1,moveNotation,""});
			table.changeSelection(table.getModel().getRowCount()-1, 1, false, false);
		}else {
			table.getModel().setValueAt(moveNotation, table.getModel().getRowCount()-1, 2);
			table.changeSelection(table.getModel().getRowCount()-1, 2, false, false);
		}
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				firstMove.setEnabled(true);
				previousMove.setEnabled(true);
				JScrollBar bar = scrollPane.getVerticalScrollBar();
				bar.setValue(bar.getMaximum());
				scrollPane.revalidate();
			}

		});
	}

	public void clearAllMoves() {
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
						isLast = false;
						if(boardIndex == 0){
							chessPanels.updateBoard(boards.get(boardIndex));
							firstMove.setEnabled(false);
							previousMove. setEnabled(false);
						}else {
							chessPanels.updateBoard(boards.get(boardIndex));
							boardIndex = boardIndex - 1;
						}
						nextMove.setEnabled(true);
						lastMove.setEnabled(true);
					}else if(arrow == "Next") {
						if(boardIndex == boards.size()-1) {
							chessPanels.updateBoard(currentBoard);
							nextMove.setEnabled(false);
							lastMove.setEnabled(false);
							isLast = true;
						}else {
							chessPanels.updateBoard(boards.get(boardIndex+1));
							boardIndex = boardIndex + 1;
						}
						firstMove.setEnabled(true);
						previousMove.setEnabled(true);
					}else if(arrow == "First") {
						isLast = false;
						chessPanels.updateBoard(boards.get(0));
						boardIndex = 0;
						firstMove.setEnabled(false);
						previousMove.setEnabled(false);
						nextMove.setEnabled(true);
						lastMove.setEnabled(true);
					}else if(arrow == "Last") {
						chessPanels.updateBoard(currentBoard);
						boardIndex = boards.size()-1;
						isLast = true;
						nextMove.setEnabled(false);
						lastMove.setEnabled(false);
						firstMove.setEnabled(true);
						previousMove.setEnabled(true);
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

	public boolean isLast() {
		return isLast;
	}

}
