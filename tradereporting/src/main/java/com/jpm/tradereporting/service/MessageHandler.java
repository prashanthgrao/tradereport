package com.jpm.tradereporting.service;

import com.jpm.tradereporting.data.Message;

public interface MessageHandler {
	
	public void process(Message message);
}
