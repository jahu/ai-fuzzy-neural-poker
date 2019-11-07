package pl.j4hu.ai.fuzzy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class FuzzyVariable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8045073832863913184L;
	
	private String name;

	private double xMin = 0.0;
	private double xMax = 1.0;
	
	private List<FuzzySet> sets = null;
	
	public FuzzyVariable(String name) {
		this.name = name;
		sets = new ArrayList<FuzzySet>();
	}
	
	public void addFuzzySet(FuzzySet set) {
		sets.add(set);
	}
	
	public List<FuzzySet> getSets(){
		return sets;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setSpace(double xMin, double xMax) throws FuzzyException {
		if (xMin >= xMax) {
			throw new FuzzyException(
					"Wrong space parameter. Min cannot be greater than Max");
		} else {
			this.xMin = xMin;
			this.xMax = xMax;
		}
	}
	
	public double getxMin() {
		return xMin;
	}

	public double getxMax() {
		return xMax;
	}

	public List<String> getPossibleValues(){
		List<String> values = new ArrayList<String>();
		for(FuzzySet set: sets) {
			values.add(set.getName());
		}
		return values;
	}
	
	public BufferedImage saveFuzzyVariableAsChart(String path){
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (FuzzySet set : sets) {
			XYSeries series = new XYSeries(set.getName());
			for (int i = ((Double) xMin).intValue(); i <= xMax; i++) {
				series.add(i, set.getValue(i));
			}
			dataset.addSeries(series);
		}
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Fuzzy Variable: '" + this.name + "'", // Title
				"x", // x-axis Label
				"Membership", // y-axis Label
				dataset, // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);

		if (path != null) {
			try {
				ChartUtilities
						.saveChartAsJPEG(new File(path), chart, 1024, 600);
			} catch (IOException e) {
				System.err.println("Problem occurred creating chart.");
			}
		}
		return chart.createBufferedImage(640, 460);
	}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FuzzyVariable other = (FuzzyVariable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String toString(){
		return this.name;
	}
	
	public FuzzySet getBestMatchedSet(double value) {
		FuzzySet fuzzySet = null;
		boolean hasOnlyTerms = true;
		for (FuzzySet set: getSets()) {
			if (! (set instanceof FuzzyTerm)) {
				hasOnlyTerms = false;
			}
		}
		if (hasOnlyTerms) {
			double minDistance = Double.MAX_VALUE;
			for (FuzzySet set: getSets()) {
				FuzzyTerm term = (FuzzyTerm) set;
				double termValue = term.getTerm();
				double tmpDist = Math.abs(termValue - value);
				if (tmpDist < minDistance) {
					fuzzySet = set;
					minDistance = tmpDist;
				}
			}
		} else {
			double max = 0.0;
			for (FuzzySet set: getSets()) {
				double setValue = set.getValue(value);
				if (setValue > max) {
					max = setValue;
					fuzzySet = set;
				}
			}
		}
		return fuzzySet;
	}
	
	public List<Double> getSetValues(double value) {
		List<Double> values = new ArrayList<Double>();
		for (FuzzySet set: getSets()) {
			values.add(set.getValue(value));
		}
		return values;
	}
}
