package com.suitandtiefinancial.cribbage.game;

public class Card implements Comparable<Card>{
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
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if(!(o instanceof Card)) {
			return false;
		}
		Card c = (Card) o;
		
		return c.rank == this.rank && c.suit == this.suit;
	}

	@Override
	public int compareTo(Card c) {
		return this.rank.compareTo(c.rank);
	}
}
