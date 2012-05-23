
package webarchive.api;

import java.net.InetAddress;
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
	public WebarchiveClient getInstance() {
		//TODO
		return null;
	}
}
