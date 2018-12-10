package com.suitandtiefinancial.cribbage.game;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import com.suitandtiefinancial.cribbage.players.Player;

/**
 * This class holds one single game between two players of cribbage
 * 
 * @author Boxxy
 *
 */
public class Game {

	public static final int STARTING_HAND_SIZE = 6;
	public static final int CARDS_DISCARDED = 2;
	public static final int PLAYING_HAND_SIZE = STARTING_HAND_SIZE - CARDS_DISCARDED;
	public static final int PEGGING_LIMIT = 31;
	public static final int POINTS_TO_WIN = 121;

	public static final int HIS_HEELS_POINTS = 2;
	public static final int GO_POINTS = 1;

	Deck deck;
	List<Card> crib, pegCurrent, pegOld;
	int currentPegginTotal;
	Card turnUp;
	EnumMap<Positions, Position> players;

	Positions upToPeg;
	private final Player firstPlayer;

	private enum Positions {
		DEALER, PONE;

		public Positions getOpponent() {
			if (this == DEALER) {
				return PONE;
			} else {
				return DEALER;
			}
		}
	}

	private class Position {
		private final Player player;
		private final List<Card> hand;
		private final List<Card> peggingHand;
		int score;

		private Position(Player player) {
			this.player = player;
			hand = new LinkedList<Card>();
			peggingHand = new LinkedList<Card>();
			score = 0;
		}

		private boolean score(int points) {
			score+=points;
			if (score > POINTS_TO_WIN) {
				return true;
			}
			return false;
		}
	}

	public Game(Player firstPlayer, Player secondPlayer) {
		deck = new Deck();
		firstPlayer.newGame(true);
		secondPlayer.newGame(false);
		players = new EnumMap<Positions, Position>(Positions.class);
		players.put(Positions.DEALER, new Position(firstPlayer));
		players.put(Positions.PONE, new Position(secondPlayer));
		crib = new LinkedList<Card>();
		pegCurrent = new LinkedList<Card>();
		pegOld = new LinkedList<Card>();
		this.firstPlayer = firstPlayer;
	}

	public boolean doRound() {

		makeSureCardsAreCorrect();

		deal();

		for (Position p : players.values()) {
			getDiscard(p);
		}

		if (turnUp()) {
			return true;
		}

		if (peg()) {
			return true;
		}

		if (scorePositions(Positions.PONE)) {
			return true;
		}
		if (scorePositions(Positions.DEALER)) {
			return true;
		}
		if (scoreCrib()) {
			return true;
		}

		cleanup();

		return false;
	}

	private void makeSureCardsAreCorrect() {
		if (deck.getSize() != 52) {
			throw new IllegalStateException();
		}
		if (crib.size() + pegCurrent.size() + pegOld.size() != 0) {
			throw new IllegalStateException();
		}
		for (Position p : players.values()) {
			if (p.hand.size() != 0) {
				throw new IllegalStateException();
			}
		}
	}

	private void deal() {
		for (int card = 0; card < STARTING_HAND_SIZE; card++) {
			for (Position p : players.values()) {
				Card c = deck.draw();
				p.hand.add(c);
				p.peggingHand.add(c);
				p.player.notifyDrawnCard(c);
			}
		}
		for (Position p : players.values()) {
			Collections.sort(p.hand);
		}
	}

	private void getDiscard(Position p) {
		List<Card> discard = p.player.getDiscard();
		if (discard.size() != CARDS_DISCARDED) {
			throw new IllegalStateException("Didn't recieve a 2 card discard from " + p.player);
		}
		for (Card c : discard) {
			if (!p.hand.contains(c)) {
				throw new IllegalStateException(
						"Player " + p.player + " tried to discard a " + c + " when there hand was " + p.hand);
			}
			p.hand.remove(c);
			p.peggingHand.remove(c);
			crib.add(c);
		}
	}

	private boolean turnUp() {
		turnUp = deck.draw();
		for (Position p : players.values()) {
			p.player.notifyTurnUp(turnUp);
		}
		if (turnUp.getRank() == Rank.JACK) {
			if (score(Positions.DEALER, 2)) {
				return true;
			}
		}
		return false;
	}

	private boolean score(Positions p, int points) {
		if(points == 0) {
			return false;
		}
		players.get(p).player.youScored(points);
		players.get(p.getOpponent()).player.opponentScored(points);
		return players.get(p).score(points);
	}

	private boolean peg() {
		upToPeg = Positions.PONE;
		while (pegOld.size() < (PLAYING_HAND_SIZE * players.values().size())) {
			if (doPeggingSequence()) {
				return true;
			}
		}
		return false;
	}

	private boolean doPeggingSequence() {
		Positions calledGo = null;
		while (calledGo == null) {
			if (canPeg(upToPeg)) {
				pegOneCard(upToPeg);
				upToPeg = upToPeg.getOpponent();
			} else {
				calledGo = upToPeg;
				if (score(upToPeg.getOpponent(), 1)) {
					return true;
				}
			}
		}
		while(canPeg(calledGo.getOpponent())) {
			if(pegOneCard(calledGo.getOpponent())) {
				return true;
			}
		}
		
		upToPeg = calledGo; //Whoever called peg is going first next sequence
		pegOld.addAll(pegCurrent);
		pegCurrent.clear();
		currentPegginTotal = 0;
		return false;
	}

	private boolean pegOneCard(Positions p) {
		Card c = getPegCardFromPlayer(p);
		pegCurrent.add(c);
		players.get(p).peggingHand.remove(c);
		players.get(p.getOpponent()).player.opponentPegged(c);
		int points = ScoringUtility.scorePeg(pegCurrent);
		if (score(p, points)) {
			return true;
		}
		return false;
	}

	private boolean canPeg(Positions p) {
		List<Card> hand = players.get(p).peggingHand;
		if(hand.size() == 0) {
			return false;
		}
		return (hand.get(0).getValue() + currentPegginTotal) <= PEGGING_LIMIT;
	}

	private Card getPegCardFromPlayer(Positions p) {
		Player player = players.get(p).player;
		List<Card> hand = players.get(p).peggingHand;
		Card c = player.getPeg();

		if (c == null) {
			throw new IllegalStateException("Player " + player + " pegged a null card");
		}
		if (!hand.contains(c)) {
			throw new IllegalStateException(
					"Player " + player + " tried to peg " + c + " which is not in their hand " + hand);
		}

		currentPegginTotal = c.getValue();
		for (Card card : pegCurrent) {
			currentPegginTotal += card.getValue();
		}
		if (currentPegginTotal > 31) {
			throw new IllegalStateException("Player " + player + " tried to peg " + c + "on to " + pegCurrent
					+ " which makes the peg go over " + PEGGING_LIMIT);
		}
		return c;
	}

	private boolean scorePositions(Positions p) {
		int score = ScoringUtility.scoreHand(players.get(p).hand, turnUp, false);
		return score(p, score);
	}

	private boolean scoreCrib() {
		int score = ScoringUtility.scoreHand(crib, turnUp, false);
		return score(Positions.DEALER, score);
	}

	private void cleanup() {
		// TODO Auto-generated method stub
		pegCurrent.clear();
		pegOld.clear();
		deck.addAll(crib);
		deck.add(turnUp);
		crib.clear();
		for(Position p : players.values()) {
			deck.addAll(p.hand);
			p.hand.clear();
		}
	}
	
	public boolean didFirstPlayerWin() {
		for(Position p : players.values()) {
			if(p.score >= POINTS_TO_WIN) {
				return p.player == firstPlayer;
			}
		}
		throw new IllegalStateException(); 
	}

}
