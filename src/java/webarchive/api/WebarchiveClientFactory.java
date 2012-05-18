
package webarchive.api;

import java.net.InetAddress;

/**
 *
 * @author ccwelich
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
