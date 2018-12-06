package com.suitandtiefinancial.cribbage.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ScoringUtility {

	public static final int PEGGING_LIMIT_POINTS = 1; // 31
	public static final int FIFTEEN_POINTS = 2;
	public static final int PAIR_POINTS = 2;
	public static final int TRIPLE_POINTS = 6;
	public static final int QUADRA_POINTS = 12;

	public static int scorePeg(List<Card> cards) {
		int score = 0;

		int cardTotal = 0;
		for (Card c : cards) {
			cardTotal += c.getValue();
		}
		if (cardTotal == 15) {
			score += FIFTEEN_POINTS;
		}

		if (cardTotal == Game.PEGGING_LIMIT) {
			score += PEGGING_LIMIT_POINTS;
		}

		score += scorePegDoubles(cards);
		score += scorePegRuns(cards);

		return score;
	}

	private static int scorePegRuns(List<Card> cards) {
		int points = 0;
		for (int runLength = 3; runLength <= cards.size(); runLength++) {
			if (pegHasRun(cards, runLength)) {
				points = runLength;
			}
		}
		return points;
	}

	private static boolean pegHasRun(List<Card> cards, int runLength) {
		List<Rank> temp = new LinkedList<Rank>();
		for (int i = 0; i < runLength; i++) {
			temp.add(cards.get(cards.size() - 1 - i).getRank());
		}
		Collections.sort(temp);
		for (int index = 0; index < temp.size() - 1; index++) {
			if (temp.get(index).isAdjacent(temp.get(index + 1))) {

			} else {
				return false;
			}
		}
		return true;
	}

	private static int scorePegDoubles(List<Card> cards) {
		int frequency = 1;
		Rank lastRank = cards.get(cards.size() - 1).getRank();
		for (int cardIndex = cards.size() - 2; cardIndex >= 0; cardIndex--) {
			if (lastRank == cards.get(cardIndex).getRank()) {
				frequency++;
			} else {
				break;
			}
		}
		return frequencyToScore(frequency);
	}

	private static int frequencyToScore(int frequency) {
		if (frequency == 2) {
			return PAIR_POINTS;
		} else if (frequency == 3) {
			return TRIPLE_POINTS;
		} else if (frequency == 4) {
			return QUADRA_POINTS;
		}
		return 0;
	}

	public static int scoreHand(List<Card> hand, Card turnUp, boolean isCrib) {
		int score = 0;
		EnumMap<Rank, Integer> rankMap = new EnumMap<Rank, Integer>(Rank.class);
		for (Rank r : Rank.values()) {
			rankMap.put(r, 0);
		}
		for (Card c : hand) {
			Rank r = c.getRank();
			rankMap.put(r, 1 + rankMap.get(r));
		}
		rankMap.put(turnUp.getRank(), 1 + rankMap.get(turnUp.getRank()));

		score += scoreHandDoubles(rankMap);
		score += scoreHandFifteens(hand, turnUp);
		score += scoreHandRuns(rankMap);
		score += scoreHandFlush(hand, turnUp, isCrib);
		score += scoreNobs(hand, turnUp);

		return score;
	}

	private static int scoreHandDoubles(EnumMap<Rank, Integer> rankMap) {
		int total = 0;
		for (Integer i : rankMap.values()) {
			total += frequencyToScore(i);
		}
		return total;
	}

	private static int scoreHandFifteens(List<Card> hand, Card turnUp) {
		
		int[] values = new int[5];
		for(int i = 0; i < 4; i++) {
			values[i] = hand.get(i).getValue();
		}
		values[4] = turnUp.getValue();
		return scoreHandFifteensInternal(values);
	
	}

	private static int scoreHandFifteensInternal(int[] values) {
		int score = 0;
		int masks[] = {1, 2, 4, 8, 16};

		for(int i = 1 ; i < 32; i++) {
			int total = 0;
			for(int digit = 0; digit < 5; digit++) {
				if((i & masks[digit]) > 0) {
					total += values[digit];
				}
			}
			if(total == 15) {
				score+=2;
			}
		}
		
		return score;
	}

	private static int scoreHandRuns(EnumMap<Rank, Integer> rankMap) {
		int score = 0, run = 0;
		int doubles = 0;
		int triples = 0;
		for (Rank r : Rank.values()) {
			int i = rankMap.get(r);
			if (i > 0) {
				run++;
				if (i == 2) {
					doubles++;
				} else if (i == 3) {
					triples++;
				}
			} else {
				if (run > 2) {
					break;
				}
				run = 0;
				doubles = 0;
				triples = 0;
			}
		}

		if (run > 2) {
			score = run;
		}
		if (triples > 0) {
			score *= (triples * 3);
		}
		if (doubles > 0) {
			score *= (doubles * 2);
		}
		return score;
	}

	private static int scoreHandFlush(List<Card> hand, Card turnUp, boolean isCrib) {
		Suit s = hand.get(0).getSuit();
		for (Card c : hand) {
			if (s != c.getSuit()) {
				return 0;
			}
		}
		if (s == turnUp.getSuit()) {
			return 5;
		} else {
			if (isCrib) {
				return 0;
			} else {
				return 4;
			}
		}
	}

	private static int scoreNobs(List<Card> hand, Card turnUp) {
		for (Card c : hand) {
			if (c.getRank() == Rank.JACK && c.getSuit() == turnUp.getSuit()) {
				return 1;
			}
		}
		return 0;
	}
	
	public static void handScoringTests() {
		handScoringTestInternal( new Card(Suit.HEARTS, Rank.JACK), new Card(Suit.HEARTS, Rank.ACE), new Card(Suit.HEARTS, Rank.NINE), new Card(Suit.HEARTS, Rank.SEVEN), new Card(Suit.DIAMONDS, Rank.FOUR), 6);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.JACK), new Card(Suit.HEARTS, Rank.TEN), new Card(Suit.HEARTS, Rank.FOUR), new Card(Suit.HEARTS, Rank.THREE), new Card(Suit.DIAMONDS, Rank.THREE), 2);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.SEVEN), new Card(Suit.HEARTS, Rank.KING), new Card(Suit.HEARTS, Rank.TWO), new Card(Suit.CLUBS, Rank.JACK), new Card(Suit.CLUBS, Rank.FIVE), 5);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.THREE), new Card(Suit.HEARTS, Rank.FOUR), new Card(Suit.HEARTS, Rank.TWO), new Card(Suit.CLUBS, Rank.JACK), new Card(Suit.CLUBS, Rank.TEN), 8);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.THREE), new Card(Suit.HEARTS, Rank.SIX), new Card(Suit.HEARTS, Rank.THREE), new Card(Suit.HEARTS, Rank.FOUR), new Card(Suit.DIAMONDS, Rank.KING), 2);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.QUEEN), new Card(Suit.HEARTS, Rank.TWO), new Card(Suit.HEARTS, Rank.THREE), new Card(Suit.HEARTS, Rank.TEN), new Card(Suit.DIAMONDS, Rank.ACE), 7);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.FIVE), new Card(Suit.HEARTS, Rank.FIVE), new Card(Suit.CLUBS, Rank.FIVE), new Card(Suit.DIAMONDS, Rank.FIVE), new Card(Suit.DIAMONDS, Rank.JACK), 28);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.FIVE), new Card(Suit.HEARTS, Rank.FIVE), new Card(Suit.CLUBS, Rank.FIVE), new Card(Suit.DIAMONDS, Rank.JACK), new Card(Suit.DIAMONDS, Rank.FIVE), 29);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.SIX), new Card(Suit.HEARTS, Rank.SEVEN), new Card(Suit.CLUBS, Rank.SEVEN), new Card(Suit.DIAMONDS, Rank.EIGHT), new Card(Suit.SPADES, Rank.EIGHT), 24);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.SIX), new Card(Suit.HEARTS, Rank.SEVEN), new Card(Suit.CLUBS, Rank.SEVEN), new Card(Suit.DIAMONDS, Rank.EIGHT), new Card(Suit.SPADES, Rank.TEN), 12);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.SIX), new Card(Suit.HEARTS, Rank.SEVEN), new Card(Suit.CLUBS, Rank.SEVEN), new Card(Suit.DIAMONDS, Rank.SEVEN), new Card(Suit.SPADES, Rank.EIGHT), 21);
		handScoringTestInternal( new Card(Suit.SPADES, Rank.ACE), new Card(Suit.HEARTS, Rank.TWO), new Card(Suit.CLUBS, Rank.THREE), new Card(Suit.DIAMONDS, Rank.FOUR), new Card(Suit.SPADES, Rank.FIVE), 7);
	}

	private static void handScoringTestInternal(Card card1, Card card2, Card card3, Card card4, Card turnUp, int correctScore) {
		List<Card> hand = new ArrayList<Card>();
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);
		hand.add(card4);
		int calculedatedScore = ScoringUtility.scoreHand(hand, turnUp, false);
		if(calculedatedScore != correctScore) {
			System.out.println("ERROR FAILURE expected " + correctScore + " and got " + calculedatedScore);
		}
		System.out.println(hand + " + "  + turnUp + " scored " + correctScore);
		
	}

}
