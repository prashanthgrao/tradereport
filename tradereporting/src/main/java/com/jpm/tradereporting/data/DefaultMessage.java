package com.jpm.tradereporting.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultMessage implements Message {

	private final String datePattern = "dd MMM yyyy";
	private String entity;
	private Side side;
	private double agreedFxRate;
	private Date instructionDate;
	private Date settlmentDate;
	private Date adjustedSettlementDate;
	private String currency;
	private int units;
	private double pricePerUnit;
	private double tradedAmount;

	
	public DefaultMessage(String entity, Side side, double agreedFxRate, String currency, String instructionDateStr,
			String settlementDateStr, int units, double pricePerUnit) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		this.entity = entity;
		this.side = side;
		this.agreedFxRate = agreedFxRate;
		this.instructionDate = sdf.parse(instructionDateStr);
		this.settlmentDate = sdf.parse(settlementDateStr);
		// By Default its same
		this.adjustedSettlementDate = settlmentDate;
		this.currency = currency;
		this.units = units;
		this.pricePerUnit = pricePerUnit;
	}
	
	@Override
	public String getEntity() {
		return entity;
	}

	@Override
	public Side getSide() {
		return side;
	}

	@Override
	public double getAgreedFxRate() {
		return agreedFxRate;
	}

	@Override
	public String getCurrency() {
		return currency;
	}

	@Override
	public Date getInstructionDate() {
		return instructionDate;
	}

	@Override
	public Date getSettlementDate() {
		return settlmentDate;
	}

	@Override
	public Date getAdjustedSettlementDate() {
		return adjustedSettlementDate;
	}

	public void setAdjustedSettlementDate(Date adjustedSettlementDate) {
		this.adjustedSettlementDate = adjustedSettlementDate;
	}

	@Override
	public int getUnits() {
		return units;
	}

	@Override
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public double getTradedAmount() {
		return tradedAmount;
	}
	
	public void setTradedAmount(double tradedAmount) {
		this.tradedAmount = tradedAmount;
	}

}
