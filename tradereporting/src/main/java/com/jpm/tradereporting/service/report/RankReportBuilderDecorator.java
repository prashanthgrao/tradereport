package com.jpm.tradereporting.service.report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.jpm.tradereporting.data.Message;
import com.jpm.tradereporting.data.Rank;
import com.jpm.tradereporting.data.Side;

/**
 * Decorate on top of existing decoarator
 * @author prashanthgrao
 *
 */
public class RankReportBuilderDecorator extends AmountReportBuilder {
	
	@Override
	public List<Object> constructReport(List<Message> processedMessages) {
		List<Object> messagesToPublish = super.constructReport(processedMessages);
		Map<Side, List<Message>> processedMessageBySide = getProcessedMessageBySide();
		messagesToPublish.addAll(getPublisedMessages(Side.BUY, processedMessageBySide.get(Side.BUY)));
		messagesToPublish.addAll(getPublisedMessages(Side.SELL, processedMessageBySide.get(Side.SELL)));
		return messagesToPublish;
	}
	
	private List<Object> getPublisedMessages(Side side, List<Message> processedMsgs) {
		List<Rank> ranks = new ArrayList<>();
		processedMsgs.stream().collect(Collectors.groupingBy(Message::getEntity)).entrySet().forEach(entry -> {
			String entity = entry.getKey();
			Double sumPerInstrucment = entry.getValue().stream().collect(Collectors.summingDouble(Message::getTradedAmount));
			ranks.add(new Rank(entity,sumPerInstrucment,0,side));
		});
		
		ranks.sort(Comparator.comparingDouble(Rank::getTotalAmount).reversed());
		AtomicInteger rankNo=new AtomicInteger(0);
		ranks.forEach(rank -> rank.setRank(rankNo.incrementAndGet()));
		List<Object> messagesToPublish = new ArrayList<>();
		messagesToPublish.addAll(ranks);
		return messagesToPublish;
	}
	
}
