package pl.j4hu.ai.fuzzy;

public interface FuzzySet {
	double getValue(double x);
	void setParameters(double [] p) throws FuzzyException;
	int getNoParameters();
	
	String getName();
	void setName(String name);
	
	String toString();
}
