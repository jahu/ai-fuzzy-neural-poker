package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6640462619787958976L;
	
	Container con = null;
	JPanel panelBgImg;
	public JTextArea console = null;
	public ButtonPanel buttonPanel = null;
	public ScrollbarPanel scrollbarPanel = null;
	public TablePanel tablePanel = null;
	public CommunityPanel communityPanel = null;
	public CenterPanel centerPanel = null;
	
	public JTextArea getConsole() {
		return console;
	}

	public ButtonPanel getButtonPanel() {
		return buttonPanel;
	}

	public ScrollbarPanel getScrollbarPanel() {
		return scrollbarPanel;
	}

	public TablePanel getTablePanel() {
		return tablePanel;
	}

	public CommunityPanel getCommunityPanel() {
		return communityPanel;
	}

	public CenterPanel getCenterPanel() {
		return centerPanel;
	}

	@SuppressWarnings("serial")
	public MainFrame() throws Exception {
		setTitle("Heads-Up Poker - Neuro-Fuzzy AI Poker System - Jacek Gralak 2012");
		setSize(800, 600);
		//setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		//this.setLayout(null);
		con = getContentPane();
		con.setLayout(new BorderLayout());
		ImageIcon imh = new ImageIcon("resources/img/table.jpg");
		setSize(imh.getIconWidth(), imh.getIconHeight());

		panelBgImg = new JPanel() {
			public void paintComponent(Graphics g) {
				Image img = new ImageIcon("resources/img/table.jpg").getImage();
				Dimension size = new Dimension(img.getWidth(null),
						img.getHeight(null));
				setPreferredSize(size);
				setMinimumSize(size);
				setMaximumSize(size);
				setSize(size);
				g.drawImage(img, 0, 0, null);
			}
		};
		panelBgImg.setLayout(null);
		con.add(panelBgImg);
		panelBgImg.setBounds(0, 0, imh.getIconWidth(), imh.getIconHeight());
		
		tablePanel = new TablePanel();
		tablePanel.setBounds(130, 70, tablePanel.getWidth(), tablePanel.getHeight());
		panelBgImg.add(tablePanel);
		tablePanel.setOpaque(false);
		
		console = new JTextArea(5, 20);
		console.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(console);
		panelBgImg.add(scrollPane);
		scrollPane.setBounds(5, 550, 650, 100);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
			e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}});
		
		communityPanel = new CommunityPanel();
		communityPanel.setOpaque(false);
		communityPanel.setBounds(335, 10, 360, 100);
		panelBgImg.add(communityPanel);
		
		buttonPanel = new ButtonPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setBounds(680, 580, 350, 60);
		panelBgImg.add(buttonPanel);
		
		scrollbarPanel = new ScrollbarPanel();
		scrollbarPanel.setOpaque(false);
		scrollbarPanel.setBounds(680, 535, 348, 40);
		panelBgImg.add(scrollbarPanel);
		
		centerPanel = new CenterPanel();
		centerPanel.setOpaque(false);
		centerPanel.setBounds(360, 180, 450, 200);
		panelBgImg.add(centerPanel);
	}
	
	public void showDialogBox(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
}
