package com.jpm.tradereporting.service.report;

import java.util.List;

import com.jpm.tradereporting.data.Message;

/**
 * Interface which builds report
 * @author prashanthgrao
 *
 */
public interface ReportBuilder {
	List<Object> constructReport(List<Message> processedMessages);
}
