package com.jpm.tradereporting.service.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jpm.tradereporting.data.Message;
import com.jpm.tradereporting.service.FxMessageHandler;
import com.jpm.tradereporting.service.MessageHandler;
import com.jpm.tradereporting.service.TradingEngine;

/**
 * Entry point to applicaiton
 * 
 * @author prashanthgrao
 *
 */
public class TradingEngineImpl implements TradingEngine {
	private List<Message> processedMessages;
	private Map<String, MessageHandler> messageTypeToHandler;

	public TradingEngineImpl() {
		processedMessages = new ArrayList<>();
		messageTypeToHandler = new HashMap<>();
	}

	public void process(List<Message> messages) {
		// For now hard coding....factory pattern good enuf
		MessageHandler defaultHandler = new FxMessageHandler();
		messages.stream().forEach(msg -> {
			MessageHandler handler = messageTypeToHandler.get(msg.getEntity()) != null
					? messageTypeToHandler.get(msg.getEntity())
					: defaultHandler;
			handler.process(msg);
			processedMessages.add(msg);
		});
	}

	public void setHandlers(Map<String, MessageHandler> messageTypeToHandler) {
		this.messageTypeToHandler = messageTypeToHandler;
	}

	@Override
	public void report(ReportPublisher publisher) {
		// ideally plugin this with some dependency IOC. Hard coding for now!
		ReportBuilder reportBuiler = new RankReportBuilderDecorator();
		List<Object> publishMsgs = reportBuiler.constructReport(processedMessages);
		publisher.publish(publishMsgs);
	}
}
