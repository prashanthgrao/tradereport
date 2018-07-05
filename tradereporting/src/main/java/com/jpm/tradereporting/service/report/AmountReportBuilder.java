
package com.jpm.tradereporting.service.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jpm.tradereporting.data.Message;
import com.jpm.tradereporting.data.Side;

/**
 * Displays core report related to Amount
 * 
 * @author prashanthgrao
 *
 */
public class AmountReportBuilder implements ReportBuilder {

	private Map<Side, List<Message>> processedMessageBySide;


	@Override
	public List<Object> constructReport(List<Message> processedMessages) {
		// If Multithreaded these things can be used on parallel Stream
		this.processedMessageBySide = processedMessages.stream()
				.collect(Collectors.groupingBy(Message::getSide));
		List<Object> messagesToPublish = getPublisedMessage(Side.BUY, processedMessageBySide.get(Side.BUY));
		messagesToPublish.addAll(getPublisedMessage(Side.SELL, processedMessageBySide.get(Side.SELL)));
		return messagesToPublish;
	}

	private List<Object> getPublisedMessage(Side side, List<Message> processedMsgs) {
		Map<Date, List<Message>> messageProcessedPerDay = processedMsgs.stream()
				.collect(Collectors.groupingBy(Message::getInstructionDate));
		List<Object> messagesToPublish = new ArrayList<>();
		messageProcessedPerDay.entrySet().stream().forEach(entry -> {
			Date date = entry.getKey();
			Double totalTradedAmountPerDay = entry.getValue().stream()
					.collect(Collectors.summingDouble(Message::getTradedAmount));
			messagesToPublish.add("Total " + getMessageBasedOnSide(side) + " Amount " + totalTradedAmountPerDay.doubleValue()
					+ " in USD settled for Date" + date);
		});
		return messagesToPublish;
	}

	public String getMessageBasedOnSide(Side side) {
		String str = Side.BUY == side ? "Outgoing" : "Incoming";
		return str;
	}

	protected Map<Side, List<Message>> getProcessedMessageBySide() {
		return processedMessageBySide;
	}
	
	
	
}
