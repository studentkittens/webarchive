package webarchive.server;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.transfer.Message;

public class ServerConnectionHandler extends ConnectionHandler {

	public ServerConnectionHandler(Connection c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(Message msg) {

		switch (msg.getHeader()) {
			case HANDSHAKE: 
			{
				if(msg.getId()!=null)
				{
					wakeUp(msg);
				}
			}
				break;
			case EXCEPTION:
			{
				
			}
				break;
			case SUCCESS:
			{
				
			}
				break;
			case SQL:
			{
				
			}
				break;
			case WRITEFILE:
			{
				
			}
				break;
			case READFILE:
			{
				
			}
				break;
			case XMLEDIT:
			{
				
			}
				break;
			case LS:
			{
				
			}
				break;
			default:
				break;
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Message msg) throws Exception {
		c.send(msg);		
	}
	
	private void wakeUp(Message msg)
	{
		synchronized (getMap())
		{
			getMap().put(msg.getId(), msg);
			getMap().notifyAll();
		}
	}
}
