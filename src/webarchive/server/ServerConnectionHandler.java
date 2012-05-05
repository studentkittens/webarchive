package webarchive.server;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.transfer.Transferable;

public class ServerConnectionHandler extends ConnectionHandler {

	public ServerConnectionHandler(Connection c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(Transferable t) {

		switch (t.getHeader()) {
			case HANDSHAKE: 
			{
				// THEY DO NOTHIN
			}
				break;
			default:
				break;
		}
	}
}
