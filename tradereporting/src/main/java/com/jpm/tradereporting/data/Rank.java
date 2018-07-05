package com.jpm.tradereporting.data;

/**
 * Pojo which has Rank Info
 * 
 * @author prashanthgrao
 *
 */
public class Rank {
	private String entity;
	private double totalAmount;
	private int rank;
	private Side side;

	public Rank(String entity, double totalAmount, int rank, Side side) {
		this.entity = entity;
		this.rank = rank;
		this.totalAmount = totalAmount;
		this.side = side;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public double getTotalAmount() {
		return totalAmount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Entity:");
		builder.append(this.entity).append(",side:").append(side.getSide()).append(",totalAmount:").append(totalAmount)
				.append(",Rank:").append(rank);
		return builder.toString();
	}

}
