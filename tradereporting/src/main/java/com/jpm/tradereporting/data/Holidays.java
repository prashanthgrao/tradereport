package com.jpm.tradereporting.data;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic pojo which contains instrument details and the settlement weekend dates
 * 
 * @author prashanthgrao
 *
 */
public class Holidays {
	private String instrument;
	// Java day of week stored
	private Set<Integer> weekend = new HashSet<>();

	public Holidays(String instrument, Set<Integer> weekend) {
		this.instrument = instrument;
		this.weekend = weekend;
	}

	public boolean isWeekendForTheInstrument(Integer dayOfWeek) {
		return weekend.contains(dayOfWeek);
	}

	public String getInstrument() {
		return instrument;
	}

	public static Holidays getDefault(String instr) {
		// log message. ATM putting on sysout, can attach logger later.
		System.out.println("No holiday list found for the instr " + instr
				+ ". Considering default weekday and weekends");
		Set<Integer> defaultWeekend = Set.of(new Integer[] {Calendar.SATURDAY,Calendar.SUNDAY});
		return new Holidays(instr,defaultWeekend);
	}
}
