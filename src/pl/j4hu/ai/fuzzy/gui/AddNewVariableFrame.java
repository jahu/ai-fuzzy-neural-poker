package pl.j4hu.ai.fuzzy.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import pl.j4hu.ai.fuzzy.FuzzyController;
import pl.j4hu.ai.fuzzy.FuzzyException;
import pl.j4hu.ai.fuzzy.FuzzySet;
import pl.j4hu.ai.fuzzy.FuzzySetClassGamma;
import pl.j4hu.ai.fuzzy.FuzzySetClassL;
import pl.j4hu.ai.fuzzy.FuzzySetClassT;
import pl.j4hu.ai.fuzzy.FuzzyTerm;
import pl.j4hu.ai.fuzzy.FuzzyVariable;

public class AddNewVariableFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4072337749663085959L;

	private FuzzyController fuzzyController = null;
	private Container mainContainer = null;
	private FuzzyControllerGui mainGui = null;
	private FuzzySet fuzzySet = null;
	private FuzzyVariable variable = null;
	private FuzzyVariableVisualization fuzzyVariableVisualization = null;
	
	private static AddNewVariableFrame thisFrame = null;
	
	private JTextField textField = null;
	private JComboBox setBox = null;
	private List<JTextField> paramFields = null;
	private JButton btAddSet = null;
	private JButton btAddVar = null;
	
	private String varName = null;

	private static final String btLabelCreateVariable = "Create Variable";
	private static final String btLabelAddSet = "Add Set";
	private static final String btLabelShowVisualization = "Show Visualization";
	private static final String btLabelAddVariable = "ADD VARIABLE";
	
	public AddNewVariableFrame(FuzzyController fuzzyController, FuzzyControllerGui mainGui){
		this.fuzzyController = fuzzyController;
		this.mainGui = mainGui;
		thisFrame = this;
		
		setTitle("Add New Variable");
		setSize(300, 300);
		setResizable(false);
		mainContainer = getContentPane();
		mainContainer.setLayout(new GridLayout(5,1));
		
		textField = new JTextField("Enter name of the variable..");

		
		mainContainer.add(textField);
		
		JButton button = new JButton(btLabelCreateVariable);
		button.addActionListener(this);
		mainContainer.add(button);
		
		setBox = new JComboBox();
		setUpSetBox();	
		setBox.addActionListener(new SetSelectedListener());
		setBox.setEnabled(false);
		mainContainer.add(setBox);
		
		btAddSet = new JButton(btLabelAddSet);
		btAddSet.addActionListener(this);
		btAddSet.setEnabled(false);
		mainContainer.add(btAddSet);
		
		btAddVar = new JButton(btLabelAddVariable);
		btAddVar.addActionListener(this);
		btAddVar.setEnabled(false);
		mainContainer.add(btAddVar);

	}
	
	private void setUpParams(Object object){
		if(paramFields != null) {
			while(paramFields.size() > 0) {
				mainContainer.remove(paramFields.get(0));
				paramFields.remove(0);
			}
			mainContainer.remove(btAddSet);
			mainContainer.remove(btAddVar);
		} else {
			paramFields = new ArrayList<JTextField>();
		}
		if (object.equals("Fuzzy Set Class L")) {
			fuzzySet = new FuzzySetClassL(textField.getText());
		} else if(object.equals("Fuzzy Set Class T")) {
			fuzzySet = new FuzzySetClassT(textField.getText());
		} else if(object.equals("Fuzzy Set Class Gamma")) {
			fuzzySet = new FuzzySetClassGamma(textField.getText());
		} else if(object.equals("Fuzzy Term")) {
			fuzzySet = new FuzzyTerm(textField.getText());
		}
		for(int i=0; i<fuzzySet.getNoParameters(); i++){
			JTextField tmp = new JTextField("Parameter " + (i+1));
			mainContainer.add(tmp);
			paramFields.add(tmp);
		}
		mainContainer.setLayout(new GridLayout(5 + fuzzySet.getNoParameters(),1));
		mainContainer.add(btAddSet);
		mainContainer.add(btAddVar);
		mainContainer.validate();
		mainContainer.repaint();
	}
	
	private void setUpSetBox(){
		setBox.addItem("Fuzzy Set Class L");
		setBox.addItem("Fuzzy Set Class T");
		setBox.addItem("Fuzzy Set Class Gamma");
		setBox.addItem("Fuzzy Term");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj instanceof JButton) {
			String source = ((JButton) obj).getText();
			if(source.equals(btLabelCreateVariable)){
				varName = textField.getText();
				if(validateVariableName(varName)) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							VariableSizeDialog varSize = new VariableSizeDialog(thisFrame);
							varSize.setLocationRelativeTo(null);
							varSize.setVisible(true);
						}
					});
				} else {
					
				}
			}
			if(source.equals(btLabelAddSet)){
				double [] params = new double [paramFields.size()];
				int idx = 0;
				for(JTextField field: paramFields) {
					params[idx] = Double.parseDouble(field.getText());
					idx++;
				}
				try {
					fuzzySet.setParameters(params);
					variable.addFuzzySet(fuzzySet);
					fuzzyVariableVisualization.reload(variable);
				} catch (FuzzyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(source.equals(btLabelAddVariable)){
				fuzzyController.addVariable(variable);
				mainGui.fuzzyVariablesReload();
				this.dispose();
			}
		}
		
	}
	
	public void createVariable(double minValue, double maxValue) throws FuzzyException {
		variable = new FuzzyVariable(varName);
		variable.setSpace(minValue, maxValue);
		mainContainer.getComponent(1).setEnabled(false);
		enableComponents();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				fuzzyVariableVisualization = new FuzzyVariableVisualization(variable);
				fuzzyVariableVisualization.setLocationRelativeTo(null);
				fuzzyVariableVisualization.setVisible(true);
			}
		});
	}
	
	boolean validateVariableName(String name) {
		//TODO validate if name is ok
		return true;
	}
	
	private void enableComponents(){
		mainContainer.getComponent(2).setEnabled(true);
		mainContainer.getComponent(3).setEnabled(true);
		mainContainer.getComponent(4).setEnabled(true);
	}
	
	class SetSelectedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("comboBoxChanged")){
				setUpParams(setBox.getSelectedItem());
			}
		}
		
	}

}
