package com.suitandtiefinancial.cribbage.game;

import java.util.LinkedList;
import java.util.List;

import com.suitandtiefinancial.cribbage.players.Player;

/**
 * This class holds one single game between two players of cribbage
 * @author Boxxy
 *
 */
public class Game {
	
	Deck deck;
	Player dealer, pone;
	List<Card> dealerHand, poneHand, crib, peg;
	
	public Game(Player firstPlayer, Player secondPlayer) {
		deck = new Deck();
		firstPlayer.newGame(true);
		secondPlayer.newGame(false);
		dealer = firstPlayer;
		pone = secondPlayer;
		dealerHand = new LinkedList<Card>();
		poneHand = new LinkedList<Card>();
		crib = new LinkedList<Card>();
		peg = new LinkedList<Card>();
	}
	
	public boolean doRound(){
		
		for(int card = 0; card < 6; card++) {
			Card dealerCard = deck.draw();
			dealerHand.add(dealerCard);
			dealer.drawCard(dealerCard);
			Card poneCard = deck.draw();
			poneHand.add(poneCard);
			pone.drawCard(poneCard);
		}
		
		getDiscard(dealer, dealerHand);
		getDiscard(pone, poneHand);
		
		
		
		
		
		
		return false;
	}

	private void getDiscard(Player player, List<Card> hand) {
		
	}
}
