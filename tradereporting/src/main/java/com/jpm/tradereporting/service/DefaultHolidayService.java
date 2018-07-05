package com.jpm.tradereporting.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jpm.tradereporting.data.Holidays;
import com.jpm.tradereporting.data.Message;
import com.jpm.tradereporting.util.DateUtil;

/**
 * Default implementation
 * @author prashanthgrao
 *
 */
public class DefaultHolidayService implements HolidayService {
	public static final Calendar calendar = Calendar.getInstance();
	
	private Map<String, Holidays> holidaysPerInsrument;

	public DefaultHolidayService() {
		load();
	}
	
	@Override
	public void load() {
		// Load from relevant source
		holidaysPerInsrument = new HashMap<>();
	}
	
	public void setHolidays(List<Holidays> holidaysPerInsrument) {
		this.holidaysPerInsrument = holidaysPerInsrument.stream().collect(Collectors.toMap(Holidays::getInstrument,Function.identity()));
	}
	
	@Override
	public Date getNextWorkingDay(Message message) {
		Holidays holiday = holidaysPerInsrument.get(message.getCurrency()) != null
				? holidaysPerInsrument.get(message.getCurrency())
				: Holidays.getDefault(message.getCurrency());
		Date adjustedDate = message.getSettlementDate();
		calendar.setTime(adjustedDate);
		while (holiday.isWeekendForTheInstrument(calendar.get(Calendar.DAY_OF_WEEK))) {
			// Increment and set
			adjustedDate = DateUtil.getNextDay(adjustedDate);
			calendar.setTime(adjustedDate);
		}
		return adjustedDate;
	}

}
