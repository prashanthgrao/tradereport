package com.jpm.tradereporting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jpm.tradereporting.data.DefaultMessage;
import com.jpm.tradereporting.data.Holidays;
import com.jpm.tradereporting.data.Message;
import com.jpm.tradereporting.data.Side;
import com.jpm.tradereporting.service.DefaultHolidayService;
import com.jpm.tradereporting.service.FxMessageHandler;
import com.jpm.tradereporting.service.MessageHandler;
import com.jpm.tradereporting.service.report.TradingEngineImpl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class TradingEngineTest extends TestCase {
	private TradingEngineImpl tradingEngine;
	private FxMessageHandler handler;
	private final String product1 = "PRODUCT_1";
	private final String product2 = "PRODUCT_2";
	private final String product3 = "PRODUCT_3";
	private final String product4 = "PRODUCT_4";

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public TradingEngineTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TradingEngineTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		tradingEngine = new TradingEngineImpl();
		handler = new FxMessageHandler();
		Map<String,MessageHandler> handlers = new HashMap<>();
		handlers.put(product1, handler);
		handlers.put(product2, handler);
		handlers.put(product3, handler);
		tradingEngine.setHandlers(handlers);
		DefaultHolidayService hService = (DefaultHolidayService) handler.getHolidayService();
		hService.setHolidays(cookupHolidayData());
	}

	private List<Holidays> cookupHolidayData() {
		List<Holidays> holidays = new ArrayList<>();
		holidays.add(new Holidays("SGP", Set.of(new Integer[] { Calendar.FRIDAY, Calendar.SATURDAY })));
		holidays.add(new Holidays("AED", Set.of(new Integer[] { Calendar.FRIDAY, Calendar.SATURDAY })));
		return holidays;
	}

	public void testTradingEngine() throws Exception {
		List<Message> messages1 = createTestMessage1(product1, "05 July 2018", "06 July 2018");
		tradingEngine.process(messages1);
		tradingEngine.report((msgs) -> assertReportAndRank(product1,msgs));
		// Can assert individual number. ATM asserting report generated
		assertProcessedMessage(product1, messages1);
		List<Message> messages2 = createTestMessage1(product2, "06 July 2018", "08 July 2018");
		tradingEngine.process(messages2);
		tradingEngine.report((msgs) -> assertReportAndRank(product2,msgs));
		// Can assert individual number. ATM asserting report generated
		assertProcessedMessage(product2, messages2);
		List<Message> messages3 = createTestMessage1(product3, "07 July 2018", "09 July 2018");
		tradingEngine.process(messages3);
		// Can assert individual number. ATM asserting report generated
		assertProcessedMessage(product3, messages3);
		tradingEngine.report((msgs) -> assertReportAndRank(product3,msgs));
	}
	
	private void assertReportAndRank(String product, List<Object> reportedMessages) {
		if (product.equals(product1)) {
			assertEquals("Total Outgoing Amount 147.0 in USD settled for DateThu Jul 05 00:00:00 BST 2018",reportedMessages.get(0));
			assertEquals("Total Incoming Amount 230.0 in USD settled for DateThu Jul 05 00:00:00 BST 2018",reportedMessages.get(1));
			assertEquals("Entity:PRODUCT_1,side:B,totalAmount:147.0,Rank:1",reportedMessages.get(2).toString());
			assertEquals("Entity:PRODUCT_1,side:S,totalAmount:230.0,Rank:1",reportedMessages.get(3).toString());
		} else if (product.equals(product2)) {
			assertEquals("Total Outgoing Amount 147.0 in USD settled for DateThu Jul 05 00:00:00 BST 2018",reportedMessages.get(0));
			assertEquals("Total Outgoing Amount 588.0 in USD settled for DateFri Jul 06 00:00:00 BST 2018",reportedMessages.get(1));
			assertEquals("Total Incoming Amount 230.0 in USD settled for DateThu Jul 05 00:00:00 BST 2018",reportedMessages.get(2));
			assertEquals("Total Incoming Amount 920.0 in USD settled for DateFri Jul 06 00:00:00 BST 2018",reportedMessages.get(3));
			assertEquals("Entity:PRODUCT_2,side:B,totalAmount:588.0,Rank:1",reportedMessages.get(4).toString());
			assertEquals("Entity:PRODUCT_1,side:B,totalAmount:147.0,Rank:2",reportedMessages.get(5).toString());
			assertEquals("Entity:PRODUCT_2,side:S,totalAmount:920.0,Rank:1",reportedMessages.get(6).toString());
			assertEquals("Entity:PRODUCT_1,side:S,totalAmount:230.0,Rank:2",reportedMessages.get(7).toString());
		} else if (product.equals(product3)) {
			System.out.println(reportedMessages);
			assertEquals("Total Outgoing Amount 147.0 in USD settled for DateThu Jul 05 00:00:00 BST 2018",reportedMessages.get(0));
			assertEquals("Total Outgoing Amount 1323.0 in USD settled for DateSat Jul 07 00:00:00 BST 2018",reportedMessages.get(1));
			assertEquals("Total Outgoing Amount 588.0 in USD settled for DateFri Jul 06 00:00:00 BST 2018",reportedMessages.get(2));
			assertEquals("Total Incoming Amount 230.0 in USD settled for DateThu Jul 05 00:00:00 BST 2018",reportedMessages.get(3));
			assertEquals("Total Incoming Amount 2070.0 in USD settled for DateSat Jul 07 00:00:00 BST 2018",reportedMessages.get(4));
			assertEquals("Total Incoming Amount 920.0 in USD settled for DateFri Jul 06 00:00:00 BST 2018",reportedMessages.get(5));
			
			assertEquals("Entity:PRODUCT_3,side:B,totalAmount:1323.0,Rank:1",reportedMessages.get(6).toString());
			assertEquals("Entity:PRODUCT_2,side:B,totalAmount:588.0,Rank:2",reportedMessages.get(7).toString());
			assertEquals("Entity:PRODUCT_1,side:B,totalAmount:147.0,Rank:3",reportedMessages.get(8).toString());
			assertEquals("Entity:PRODUCT_3,side:S,totalAmount:2070.0,Rank:1",reportedMessages.get(9).toString());
			assertEquals("Entity:PRODUCT_2,side:S,totalAmount:920.0,Rank:2",reportedMessages.get(10).toString());
			assertEquals("Entity:PRODUCT_1,side:S,totalAmount:230.0,Rank:3",reportedMessages.get(11).toString());
		}
	}

	private void assertProcessedMessage(String product, List<Message> messages) {
		SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
		if (product.equals(product1)) {
			assertEquals(messages.get(0).getTradedAmount(), 50.0, 0);
			assertEquals(messages.get(1).getTradedAmount(), 133.0, 0);
			assertEquals(messages.get(2).getTradedAmount(), 22.0, 0);
			assertEquals(messages.get(3).getTradedAmount(), 22, 0);
			assertEquals(messages.get(4).getTradedAmount(), 75, 0);
			assertEquals(messages.get(5).getTradedAmount(), 75, 0);

			// Singapore and AED
			assertEquals("08 Jul 2018", format.format(messages.get(0).getAdjustedSettlementDate()));
			assertEquals("08 Jul 2018", format.format(messages.get(1).getAdjustedSettlementDate()));
			assertEquals("08 Jul 2018", format.format(messages.get(2).getAdjustedSettlementDate()));
			assertEquals("08 Jul 2018", format.format(messages.get(3).getAdjustedSettlementDate()));
			// GBP
			assertEquals("06 Jul 2018", format.format(messages.get(4).getAdjustedSettlementDate()));
			assertEquals("06 Jul 2018", format.format(messages.get(5).getAdjustedSettlementDate()));
		} else if (product.equals(product2)) {
			assertEquals(messages.get(0).getTradedAmount(), 200.0, 0);
			assertEquals(messages.get(1).getTradedAmount(), 532.0, 0);
			assertEquals(messages.get(2).getTradedAmount(), 88.0, 0);
			assertEquals(messages.get(3).getTradedAmount(), 88.0, 0);
			assertEquals(messages.get(4).getTradedAmount(), 300.0, 0);
			assertEquals(messages.get(5).getTradedAmount(), 300.0, 0);

			// Singapore and AED
			assertEquals("08 Jul 2018", format.format(messages.get(0).getAdjustedSettlementDate()));
			assertEquals("08 Jul 2018", format.format(messages.get(1).getAdjustedSettlementDate()));
			assertEquals("08 Jul 2018", format.format(messages.get(2).getAdjustedSettlementDate()));
			assertEquals("08 Jul 2018", format.format(messages.get(3).getAdjustedSettlementDate()));
			// GBP
			assertEquals("09 Jul 2018", format.format(messages.get(4).getAdjustedSettlementDate()));
			assertEquals("09 Jul 2018", format.format(messages.get(5).getAdjustedSettlementDate()));
		} else if (product.equals(product3)) {
			assertEquals(messages.get(0).getTradedAmount(), 450.0, 0);
			assertEquals(messages.get(1).getTradedAmount(), 1197.0, 0);
			assertEquals(messages.get(2).getTradedAmount(), 198.0, 0);
			assertEquals(messages.get(3).getTradedAmount(), 198.0, 0);
			assertEquals(messages.get(4).getTradedAmount(), 675.0, 0);
			assertEquals(messages.get(5).getTradedAmount(), 675.0, 0);

			// Singapore and AED
			assertEquals("09 Jul 2018", format.format(messages.get(0).getAdjustedSettlementDate()));
			assertEquals("09 Jul 2018", format.format(messages.get(1).getAdjustedSettlementDate()));
			assertEquals("09 Jul 2018", format.format(messages.get(2).getAdjustedSettlementDate()));
			assertEquals("09 Jul 2018", format.format(messages.get(3).getAdjustedSettlementDate()));
			// GBP
			assertEquals("09 Jul 2018", format.format(messages.get(4).getAdjustedSettlementDate()));
			assertEquals("09 Jul 2018", format.format(messages.get(5).getAdjustedSettlementDate()));
		}
	}

	private List<Message> createTestMessage1(String product, String instrDate, String settlementDate) throws Exception {
		List<Message> messages = new ArrayList<>();
		int units = product.equals(product1) ? 1 : ((product.equals(product2) ? 2 : 3));
		int pricePerUnit = product.equals(product1) ? 100 : ((product.equals(product2) ? 200 : 300));

		messages.add(0,
				new DefaultMessage(product, Side.BUY, 0.5, "SGP", instrDate, settlementDate, units, pricePerUnit));
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
