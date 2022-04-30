package fr.leblanc.gomoku.model;

public enum GomokuColor
{
	BLACK(0), WHITE(1), RED(2), NONE(-1);

	public int toNumber() {
		return number;
	}

	private int number;

	GomokuColor(int number) {
		this.number = number;
	}
}