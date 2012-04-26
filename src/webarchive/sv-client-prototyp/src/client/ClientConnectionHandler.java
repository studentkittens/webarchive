package client;

import headers.classes.HandShakeHeader;
import connection.Connection;
import connection.ConnectionHandler;
import transfer.Transferable;

public class ClientConnectionHandler extends ConnectionHandler {

	public ClientConnectionHandler(Connection c) {
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
