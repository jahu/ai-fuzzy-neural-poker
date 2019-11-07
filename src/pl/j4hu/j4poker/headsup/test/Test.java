package pl.j4hu.j4poker.headsup.test;

import java.io.FileInputStream;
import java.util.Properties;

import pl.j4hu.j4poker.headsup.controller.StandardHeadsUpController;
import pl.j4hu.j4poker.headsup.domain.CpuPlayer;
import pl.j4hu.j4poker.headsup.domain.HumanPlayer;
import pl.j4hu.j4poker.headsup.facade.HeadsUpGame;
import pl.j4hu.j4poker.headsup.facade.StandardHeadsUpGame;
import pl.j4hu.j4poker.headsup.view.ConsoleHeadsUpView;
import pl.j4hu.j4poker.headsup.view.GuiHeadsUpView;
import pl.j4hu.j4poker.headsup.view.HeadsUpView;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		HeadsUpGame game = new StandardHeadsUpGame();
		
		String opponentType = "human";
		int startStack = 1500;
		Properties properties = new Properties();
		properties.load(new FileInputStream("resources/config.properties"));
		opponentType = properties.getProperty("opponent");
		startStack = Integer.parseInt(properties.getProperty("startStack"));
		HeadsUpView headsUpView = null;
		
		String viewType = properties.getProperty("viewMode");
		if (viewType.equalsIgnoreCase("gui")) {
			headsUpView = new GuiHeadsUpView();
		} else if (viewType.equalsIgnoreCase("console")) {
			headsUpView = new ConsoleHeadsUpView();
		} else {
			throw new Exception("Wrong view mode: " + viewType);
		}
		
		if (opponentType.equalsIgnoreCase("human")) {
			game.newGame(startStack, new HumanPlayer("Gracz"), new CpuPlayer("Komputer"));
			game.start(new StandardHeadsUpController(headsUpView));
		} else if (opponentType.equalsIgnoreCase("call")) {
			game.newGame(startStack, new CpuPlayer("KomputerNauczony"), new CpuPlayer("CallPlayer", "resources/pokerFuzzyController_alwaysCall.xml"));
			game.start(new StandardHeadsUpController(headsUpView));
		} else if (opponentType.equalsIgnoreCase("raise")) {
			game.newGame(startStack, new CpuPlayer("KomputerNauczony"), new CpuPlayer("RaisePlayer", "resources/pokerFuzzyController_alwaysRaise.xml"));
			game.start(new StandardHeadsUpController(headsUpView));
		} else if (opponentType.equalsIgnoreCase("learned")) {
			game.newGame(startStack, new CpuPlayer("KomputerNauczony"), new CpuPlayer("KomputerNauczony2"));
			game.start(new StandardHeadsUpController(headsUpView));
		} 
		else {
			throw new Exception("Wrong opponent type specified in properties: " + opponentType);
		}
		
/*		*/
	}

}
