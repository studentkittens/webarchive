package server;

import headers.classes.HandShakeHeader;
import connection.Connection;
import connection.ConnectionHandler;
import transfer.Transferable;

public class ServerConnectionHandler extends ConnectionHandler {

	public ServerConnectionHandler(Connection c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(Transferable t) {
		// TODO Auto-generated method stub
		int id=t.getHeader().getId();
		
		switch(id)
		{
		case HandShakeHeader.ID:
			{
				// THEY DO NOTHIN
			}
			break;
		default: break;
		}
	}

}
