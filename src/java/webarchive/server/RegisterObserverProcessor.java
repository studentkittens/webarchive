package webarchive.server;

import java.util.List;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

public class RegisterObserverProcessor implements MessageProcessor {

	private ConnectionHandler cH;
	private Message msg;

	private RegisterObserverProcessor(ConnectionHandler cH, Message msg) {
		super();
		this.cH = cH;
		this.msg = msg;
	}

	public RegisterObserverProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		
		new Thread(new RegisterObserverProcessor(cH,msg)).start();
	}

	@Override
	public void run() {
//		Server.getInstance().doHandShake(cH.getConnection());
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
