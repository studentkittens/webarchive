package webarchive.server;

import webarchive.transfer.Message;

public interface MessageProcessor extends Runnable {

	void process(Message msg, ServerConnectionHandler cH);
	
}
