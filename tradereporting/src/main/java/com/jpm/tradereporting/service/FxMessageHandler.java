package com.jpm.tradereporting.service;

import com.jpm.tradereporting.data.DefaultMessage;
import com.jpm.tradereporting.data.Message;

/**
 * Default handler for FxSettlement (for this given test)
 * @author prashanthgrao
 *
 */
public class FxMessageHandler implements MessageHandler {

	private DefaultHolidayService holidayService;

	public FxMessageHandler() {
		holidayService = new DefaultHolidayService();
	}
	
	@Override
	public void process(Message message) {
		DefaultMessage defMessage = (DefaultMessage)message;
		// Actual work here
		defMessage.setTradedAmount(message.getPricePerUnit() * message.getUnits() * message.getAgreedFxRate());
		defMessage.setAdjustedSettlementDate(holidayService.getNextWorkingDay(message));
	}
	
	public HolidayService getHolidayService() {
		return holidayService;
	}

}
