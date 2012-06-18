package webarchive.client;

import java.util.List;

import webarchive.api.model.CommitTag;
import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.connection.NetworkModule;
import webarchive.transfer.Message;

public class ClientConnectionHandler extends ConnectionHandler {


	public ClientConnectionHandler(Connection c, NetworkModule netMod) {
		super(c, netMod);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
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
			WebarchiveClient obs = (WebarchiveClient) ((Client)netMod).getObservable();
			obs.notifyClients((List<CommitTag>)msg.getData());
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
		case GETXMLEDIT:
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
