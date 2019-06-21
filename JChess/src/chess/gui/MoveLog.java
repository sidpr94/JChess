package chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import chess.move.Move;
import chess.board.BoardUtil;

public class MoveLog {
	
	private final JPanel pane;
	private final JScrollPane scrollPane;
	private final JTable table;
	
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
		scrollPane.setPreferredSize(new Dimension(80*4,(80*6)));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(new Color(38,36,33));
		scrollPane.setHorizontalScrollBar(null);
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
		pane.add(scrollPane,BorderLayout.CENTER);
	};
	
	public JPanel getPane() {
		return this.pane;
	}

	public void addMove(Move move) {
		// TODO Auto-generated method stub
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
	
}
