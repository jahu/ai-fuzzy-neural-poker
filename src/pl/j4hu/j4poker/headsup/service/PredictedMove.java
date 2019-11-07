package pl.j4hu.j4poker.headsup.service;

public enum PredictedMove {
	Fold, Check, Call, RaiseSmall, RaiseBig, RaiseAllIn;
	
	public static PredictedMove toPredictedMove(int value) {
		switch (value) {
		case 0:
			return PredictedMove.Fold;
		case 1:
			return PredictedMove.Check;
		case 2:
			return PredictedMove.Call;
		case 3:
			return PredictedMove.RaiseSmall;
		case 4:
			return PredictedMove.RaiseBig;
		case 5:
			return PredictedMove.RaiseAllIn;
		}
		return null;
	}
}
