package com.jpm.tradereporting.service;

import java.util.Date;

import com.jpm.tradereporting.data.Message;

/**
 * Service to load Holidays per Instrument
 * @author prashanthgrao
 *
 */
public interface HolidayService {
	
	void load();
	
	Date getNextWorkingDay(Message message);
	
}
