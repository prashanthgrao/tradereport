package com.jpm.tradereporting.data;

public enum Side {
	BUY("B"),SELL("S");
	
	private String side;

	private Side(String side) {
		this.side = side;
	}
	
	public String getSide() {
		return side;
	}
}
