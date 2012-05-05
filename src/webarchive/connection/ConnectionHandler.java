package webarchive.connection;

import java.net.Socket;
import webarchive.transfer.Message;

public abstract class ConnectionHandler {

	private final Connection c;

	public ConnectionHandler(Connection c) {
		this.c = c;
	}

	public abstract void handle(Message msg);

	public Connection getConnection() {
		return c;
	}

	public Socket getSocket() {
		return c.getSocket();
	}
}
