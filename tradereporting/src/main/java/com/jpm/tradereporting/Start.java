package com.jpm.tradereporting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.jpm.tradereporting.data.DefaultMessage;
import com.jpm.tradereporting.data.Message;
import com.jpm.tradereporting.data.Side;
import com.jpm.tradereporting.service.report.TradingEngineImpl;

public class Start {
	
	public static final String THURSDAY = "30 August 2018";
	public static final String FRIDAY = "31 August 2018";
	public static final String SATURDAY = "01 September 2018";
	public static final String SUNDAY = "02 September 2018";
	public static final String MONDAY = "03 September 2018";
	

	public static void main(String[] args) {
		// Can Inject by spring later
		TradingEngineImpl tradingEngine = new TradingEngineImpl();
		// Write a consumer at start processing
		AtomicInteger randomCounter = new AtomicInteger(1);
		// Process for every 40 seconds
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
			try {
				randomCounter.incrementAndGet();
				System.out.println("Starting..." + randomCounter.get());
				if (randomCounter.get() % 4 == 0) {
					tradingEngine.report((msgs) -> {
						System.out.println(msgs.size());
						System.out.println(msgs);
					});
				} else {
					if (randomCounter.get() % 4 == 1) {
						tradingEngine.process(randomMessages(randomCounter.get() % 4 + "", THURSDAY,
								FRIDAY, randomCounter.get() % 4, randomCounter.get() % 4));
					} else if (randomCounter.get() % 4 == 2) {
						tradingEngine.process(randomMessages(randomCounter.get() % 4 + "", FRIDAY,
								SATURDAY, randomCounter.get() % 4, randomCounter.get() % 4));
					} else {
						tradingEngine.process(randomMessages(randomCounter.get() % 4 + "", SATURDAY,
								SATURDAY, randomCounter.get() % 4, randomCounter.get() % 4));
					}
				}
			} catch (Exception e) {
				// Something went wrong....am alright for now !:)
				e.printStackTrace();
			}

		}, 0, 10, TimeUnit.SECONDS);
	}

	private static List<Message> randomMessages(String product, String instrDate, String settlementDate, int units,
			double pricePerUnit) throws Exception {
		List<Message> messages = new ArrayList<>();
		messages.add(0,
				new DefaultMessage(product, Side.BUY, 0.5, "SAR", instrDate, settlementDate, units, pricePerUnit));
		messages.add(1,
				new DefaultMessage(product, Side.SELL, 1.33, "SGP", instrDate, settlementDate, units, pricePerUnit));
		messages.add(2,
				new DefaultMessage(product, Side.BUY, 0.22, "AED", instrDate, settlementDate, units, pricePerUnit));
		messages.add(3,
				new DefaultMessage(product, Side.SELL, 0.22, "AED", instrDate, settlementDate, units, pricePerUnit));
		messages.add(4,
				new DefaultMessage(product, Side.BUY, 0.75, "GBP", instrDate, settlementDate, units, pricePerUnit));
		messages.add(5,
				new DefaultMessage(product, Side.SELL, 0.75, "GBP", instrDate, settlementDate, units, pricePerUnit));
		return messages;
	}
}
