package webarchive.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import webarchive.connection.Connection;
import webarchive.transfer.HandShake;
import webarchive.transfer.Header;
import webarchive.transfer.Message;
/**
 * Separate Thread, that is started every time a Client tries to connect to the Server.
 * Upon running, it creates Input and Outputstreams and sends a HandShake-Message to the Client.
 * Only if the Client responds with the same message, the connection is accepted and will be added to the connected Clients-List in the Server.
 * Otherwise the Connection will be closed and the Thread terminates.
 * @author Schneider
 *
 */
public class ClientManager implements Runnable {

	private Server sv;
	private Socket sock;
	
	/**
	 * Verifies, that the other side of the Connection behaves like a WebarchiveClient.
	 * @param c The Connection to the Client
	 * @return true if the Client is accpeted
	 */
	private boolean doHandShake(Connection c) {
		Message h = null;
		try {
			Message m = new Message(Header.HANDSHAKE,new HandShake(Math.random()));
			c.send(m);
			h = c.getConHandler().waitForAnswer(m,c);//Go to sleep

		} catch (Exception ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}

		if ((h != null) && (h.getHeader() == Header.HANDSHAKE)) {
			return true;
		}

		return false;
	}

	@Override
	public void run() {
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		System.out.println("\ttrying to get streams");
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
		} catch (Exception ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null,
				ex);
		}
		Connection c = new Connection(sock, oos, ois);
		c.setConHandler(new ServerConnectionHandler(c, sv));

		new Thread(c).start();

		if (doHandShake(c)) {
			sv.addNewConnection(c);
			System.out.println("\thandshake successful");
		} else {
			System.out.println("\thandshake failed");
			try {
				sock.close();
			} catch (IOException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.WARNING,
					null, ex);
			}
		}

	}
	ClientManager(Server sv, Socket sock) {
		super();
		this.sv = sv;
		this.sock = sock;
	}

}
