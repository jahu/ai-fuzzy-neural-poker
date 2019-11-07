package pl.j4hu.j4poker.headsup.facade;

import pl.j4hu.j4poker.headsup.controller.HeadsUpController;
import pl.j4hu.j4poker.headsup.domain.Player;

public interface HeadsUpGame {

	void newGame(int initialStack, Player humanPlayer, Player cpuPlayer);
	void start(HeadsUpController controller);
	void stop();
	
}
