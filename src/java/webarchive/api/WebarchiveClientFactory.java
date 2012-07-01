
package webarchive.api;

import java.io.IOException;
import java.net.InetAddress;

import webarchive.client.Client;
/**
 * Factory class to create WebarchiveClient-instances.
 * @author eschneider
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
	public WebarchiveClient getInstance() throws IOException {
		return new webarchive.client.WebarchiveClient(server,port);
	}
	public void disconnect(WebarchiveClient cl) {
		((webarchive.client.WebarchiveClient)cl).getClient().disconnect();
	}
	public InetAddress getHostAddress() {
		return server;
	}
	public void setHostAddress(InetAddress server) {
		this.server = server;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
