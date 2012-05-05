package webarchive.client;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.transfer.Transferable;

public class ClientConnectionHandler extends ConnectionHandler {

	public ClientConnectionHandler(Connection c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(Transferable t) {
		// TODO Auto-generated method stub

		
		switch(t.getHeader())
		{
		case HANDSHAKE:
			{
				try {
					super.getConnection().send(t);
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
