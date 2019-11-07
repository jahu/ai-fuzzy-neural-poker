package pl.j4hu.ai.fuzzy.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import pl.j4hu.ai.fuzzy.FuzzyException;

public class VariableSizeDialog extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 787859083481578608L;

	private Container mainContainer = null;
	private AddNewVariableFrame mainGui = null; 
	private JTextField fieldMin = null;
	private JTextField fieldMax = null;
	
	public VariableSizeDialog(AddNewVariableFrame mainGui) {
		this.mainGui = mainGui;
		setTitle("Add New Variable");
		setSize(150, 100);
		setResizable(false);
		mainContainer = getContentPane();
		mainContainer.setLayout(new GridLayout(3,1));
		
		fieldMin = new JTextField("Enter min value");
		mainContainer.add(fieldMin);
		
		fieldMax = new JTextField("Enter max value");
		mainContainer.add(fieldMax);
		
		JButton button = new JButton("Create Variable");
		button.addActionListener(this);
		mainContainer.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj instanceof JButton) {
			String source = ((JButton) obj).getText();
			if(source.equals(("Create Variable"))){
				Double xMin = Double.parseDouble(fieldMin.getText());
				Double xMax = Double.parseDouble(fieldMax.getText());
				try {
					mainGui.createVariable(xMin, xMax);
				} catch (FuzzyException e1) {
					e1.printStackTrace();
				}
				this.dispose();
			}
		}
	}
}
