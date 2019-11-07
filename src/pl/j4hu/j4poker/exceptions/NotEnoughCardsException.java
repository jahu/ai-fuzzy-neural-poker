package pl.j4hu.j4poker.exceptions;

public class NotEnoughCardsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String toString(){
		return "Not enough cards on table to generate results";
	}

}
