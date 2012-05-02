package webarchive.server;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.headers.classes.HandShakeHeader;
import webarchive.transfer.Transferable;

public class ServerConnectionHandler extends ConnectionHandler {

	public ServerConnectionHandler(Connection c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(Transferable t) {
		// TODO Auto-generated method stub
		int id = t.getHeader().getId();

		switch (id) {
			case HandShakeHeader.ID: {
				// THEY DO NOTHIN
			}
			break;
			default:
				break;
		}
	}
}
