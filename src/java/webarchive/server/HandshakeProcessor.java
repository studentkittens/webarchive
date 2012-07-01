package webarchive.server;

import webarchive.transfer.Message;

public class HandshakeProcessor implements MessageProcessor  {

	private Message msg;
	private ServerConnectionHandler cH;

	public HandshakeProcessor(Message msg, ServerConnectionHandler cH) {
		this.msg=msg;
		this.cH=cH;
	}

	public HandshakeProcessor() {
	}

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		new Thread(new HandshakeProcessor(msg,cH)).start();
	}

	@Override
	public void run() {
		cH.wakeUp(msg);		
	}

}
