package pl.j4hu.ai.ann;

import java.io.Serializable;

public interface ActivationFunction extends Serializable {
	double getValue(double x);
	double getDerivative(double x);
}
