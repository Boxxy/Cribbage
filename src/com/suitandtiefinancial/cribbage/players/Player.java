package com.suitandtiefinancial.cribbage.players;

import java.util.List;

import com.suitandtiefinancial.cribbage.game.Card;

public interface Player {
	public void newGame(boolean goingFirst);
	public void notifyDrawnCard(Card c);
	public void notifyTurnUp(Card c);
	public void opponentPegged(Card c);
	public void opponentScored(int point);
	public void youScored(int point);
	
	public Card getPeg();
	public List<Card> getDiscard();
	
}
