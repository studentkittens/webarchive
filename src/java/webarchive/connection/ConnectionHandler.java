package webarchive.connection;

import java.net.Socket;
import java.util.HashMap;

import webarchive.handler.Handler;
import webarchive.transfer.Message;

/**
 * The abstract class ConnectionHandler should not be instantiated manually.
 * Instead create Instances of ServerConnectionHandler or ClientConnectionHandler.
 * 
 * This class is responsible for handling incoming Messages over the associated Connection c.
 * It also defines abstract methods, which must be implemented by the Server- and ClientConnectionHandler, since they can differ.
 * There is also a temporary Message-Storage for incoming answers. It is used to redirect Messages to calling certain Threads, which called waitForAnswer().
 * 
 * 
 * @author Schneider
 */
public abstract class ConnectionHandler extends Handler {

	protected final Connection c;
	
	private final HashMap<Integer,Message> map = new HashMap<Integer,Message>();

	protected NetworkModule netMod;
	
	/**
	 * Instantiates a new connection handler.
	 * Should NEVER be called manually.
	 * It's used by inheriting classes ONLY.
	 *
	 * @param c Connection
	 * @param netMod NetworkModule
	 */
	protected ConnectionHandler(Connection c, NetworkModule netMod) {
		this.c = c;
		this.netMod = netMod;
	}

	/**
	 * Abstract method to 'handler' incoming Messages.
	 *
	 * @param msg Message
	 */
	public abstract void handle(Message msg);
	
	/**
	 * Convenience Method.
	 * This just calls Connection.send(msg).
	 *
	 * @param msg the Message
	 * @throws Exception @see {@link webarchive.connection.Connection#send(Message)}
	 */
	public abstract void send(Message msg) throws Exception;
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public Connection getConnection() {
		return c;
	}

	/**
	 * Convenience Method.
	 * Gets the socket.
	 *
	 * @return the socket
	 */
	public Socket getSocket() {
		return c.getSocket();
	}

	/**
	 * Gets the map which acts as a Message-RAM.
	 *
	 * @return the map
	 */
	public HashMap<Integer,Message> getMap() {
		return map;
	}
	
	/**
	 * Wait for an answer.
	 * This blocks the calling Thread until an answer is received.
	 * Only call this Method if 
	 * 	i) a Message m has been sent over the socket and
	 *	ii) the Message m was NOT marked as a broadcast or no reply by calling  markAsBroadCast() or setNoAnswer() which are basically the same :) 
	 *
	 * @param m the sent Message
	 * @return the answer-Message
	 */
	public Message waitForAnswer(Message m)
	{
		
		map.put( m.getId(), null );
		Message answer = null;
		do {
		
			answer = map.get(m.getId());
			
			if(answer != null  && answer.getId().equals(m.getId())) {
				return answer;
			}
			
			answer = null;
			
			synchronized(map) {
				try {
					map.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}	
			
		} while (answer == null);
		
		return null;
	}
	
	/**
	 * Removes the connection from the 'ConnectedClients'-List in the NetworkModule.
	 * Only relevant to the ServerSide.
	 *
	 * @param c the Connection to be removed
	 */
	protected void removeConnection(Connection c) {
		netMod.removeConnection(c);
	}
	
	/**
	 * Wake up the Thread waiting for the Message msg.
	 * This is likely to be changed in the future due to performance issues
	 *
	 * @param msg the Message that just arrived over the socket
	 */
	protected void wakeUp(Message msg)
	{
		if(msg.getId()!=null)
		{
			synchronized (getMap())
			{
				getMap().put(msg.getId(), msg);
				getMap().notifyAll();
			}
		}
	}
	
	
}
