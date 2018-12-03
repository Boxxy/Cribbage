package com.suitandtiefinancial.cribbage.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Deck {
	private final List<Card> deck;
	
	Deck(){
		deck = new LinkedList<Card>();
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				deck.add(new Card(suit, rank));
			}
		}
		shuffle();
	}

	void shuffle() {
		Collections.shuffle(deck);
	}
	
	Card draw() {
		return deck.remove(0);
	}

	public int getSize() {
		return deck.size();
	}
}
