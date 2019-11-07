package pl.j4hu.j4poker.headsup.domain;

public interface Player extends Comparable<Player> {
	
	public String getName();
	public void setName(String name);
	
	public Hand getHand();
	public void setHand(Hand hand);
	
	public Result getResult();
	
	public int getStack();
	public void setStack(int stack);
	
	public int removeCash(int amount);
	public void addCash(int amount);

}
