package webarchive.server;

import java.util.List;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

public class DeleteObserverProcessor implements MessageProcessor {

	private ConnectionHandler cH;
	private Message msg;

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		new Thread(new DeleteObserverProcessor(cH,msg)).start();

	}

	@Override
	public void run() {
		List<Connection> l = Server.getInstance().getObservers();
		synchronized (l) {
			l.remove(cH.getConnection());
		}
		Message answer = new Message(msg, null);
		answer.setHeader(Header.SUCCESS);
		try {
			cH.send(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private DeleteObserverProcessor(ConnectionHandler cH, Message msg) {
		super();
		this.cH = cH;
		this.msg = msg;
	}

	public DeleteObserverProcessor() {
		// TODO Auto-generated constructor stub
	}

}
