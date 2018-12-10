package com.suitandtiefinancial.cribbage.testbench;

import com.suitandtiefinancial.cribbage.game.Game;
import com.suitandtiefinancial.cribbage.players.Player;

/**
 * This class is designed to any number of games with any number of AIs at once
 * and aggregate results.
 * 
 * @author Boxxy
 *
 */
public class TestBench {
	private final Player player1;
	private final Player player2;
	private final int games;
	private final boolean debugOn;
	boolean player1First = true;
	int winsPlayer1 = 0;
	int winsPlayer2 = 0;

	public TestBench(Player player1, Player player2, int games, boolean debugOn) {
		this.player1 = player1;
		this.player2 = player2;
		this.games = games;
		this.debugOn = debugOn;
	}

	public void run() {
		for (int i = 0; i < games; i++) {
			boolean firstPlayerWon;
			if (player1First) {
				firstPlayerWon = runGame(player1, player2);
			} else {
				firstPlayerWon = runGame(player2, player1);
			}
			
			if(player1First == firstPlayerWon) {
				winsPlayer1++;
			}else {
				winsPlayer2++;
			}
			
			
			
			if(!firstPlayerWon) {
				player1First = !player1First;
			}
		}
		
		System.out.println("Ran " + games + " games. Player 1 has " + winsPlayer1 + " wins and player 2 has " + winsPlayer2 + " wins");
	}

	private boolean runGame(Player firstPlayer, Player secondPlayer) {
		Game game = new Game(firstPlayer, secondPlayer);
		while(!game.doRound()) {
			
		}
		return game.didFirstPlayerWin();
	}

}
