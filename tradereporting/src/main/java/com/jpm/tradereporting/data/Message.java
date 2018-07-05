package com.jpm.tradereporting.data;

import java.util.Date;

/**
 * All Message should extend this basic class
 * 
 * @author prashanthgrao
 *
 */
public interface Message {

	String getEntity();

	Side getSide();

	double getAgreedFxRate();

	String getCurrency();

	Date getInstructionDate();

	Date getSettlementDate();

	/**
	 * Additional date field which will have any settlement done that is not same
	 * settlementDate originally received...May be we can have custom message and
	 * put this specifically there. For now here
	 * 
	 */
	Date getAdjustedSettlementDate();

	int getUnits();

	double getPricePerUnit();

	double getTradedAmount();

}
