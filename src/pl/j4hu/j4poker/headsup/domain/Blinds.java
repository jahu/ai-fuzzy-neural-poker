package pl.j4hu.j4poker.headsup.domain;

public class Blinds {
	private int bigBlind;
	private int smallBlind;
	
	public int getBigBlind() {
		return bigBlind;
	}
	public int getSmallBlind() {
		return smallBlind;
	}
	
	public Blinds(int smallBlind, int bigBlind){
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
	}
	
	public void increaseBlinds(){
		bigBlind *= 2;
		smallBlind *= 2;
	}
	@Override
	public String toString() {
		return "Blinds [bigBlind=" + bigBlind + ", smallBlind=" + smallBlind
				+ "]";
	}
	
}
