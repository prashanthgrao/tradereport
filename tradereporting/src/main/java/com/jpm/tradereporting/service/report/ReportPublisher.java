package com.jpm.tradereporting.service.report;

import java.util.List;

/**
 * Interface to handle report consutrcted
 * @author prashanthgrao
 *
 */
public interface ReportPublisher {
	void publish(List<Object> msgs);
}
