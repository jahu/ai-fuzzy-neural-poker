package pl.j4hu.j4poker.headsup.domain;

public class HumanPlayer implements Player {
	
	private String name;
	private Hand hand;
	private int stack;
	
	public HumanPlayer(String name) {
		super();
		this.name = name;
	}

	@Override
	public int compareTo(Player o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Hand getHand() {
		return hand;
	}

	@Override
	public void setHand(Hand hand) {
		this.hand = hand;
	}

	@Override
	public Result getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStack() {
		return stack;
	}

	@Override
	public void setStack(int stack) {
		this.stack = stack;
	}

	@Override
	public int removeCash(int amount) {
		stack -= amount;
		return amount;
	}

	@Override
	public void addCash(int amount) {
		stack += amount;
	}

	@Override
	public String toString() {
		return "HumanPlayer [name=" + name + ", hand=" + hand + ", stack="
				+ stack + "]";
	}

}
