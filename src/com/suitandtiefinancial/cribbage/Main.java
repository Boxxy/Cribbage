package com.suitandtiefinancial.cribbage;

import com.suitandtiefinancial.cribbage.game.ScoringUtility;
import com.suitandtiefinancial.cribbage.players.RandomPlayer;
import com.suitandtiefinancial.cribbage.testbench.TestBench;

public class Main {

	public static void main(String[] args) {
		TestBench tb = new TestBench(new RandomPlayer("Randy"), new RandomPlayer("Dominic"), 1, false);
		tb.run();
		tb = new TestBench(new RandomPlayer(), new RandomPlayer(), 100000, false);
		tb.run();
	}
	
	
	

}
