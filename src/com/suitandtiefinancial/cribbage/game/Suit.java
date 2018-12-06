package com.suitandtiefinancial.cribbage.game;

public enum Suit {
	HEARTS("H"), CLUBS("C"), SPADES("S"), DIAMONDS("D");
	private final String shortHand;
	private Suit(String shortHand) {
		this.shortHand = shortHand;
	}
	public  String getShortHand() {
		// TODO Auto-generated method stub
		return shortHand;
	}
}
