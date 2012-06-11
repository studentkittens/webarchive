package webarchive.client;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.connection.NetworkModule;
import webarchive.transfer.Message;

public class ClientConnectionHandler extends ConnectionHandler {


	public ClientConnectionHandler(Connection c, NetworkModule netMod) {
		super(c, netMod);
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
		case NOTIFY:
		{
			//TODO
			break;
		}
		case EXCEPTION:
		{
			//TODO
			break;
		}
		case SUCCESS:
		case SQL:
		case WRITEFILE:
		case READFILE:
		case XMLEDIT:
		case LS:
		default:
			wakeUp(msg);
			break;
		}
	}

	@Override
	public void send(Message msg) throws Exception {
		c.send(msg);		
		
	}

}
