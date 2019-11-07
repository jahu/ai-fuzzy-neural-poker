package pl.j4hu.ai.fuzzy.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pl.j4hu.ai.fuzzy.FuzzyVariable;

public class FuzzyVariableVisualization extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8729970366415893244L;

	private BufferedImage image = null;
	
	private Container mainContainer = null;
	
	private JPanel panelBgImg = null;
	
	public FuzzyVariableVisualization(FuzzyVariable variable) {
		setTitle("Variable Visualization");
		setSize(640, 490);
		setResizable(false);
		mainContainer = getContentPane();
		mainContainer.setLayout(new BorderLayout());
		
		image = variable.saveFuzzyVariableAsChart(null);
		
		panelBgImg = new JPanel() {
			public void paintComponent(Graphics g) {
				
				Image img = Toolkit.getDefaultToolkit().createImage(image.getSource());
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
		mainContainer.add(panelBgImg);
	}
	
	public void reload(FuzzyVariable variable){
		image = variable.saveFuzzyVariableAsChart(null);
		mainContainer.remove(panelBgImg);
		panelBgImg = new JPanel() {
			public void paintComponent(Graphics g) {
				
				Image img = Toolkit.getDefaultToolkit().createImage(image.getSource());
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
		mainContainer.add(panelBgImg);
		mainContainer.validate();
		mainContainer.repaint();
	}
}
