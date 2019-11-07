package pl.j4hu.ai.fuzzy.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pl.j4hu.ai.fuzzy.FuzzyController;
import pl.j4hu.ai.fuzzy.FuzzyException;
import pl.j4hu.ai.fuzzy.FuzzyRule;
import pl.j4hu.ai.fuzzy.FuzzySet;
import pl.j4hu.ai.fuzzy.FuzzyVariable;

public class AddNewRuleFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1613931173858109911L;
	
	private Container mainContainer = null;
	
	private FuzzyControllerGui mainGui = null; 
	
	private JComboBox varBox = null;
	private JComboBox setBox = null;
	private JTextArea ruleArea = null;
	
	private static final String btLabelAddConstraint = "Add Constraint";
	private static final String btLabelAddConclusion = "Add Conclusion";
	private static final String btLabelAddRule = "Add Rule";
	
	private FuzzyController fuzzyController = null;
	private FuzzyRule fuzzyRule = null;
	
	public AddNewRuleFrame(FuzzyController fuzzyController, FuzzyControllerGui mainGui){
		this.fuzzyController = fuzzyController;
		this.mainGui = mainGui;
		this.fuzzyRule = new FuzzyRule();
		
		setTitle("Add New Rule");
		setSize(300, 300);
		setResizable(false);
		mainContainer = getContentPane();
		mainContainer.setLayout(new GridLayout(6,1));
		
		varBox = new JComboBox();
		varBox.addActionListener(new VariableSelectedListener());
		mainContainer.add(varBox);
		
		setBox = new JComboBox();
		setBox.addActionListener(this);
		mainContainer.add(setBox);
		
		JButton button = new JButton(btLabelAddConstraint);
		button.addActionListener(this);
		mainContainer.add(button);
		
		button = new JButton(btLabelAddConclusion);
		button.addActionListener(this);
		mainContainer.add(button);
		
		ruleArea = new JTextArea();
		ruleArea.setEditable(false);
		mainContainer.add(new JScrollPane(ruleArea));
		
		button = new JButton(btLabelAddRule);
		button.addActionListener(this);
		mainContainer.add(button);
		
		loadVariables();
		reloadSets(varBox.getSelectedItem());
		reloadRuleArea();
	}

	private void reloadSets(Object var){
		setBox.removeAllItems();
		FuzzyVariable variable = null;
//		for(FuzzyVariable temp: fuzzyController.getVariables()){
//			if(temp.getName().equals(var)){
//				variable = temp;
//			}
//		}
		variable = (FuzzyVariable) varBox.getSelectedItem();
		if (variable == null || variable.getSets().size() == 0) {
			//setBox.addItem("NO VALUES");
		} else {
			for (FuzzySet set : variable.getSets()) {
				//setBox.addItem(set.getName());
				setBox.addItem(set);
			}
		}
	}
	
	private void reloadRuleArea(){
		ruleArea.setText("");
		ruleArea.append(fuzzyRule.toString());
	}
	
	private void loadVariables(){
		if (fuzzyController.getVariables().size() == 0) {
		//	varBox.addItem("NO VALUES");
		} else {
			for (FuzzyVariable var : fuzzyController.getVariables()) {
				varBox.addItem(var);
			}
		}
	}
	
	class VariableSelectedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("comboBoxChanged")){
				reloadSets(varBox.getSelectedItem());
			}
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj instanceof JButton) {
			String source = ((JButton) obj).getText();
			if(source.equals(btLabelAddConstraint)){
				try {
					fuzzyRule.addConstraint(((FuzzyVariable) varBox.getSelectedItem()), ((FuzzySet) setBox.getSelectedItem()).getName());
					reloadRuleArea();
				} catch (FuzzyException e1) {
					e1.printStackTrace();
				}
			}
			if(source.equals(btLabelAddConclusion)){
				try {
					fuzzyRule.addConclusion(((FuzzyVariable) varBox.getSelectedItem()), ((FuzzySet) setBox.getSelectedItem()).getName());
					reloadRuleArea();
				} catch (FuzzyException e1) {
					e1.printStackTrace();
				}
			}
			if(source.equals(btLabelAddRule)){
				fuzzyController.addRule(fuzzyRule);
				mainGui.fuzzyRulesReload();
				this.dispose();
			}
		}
	}

}
