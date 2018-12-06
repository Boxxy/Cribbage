package com.suitandtiefinancial.cribbage.game;

public enum Rank {
	ACE(1, "A"), TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"), SIX(6, "6"), SEVEN(7, "7"), EIGHT(8,
			"8"), NINE(9, "9"), TEN(10, "T"), JACK(10, "J"), QUEEN(10, "Q"), KING(10, "K");
	private final int value;
	private final String shortHand;

	private Rank(int value, String shortHand) {
		this.value = value;
		this.shortHand = shortHand;
	}

	public int getValue() {
		return value;
	}

	boolean isAdjacent(Rank r) {
		switch (r) {
		case ACE:
			return this == TWO;
		case TWO:
			return this == ACE || this == THREE;
		case THREE:
			return this == TWO || this == FOUR;
		case FOUR:
			return this == THREE || this == FIVE;
		case FIVE:
			return this == FOUR || this == SIX;
		case SIX:
			return this == FIVE || this == SEVEN;
		case SEVEN:
			return this == SIX || this == EIGHT;
		case EIGHT:
			return this == SEVEN || this == NINE;
		case NINE:
			return this == EIGHT || this == TEN;
		case TEN:
			return this == NINE || this == JACK;
		case JACK:
			return this == TEN || this == QUEEN;
		case QUEEN:
			return this == JACK || this == KING;
		case KING:
			return this == QUEEN;
		}
		throw new IllegalStateException();
	}

	public String getShortHand() {
		return shortHand;
	}

}
