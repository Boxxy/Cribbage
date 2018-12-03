package com.suitandtiefinancial.cribbage.game;

public enum Rank {
	ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(
			10);
	private final int value;

	private Rank(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	boolean isAdjacent(Rank r) {
		switch (r) {
		case ACE:
			return                this == TWO;
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

}
