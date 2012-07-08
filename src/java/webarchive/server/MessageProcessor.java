package webarchive.server;

import webarchive.transfer.Message;
/**
 * All Processors must implement this interface.
 * MessageProcessors are Threads created when new Messages are received by a running Connection-Thread.
 * They mainly call different Handlers and wake up waiting threads.
 * @author Schneider
 *
 */
public interface MessageProcessor extends Runnable {

	void process(Message msg, ServerConnectionHandler cH);
	
}
