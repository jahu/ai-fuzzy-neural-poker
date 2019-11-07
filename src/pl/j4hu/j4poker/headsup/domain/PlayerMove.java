package pl.j4hu.j4poker.headsup.domain;

public class PlayerMove {

	private Player player;
	private Move move;
	
	public PlayerMove(Player player, Move move) {
		super();
		this.player = player;
		this.move = move;
	}

	public Player getPlayer() {
		return player;
	}

	public Move getMove() {
		return move;
	}

	@Override
	public String toString() {
		return "PlayerMove [player=" + player.getName() + ", move=" + move + "]";
	}
	
}
