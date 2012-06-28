
package webarchive.api;

import java.net.InetAddress;

import webarchive.client.Client;
//TODO implementation by Eddy
//TODO tests
/**
 * Factory class to create WebarchiveClient-instances.
 * Has also methods to set WebarchiveClient-properties.
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
	public WebarchiveClient getInstance() {
		return new webarchive.client.WebarchiveClient(server,port);
	}
}
