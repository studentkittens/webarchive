package webarchive.client;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.transfer.Message;

public class ClientConnectionHandler extends ConnectionHandler {

	public ClientConnectionHandler(Connection c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(Message msg) {
		// TODO Auto-generated method stub


		switch(msg.getHeader())
		{
		case HANDSHAKE:
			{
				try {
					super.getConnection().send(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		default: break;
		}
	}

}
