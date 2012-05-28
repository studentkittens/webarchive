package webarchive.connection;

import java.net.Socket;
import java.util.HashMap;

import webarchive.transfer.Message;

public abstract class ConnectionHandler {

	protected final Connection c;
	private final HashMap<Integer,Message> map = new HashMap<Integer,Message>();

	public ConnectionHandler(Connection c) {
		this.c = c;
	}

	public abstract void handle(Message msg);
	
	public abstract void disconnect();
	public abstract void connect();
	public abstract void send(Message msg) throws Exception;
	
	public Connection getConnection() {
		return c;
	}

	public Socket getSocket() {
		return c.getSocket();
	}

	public HashMap<Integer,Message> getMap() {
		return map;
	}
	
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
	
	
}
