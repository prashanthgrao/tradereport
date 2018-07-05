package com.jpm.tradereporting.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final Calendar calendar = Calendar.getInstance();
	
	public static Date getNextDay(Date date) {
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}
	
	
}
