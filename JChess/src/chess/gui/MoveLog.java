package chess.gui;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import chess.move.Move;
import chess.board.BoardUtil;

public class MoveLog {
	
	private final JScrollPane pane;
	private final JTable table;
	
	public MoveLog() {
		table = new JTable(new DefaultTableModel());
		((DefaultTableModel) table.getModel()).setColumnIdentifiers(new Object[] {"White Moves","Black Moves"});
		pane = new JScrollPane(table);
		pane.getViewport().setBackground(new Color(38,36,33));
		table.setBackground(new Color(38,36,33));
		table.setForeground(Color.WHITE);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setTableHeader(null);
	};
	
	public JScrollPane getPane() {
		return this.pane;
	}

	public void addMove(Move move) {
		// TODO Auto-generated method stub
		String moveNotation = BoardUtil.getAlgebraicNotation(move);
		if(move.getMovePiece().getPieceColor() == chess.Color.WHITE) {
			((DefaultTableModel) table.getModel()).addRow(new Object[] {moveNotation,""});
		}else {
			table.getModel().setValueAt(moveNotation, table.getModel().getRowCount()-1, 1);
		}
	}
	
}
