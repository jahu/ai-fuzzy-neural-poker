package pl.j4hu.j4poker.utils;

import java.io.File;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pl.j4hu.ai.fuzzy.FuzzyConstraint;
import pl.j4hu.ai.fuzzy.FuzzyController;
import pl.j4hu.ai.fuzzy.FuzzyException;
import pl.j4hu.ai.fuzzy.FuzzyRule;
import pl.j4hu.ai.fuzzy.FuzzySet;
import pl.j4hu.ai.fuzzy.FuzzySetClassGamma;
import pl.j4hu.ai.fuzzy.FuzzySetClassL;
import pl.j4hu.ai.fuzzy.FuzzySetClassT;
import pl.j4hu.ai.fuzzy.FuzzyTerm;
import pl.j4hu.ai.fuzzy.FuzzyVariable;

public class FuzzyLoader {

	public static FuzzyController getFuzzyControllerFromXML(String pathToXMLFile) throws Exception {
		FuzzyController controller = new FuzzyController();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		File file = new File(pathToXMLFile);
		
		if (file.exists()) {
			Document doc = db.parse(file);
			Element docEle = doc.getDocumentElement();
			
			// Fuzzy Variables
			NodeList variables = docEle.getElementsByTagName("variable");
			for (int i=0; i<variables.getLength(); i++) {
				Node variableNode = variables.item(i);
				NamedNodeMap variableNodeAttributes = variableNode.getAttributes();
				Node enumNode = variableNodeAttributes.getNamedItem("enum");
				String varName = variableNodeAttributes.getNamedItem("name").getNodeValue();
				FuzzyVariable variable = null;
				if (enumNode == null) {
					variable = new FuzzyVariable(varName);
					String spaceString = variableNodeAttributes.getNamedItem("space").getNodeValue();
					double[] space = stringArrayToDoubleArray(spaceString, ";");
					variable.setSpace(space[0], space[1]);
					NodeList sets = variableNode.getChildNodes();
					for (int j=0 ;j<sets.getLength(); j++) {
						Node node = sets.item(j);
						if (node.hasAttributes()) {
							NamedNodeMap setAttributes = node.getAttributes();
							String setName = setAttributes.getNamedItem("name").getNodeValue();
							String setClass = setAttributes.getNamedItem("class").getNodeValue();
							String setValues = setAttributes.getNamedItem("values").getNodeValue();
							double[] values = stringArrayToDoubleArray(setValues, ";");
							FuzzySet fuzzySet = null;
							if (setClass.equals("L")) {
								fuzzySet = new FuzzySetClassL(setName);
							} else if (setClass.equals("T")) {
								fuzzySet = new FuzzySetClassT(setName);
							} else if (setClass.equals("G")) {
								fuzzySet = new FuzzySetClassGamma(setName);
							} else if (setClass.equals("term")) {
								fuzzySet = new FuzzyTerm(setName);
							}
							fuzzySet.setParameters(values);
							variable.addFuzzySet(fuzzySet);
						}
					}
				} else {
					variable = getFuzzyVariableFromEnum(Class.forName(enumNode.getNodeValue()));
					variable.setName(varName);
				}
				controller.addVariable(variable);
			}
			
			// Fuzzy Rules
			NodeList rules = docEle.getElementsByTagName("rule");
			for (int i=0; i<rules.getLength(); i++) {
				FuzzyRule fuzzyRule = new FuzzyRule();
				Node ruleNode = rules.item(i);
				if (ruleNode.hasChildNodes()) {
					NodeList ruleLines = ruleNode.getChildNodes();
					for (int k=0; k<ruleLines.getLength(); k++) {
						Node constraint = ruleLines.item(k);
						if (constraint.hasAttributes()) {
							NamedNodeMap constraintAttributes = constraint.getAttributes();
							String name = constraintAttributes.getNamedItem("name").getNodeValue();
							String value = constraintAttributes.getNamedItem("value").getNodeValue();
							FuzzyVariable fuzzyVariable = controller.getVariableByName(name);
							if (fuzzyVariable != null) {
								FuzzyConstraint fuzzyConstraint = new FuzzyConstraint(fuzzyVariable, value);
								if (constraint.getNodeName().equals("constraint")) {
									fuzzyRule.addConstraint(fuzzyConstraint);
								} else if (constraint.getNodeName().equals("conclusion")) {
									fuzzyRule.addConclusion(fuzzyConstraint);
								}
							} else {
								throw new Exception("Wrong rule defintion in given XML file");
							}
						}
					}
				}
				controller.addRule(fuzzyRule);
			}
			
		}
		
		return controller;
	}
	
	private static double[] stringArrayToDoubleArray(String values, String delimeter) {
		double[] array = null;
		StringTokenizer tokenizer = new StringTokenizer(values);
		String value = tokenizer.nextToken(delimeter);
		array = new double[tokenizer.countTokens() + 1];
		array[0] = Double.parseDouble(value);
		for (int i=1; i<array.length; i++) {
			array[i] = Double.parseDouble(tokenizer.nextToken());
		}
		return array;
	}
	
	public static FuzzyVariable getFuzzyVariableFromEnum(Class<?> enumeration) throws FuzzyException{
		if (enumeration.isEnum()) {
			FuzzyVariable fuzzyVariable = new FuzzyVariable(enumeration.getSimpleName());
			Object[] names = enumeration.getEnumConstants();
			int count = 0;
			for (Object obj: names) {
				FuzzySet set = new FuzzyTerm(obj.toString());
				set.setParameters(new double[] {count});
				fuzzyVariable.addFuzzySet(set);
				count++;
			}
			fuzzyVariable.setSpace(0, count-1);
			return fuzzyVariable;
		} else {
			throw new FuzzyException("Given type: " + enumeration.getSimpleName() + " is not an enumeration");
		}
	}
	
}
