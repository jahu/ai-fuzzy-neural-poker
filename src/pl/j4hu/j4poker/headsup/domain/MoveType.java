package pl.j4hu.j4poker.headsup.domain;

public enum MoveType {
	Fold, Check, Call, Raise;
	
	public static MoveType toMoveType(String move) {
		MoveType moveType = null;
		if (move.equalsIgnoreCase("fold")) {
			moveType = MoveType.Fold;
		} else if (move.equalsIgnoreCase("check")) {
			moveType = MoveType.Check;
		} else if (move.equalsIgnoreCase("call")) {
			moveType = MoveType.Call;
		} else if (move.equalsIgnoreCase("raise")) {
			moveType = MoveType.Raise;
		}
		return moveType;
	}
}
