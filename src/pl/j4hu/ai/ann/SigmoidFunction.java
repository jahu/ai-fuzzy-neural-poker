package pl.j4hu.ai.ann;


public class SigmoidFunction implements ActivationFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7150023695290558497L;
	public double beta;
	
	public SigmoidFunction(){
		this.beta = 1.0;
	}
	
	public SigmoidFunction(double beta){
		this.beta = beta;
	}
	
	@Override
	public double getValue(double x) {
		return 1.0 / (1 + Math.pow(Math.E , (-beta * x)));
	}

	@Override
	public double getDerivative(double x) {
		return beta * (x) * (1.0 - (x));
	}

}
