package chess.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import chess.PlayerType;

public class GameSetup extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	PlayerType whitePlayerType;
	PlayerType blackPlayerType;
	boolean boardFlipped;

	private JFrame gameWindow;
	
	public GameSetup(JFrame window) {
		super(window,true);
		
		this.gameWindow = window;
		setLayout(new GridLayout(10,1));
		
		JLabel label = new JLabel("Choose Game Setup");
		add(label);
		
		JLabel whiteLabel = new JLabel("Choose White Player");
		add(whiteLabel);
		
		JRadioButton whiteHuman = new JRadioButton("Human");
		whiteHuman.setSelected(true);
		JRadioButton whiteAI = new JRadioButton("Computer");
		ButtonGroup whiteButtons = new ButtonGroup();
		whiteButtons.add(whiteHuman);
		whiteButtons.add(whiteAI);
		add(whiteHuman);
		add(whiteAI);
		
		JLabel blackLabel = new JLabel("Choose Black Player");
		add(blackLabel);
		
		JRadioButton blackHuman = new JRadioButton("Human");
		blackHuman.setSelected(true);
		JRadioButton blackAI = new JRadioButton("Computer");
		ButtonGroup blackButtons = new ButtonGroup();
		blackButtons.add(blackHuman);
		blackButtons.add(blackAI);
		add(blackHuman);
		add(blackAI);
		
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				whitePlayerType = whiteAI.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                blackPlayerType = blackAI.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                
                boardFlipped = whiteHuman.isSelected() ? false : blackHuman.isSelected() ? true : false;
                GameSetup.this.setVisible(false);
			}
			
		});
		add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GameSetup.this.dispose();
				gameWindow.dispose();
			}
			
		});
		add(cancelButton);
		setLocationRelativeTo(window);
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				GameSetup.this.dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				gameWindow.dispose();
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		pack();
		setVisible(false);
	}
	
	public PlayerType getWhitePlayerType() {
		return whitePlayerType;
	}
	
	public PlayerType getBlackPlayerType() {
		return blackPlayerType;
	}
	
	public boolean isBoardFlipped() {
		return boardFlipped;
	}

}
