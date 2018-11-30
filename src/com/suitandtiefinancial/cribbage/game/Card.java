package com.suitandtiefinancial.cribbage.game;

public class Card {
	private final Rank rank;
	private final Suit suit;
	
	Card(Suit suit, Rank rank){
		this.suit = suit;
		this.rank = rank;
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public int getValue() {
		return rank.getValue();
	}
}
