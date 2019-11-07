package pl.j4hu.ai.fuzzy.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import pl.j4hu.ai.fuzzy.FuzzyController;
import pl.j4hu.ai.fuzzy.FuzzyRule;
import pl.j4hu.ai.fuzzy.FuzzySet;
import pl.j4hu.ai.fuzzy.FuzzyVariable;

public class FuzzyControllerGui extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4905973370316270887L;

	private static final String labelNew = "New";
	private static final String labelOpen = "Open";
	private static final String labelSave = "Save As";
	private static final String labelClose = "Close";
	
	private static final String labelAddVariable = "Add Variable";
	private static final String labelRemoveVariable = "Remove Variable";
	private static final String labelShowVariables = "Visualize Variables";
	
	private static final String labelAddRule = "Add Rule";
	private static final String labelRemoveRule = "Remove rule";
	
	private static FuzzyControllerGui thisFrame = null;
	private static String fileName = "fuzzyController.ser";
	
	private JTextArea varArea = null;
	private JTextArea ruleArea = null;
	
	private JMenuBar menuBar = null;
	
	private FuzzyController fuzzyController = null;
	
	private Container mainContainer = null;
	
	private void createMenuBar(){
		menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		JMenuItem item = new JMenuItem(labelNew);
		item.addActionListener(this);
		menu.add(item);
		
		item = new JMenuItem(labelOpen);
		item.addActionListener(this);
		menu.add(item);
		
		item = new JMenuItem(labelSave);
		item.addActionListener(this);
		item.setEnabled(false);
		menu.add(item);
		
		menu.addSeparator();
		
		item = new JMenuItem(labelClose);
		item.addActionListener(this);
		menu.add(item);
		
		
		menu = new JMenu("Fuzzy Variables");
		menuBar.add(menu);
		
		item = new JMenuItem(labelAddVariable);
		item.addActionListener(this);
		menu.add(item);
		
		item = new JMenuItem(labelRemoveVariable);
		item.addActionListener(this);
		menu.add(item);
		
		item = new JMenuItem(labelShowVariables);
		item.addActionListener(this);
		menu.add(item);
		
		menu.setEnabled(false);
		
		menuBar.add(menu);
		
		menu = new JMenu("Fuzzy Rules");
		menuBar.add(menu);
		
		item = new JMenuItem(labelAddRule);
		item.addActionListener(this);
		menu.add(item);
		
		item = new JMenuItem(labelRemoveRule);
		item.addActionListener(this);
		menu.add(item);
		
		menu.setEnabled(false);
		
		menuBar.add(menu);
		
		this.setJMenuBar(menuBar);	
	}
	
	public FuzzyControllerGui(){
		setTitle("Fuzzy Controller - Jacek Gralak");
		setSize(800, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		createMenuBar();
		
		mainContainer = getContentPane();
		mainContainer.setLayout(new GridLayout(1, 2));
		
		varArea = new JTextArea();
		varArea.setEditable(false);
		mainContainer.add(new JScrollPane(varArea));
		
		ruleArea = new JTextArea();
		ruleArea.setEditable(false);
		mainContainer.add(new JScrollPane(ruleArea));
		thisFrame = this;
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FuzzyControllerGui gui = new FuzzyControllerGui();
				gui.setLocationRelativeTo(null);
				gui.setVisible(true);
			}
		});
	}

	private void createNewFuzzyController(){
		fuzzyController = new FuzzyController();
		fuzzyVariablesReload();
		fuzzyRulesReload();
		enableControlls();
	}
	
	public void fuzzyVariablesReload(){
		varArea.setText("");
		for(FuzzyVariable var: fuzzyController.getVariables()) {
			varArea.append("Variable: "+var.getName()+"\n");
			for(FuzzySet set: var.getSets()){
				varArea.append("\t" + set.getName() + "\n");
			}
			varArea.append("\n");
		}
		varArea.append("------\nTotal: " + fuzzyController.getVariables().size());
	}
	
	public void fuzzyRulesReload(){
		ruleArea.setText("");
		for(FuzzyRule rule: fuzzyController.getKnowledgeBase().getRules()) {
			ruleArea.append(rule.toString()+"\n\n");
		}
		ruleArea.append("------\nTotal: " + fuzzyController.getKnowledgeBase().getRules().size());
	}
	
	private void openFuzzyController(){
		JFileChooser fileChooser = new JFileChooser(){
		    protected JDialog createDialog(Component parent) throws HeadlessException {
		        JDialog dialog = super.createDialog(parent);
		        dialog.setResizable(false);
		        return dialog;
		    }
		};
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				fileName = fileChooser.getSelectedFile().getAbsolutePath();
				fuzzyController = new FuzzyController(fileName);
			} catch (Exception e) {
				System.out.println("FuzzyController class not found in given file");
				e.printStackTrace();
			}
		}
		
		if (fuzzyController != null) {
			enableControlls();
			
			fuzzyVariablesReload();
			fuzzyRulesReload();
		}
	}
	
	private void enableControlls(){
		menuBar.getMenu(0).getMenuComponent(2).setEnabled(true);
		menuBar.getMenu(1).setEnabled(true);
		menuBar.getMenu(2).setEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj instanceof JMenuItem) {
			String source = ((JMenuItem)obj).getText();
			if(source.equals(labelClose)) {
				//TODO check for save
				System.exit(0);
			}
			if(source.equals(labelOpen)) {
				openFuzzyController();
			}
			if(source.equals(labelSave)) {
				fuzzyController.store(fileName);
			}
			if(source.equals(labelNew)) {
				createNewFuzzyController();
			}
			if(source.equals(labelAddRule)) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						AddNewRuleFrame addRule = new AddNewRuleFrame(fuzzyController, thisFrame);
						addRule.setLocationRelativeTo(null);
						addRule.setVisible(true);
					}
				});
			}
			if(source.equals(labelAddVariable)) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						AddNewVariableFrame addVariable = new AddNewVariableFrame(fuzzyController, thisFrame);
						addVariable.setLocationRelativeTo(null);
						addVariable.setVisible(true);
					}
				});
			}
			if(source.equals(labelShowVariables)) {
				for(final FuzzyVariable variable: fuzzyController.getVariables()){
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							FuzzyVariableVisualization fuzzyVariableVisualization = new FuzzyVariableVisualization(variable);
							fuzzyVariableVisualization.setLocationRelativeTo(null);
							fuzzyVariableVisualization.setVisible(true);
						}
					});
				}
			}
		}
	}

}
