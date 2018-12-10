package com.suitandtiefinancial.cribbage.players;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.suitandtiefinancial.cribbage.game.Card;

public class RandomPlayer implements Player{

	List<Card> hand = new LinkedList<Card>();
	boolean myDeal = false;
	Card turnUp = null;
	int myScore = 0;
	int opponentScore = 0;
	Random r = new Random(System.currentTimeMillis());
	int pegTotal = 0;
	boolean debug = true;
	private final String debugName;
	

	public RandomPlayer (String debugName) {
		this.debugName = debugName;
	}
	public RandomPlayer() {
		this.debugName = null;
	}
	
	@Override
	public void newGame(boolean dealing) {
		myScore = 0;
		opponentScore = 0;
		debug("New Game, going first = " + dealing);
		newRound(dealing);
		
	}
	
	private void debug(String string) {
		if(debugName != null) {
			System.out.println(debugName + ": " + string);
		}
	}
	private void newRound(boolean dealing) {
		myDeal = dealing;
		hand.clear();
		turnUp = null;
		pegTotal = 0;
		debug("New Round, myDeal = " + myDeal);
	}

	@Override
	public void notifyDrawnCard(Card c) {
		if(turnUp != null) {
			//we must be starting a new round if we are drawing again
			newRound(!myDeal);
		}

		debug("Drew " + c);
		hand.add(c);
	}

	@Override
	public void notifyTurnUp(Card c) {
		turnUp = c;

		debug("Flipped " + c);
	}

	@Override
	public void opponentPegged(Card c) {
		addToPegTotal(c.getValue());
		debug("Opponent Pegged " + c + " the peg is at " + pegTotal);
	}

	@Override
	public void opponentScored(int points) {
		opponentScore += points;
		debug("Opponent scored " + points + " they are at " + opponentScore);
		
	}

	@Override
	public void youScored(int points) {
		myScore += points;
		debug("I scored " + points + " I am at " + myScore);
	}

	@Override
	public Card getPeg() {
		
		List<Card> possibleCards = getPossiblePegs(hand, pegTotal);
		Card c = possibleCards.get(r.nextInt(possibleCards.size()));

		addToPegTotal(c.getValue());
		hand.remove(c);

		debug("I pegged " + c + " the peg is at " + pegTotal);
		return c;
	}

	private List<Card> getPossiblePegs(List<Card> cards, int total) {
		if(cards.size() == 0) {
			throw new IllegalArgumentException();
		}
		List<Card> possibleCards = new ArrayList<Card>(4);
		for(Card c : cards) {
			if (total + c.getValue() <= 31) {
				possibleCards.add(c);
			}
		}
		if(possibleCards.size() == 0) {
			if(total == 0) {
				throw new IllegalStateException();
			}
			return getPossiblePegs(cards, 0);
		}
		return possibleCards;
	}

	private void addToPegTotal(int value) {
		pegTotal += value;
		if(pegTotal > 31) {
			pegTotal = value;
		}
	}

	@Override
	public List<Card> getDiscard() {
		List<Card> returnList = new ArrayList<Card>(2);
		int first = r.nextInt(6);
		returnList.add(hand.remove(first));
		int second = r.nextInt(5);
		returnList.add(hand.remove(second));

		debug("I discarded " + returnList);
		return returnList;
	}

}
