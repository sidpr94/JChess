package chess.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import chess.Alliance;
import chess.Player;
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
		setLayout(new GridBagLayout());
		this.setTitle("Choose Game Setup");
		
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel whiteLabel = new JLabel("Choose White Player");
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(whiteLabel,gbc);
		
		JRadioButton whiteHuman = new JRadioButton("Human");
		whiteHuman.setSelected(true);
		JRadioButton whiteAI = new JRadioButton("Computer");
		ButtonGroup whiteButtons = new ButtonGroup();
		whiteButtons.add(whiteHuman);
		whiteButtons.add(whiteAI);
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(whiteHuman,gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		add(whiteAI,gbc);
		
		JLabel blackLabel = new JLabel("Choose Black Player");
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(blackLabel,gbc);
		
		JRadioButton blackHuman = new JRadioButton("Human");
		blackHuman.setSelected(true);
		JRadioButton blackAI = new JRadioButton("Computer");
		ButtonGroup blackButtons = new ButtonGroup();
		blackButtons.add(blackHuman);
		blackButtons.add(blackAI);
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(blackHuman,gbc);
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(blackAI,gbc);
		
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
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(okButton,gbc);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GameSetup.this.dispose();
				gameWindow.dispose();
			}
			
		});
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(cancelButton,gbc);
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
	
	public boolean isPlayerAI(Player player) {
		if(player.getColor() == Alliance.WHITE) {
			return getWhitePlayerType() == PlayerType.COMPUTER;
		}
		return getBlackPlayerType() == PlayerType.COMPUTER;
	}

}
