package fr.leblanc.gomoku.model;

public enum GomokuColor
{
	BLACK(1), WHITE(-1), GREEN(2), NONE(0);

	public int toNumber() {
		return number;
	}

	private int number;

	GomokuColor(int number) {
		this.number = number;
	}
	
	public static GomokuColor toValue(int number) {
		
		if (number == 1) {
			return BLACK;
		}
		if (number == -1) {
			return WHITE;
		}
		if (number == 0) {
			return NONE;
		}
		if (number == 2) {
			return GREEN;
		}
		
		return null;
		
	}
}