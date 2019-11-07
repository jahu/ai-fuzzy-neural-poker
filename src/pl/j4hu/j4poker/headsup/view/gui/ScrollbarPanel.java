package pl.j4hu.j4poker.headsup.view.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class ScrollbarPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3618636042270378331L;
	JScrollBar scroll = null;
	JLabel value = null;
	
	public ScrollbarPanel() {
		setLayout(new GridLayout(2,1));
		scroll = new JScrollBar(JScrollBar.HORIZONTAL);
		scroll.addAdjustmentListener(new SlideAdjustmentListener());
		
		value = new JLabel("Value: 0");
		value.setFont(new Font("sansserif",Font.BOLD,14));
		value.setForeground(Color.white);
		
		setMinimumValue(0);
		setMaximumValue(1000);
		
		add(value);
		add(scroll);
	}
	
	public void setMaximumValue(int max){
		scroll.setMaximum(max+10);
	}
	
	public void setMinimumValue(int min){
		scroll.setMinimum(min);
	}
	
	public int getValue(){
		return scroll.getValue();
	}
	
	
	private void setLabelValue(int value){
		this.value.setText("Value: "+value);
	}
	
	class SlideAdjustmentListener implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			setLabelValue(e.getValue());
		}
		
	}
}
