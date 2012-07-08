
package webarchive.api;

import java.io.IOException;
import java.net.InetAddress;
import webarchive.client.Client;
/**
 * Factory class to create WebarchiveClient-instances.
 * @author Schneider
 */
public class WebarchiveClientFactory {
	private InetAddress server;
	private int port;

	public WebarchiveClientFactory(InetAddress server, int port) {
		this.server = server;
		this.port = port;
	}
	public WebarchiveClientFactory(InetAddress server) {
		this(server,Client.DEFAULT_PORT);
	}
	/**
	 * Creates a new WebarchiveClient with the current settings (inetAddress, port).
	 * @return a new WebarchiveClient
	 * @throws IOException
	 */
	public WebarchiveClient getInstance() throws IOException {
		return new webarchive.client.WebarchiveClient(server,port);
	}
	/**
	 * Disconnects a WebarchiveClient from the Server.
	 * After this method is called, the disconnected Client should not be used anymore. 
	 * Instead create a new Client with this Factory.
	 * 
	 * Registered Observers are lost after this call. So if you want to be notifed, stay connected.
	 * @param cl
	 */
	public void disconnect(WebarchiveClient cl) {
		((webarchive.client.WebarchiveClient)cl).getClient().disconnect();
	}
	public InetAddress getHostAddress() {
		return server;
	}
	/**
	 * Sets the InetAddress of the Server for future Clients
	 * @param server InetAddress
	 */
	public void setHostAddress(InetAddress server) {
		this.server = server;
	}
	public int getPort() {
		return port;
	}
	/**
	 * Sets the Port of the Server for future Clients.
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
}
