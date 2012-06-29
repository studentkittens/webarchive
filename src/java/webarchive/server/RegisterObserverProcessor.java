package webarchive.server;

import java.util.List;

import webarchive.connection.Connection;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

public class RegisterObserverProcessor implements MessageProcessor {

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		List<Connection> l = Server.getInstance().getObservers();
		synchronized (l) {
			l.remove(cH.getConnection());
			l.add(cH.getConnection());
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

}
