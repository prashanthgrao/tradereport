package com.jpm.tradereporting.service;

import java.util.List;

import com.jpm.tradereporting.data.Message;
import com.jpm.tradereporting.service.report.ReportPublisher;

/**
 * Entry point to application
 * @author prashanthgrao
 *
 */
public interface TradingEngine {
	/**
	 * Process incoming messages
	 * @param messges
	 */
	public void process(List<Message> messges);

	/**
	 * Report the processed messages using publisher
	 * @param publisher
	 */
	public void report(ReportPublisher publisher);
}
