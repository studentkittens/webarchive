package webarchive.server;

import webarchive.transfer.Message;

public interface MessageProcessor {

	void process(Message msg, ServerConnectionHandler cH);
	
}
