package com.suitandtiefinancial.cribbage.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
		if (frequency == 2) {
			return PAIR_POINTS;
		} else if (frequency == 3) {
			return TRIPLE_POINTS;
		} else if (frequency == 4) {
			return QUADRA_POINTS;
		}
		return 0;
	}

}
