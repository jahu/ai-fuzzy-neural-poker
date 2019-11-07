package pl.j4hu.j4poker.headsup.domain;

public class Move {
	private MoveType type;
	private int amount;
	
	public Move(MoveType type){
		amount = 0;
		this.type = type;
	}
	
	public Move(MoveType type, int amount){
		this.amount = amount;
		this.type = type;
	}
	
	public MoveType getType() {
		return type;
	}
	public void setType(MoveType type) {
		this.type = type;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String toString(){
		return "Move=" + type + " (" + amount + ")";
	}
}
