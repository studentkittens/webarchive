package webarchive.client;

import java.util.List;

import webarchive.api.model.CommitTag;
import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.connection.NetworkModule;
import webarchive.transfer.HandShake;
import webarchive.transfer.Message;
import webarchive.xml.XmlEditor;

/**
 * The Class ClientConnectionHandler is primarily responsible for handling Handshakes, Notifications and Exceptions.
 * All other Messages are redirected to the querying Threads by using the super-type ConnectionHandler's wakeUp() method;
 * 
 * @author Schneider
 */
public class ClientConnectionHandler extends ConnectionHandler {


	/**
	 * Instantiates a new client connection handler.
	 *
	 * @param c the Connection
	 * @param netMod the Client-NetworkModule
	 */
	public ClientConnectionHandler(Connection c, NetworkModule netMod) {
		super(c, netMod);
	}

	/* (non-Javadoc)
	 * @see webarchive.connection.ConnectionHandler#handle(webarchive.transfer.Message)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handle(Message msg) {
		
		switch(msg.getHeader())
		{
		case HANDSHAKE:
			{
				try {
					super.getConnection().send(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
			break;
		case NOTIFY:
		{
			WebarchiveClient obs = (WebarchiveClient) ((Client)netMod).getObservable();
			obs.notifyClients((List<CommitTag>)msg.getData());
			break;
		}
		case GETXMLEDIT: {
			XmlEditor xmlE = (XmlEditor)msg.getData();
			xmlE.setConnection(this);
		}
		
		default:
			wakeUp(msg);
			break;
		}
	}

	/* (non-Javadoc)
	 * @see webarchive.connection.ConnectionHandler#send(webarchive.transfer.Message)
	 */
	@Override
	public void send(Message msg) throws Exception {
		c.send(msg);		
		
	}

}
