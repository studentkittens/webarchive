package connection;

import java.net.Socket;

import transfer.Transferable;

public abstract class ConnectionHandler {

	private final Connection c;
	public ConnectionHandler(Connection c)
	{
		this.c=c;
	}
	public abstract void handle(Transferable t);
		//zum beispiel c.send(new Transferable(some_random_Data));
	
	public Connection getConnection() {
		return c;
	}
	public Socket getSocket()
	{
		return c.getSocket();
	}
	
}
