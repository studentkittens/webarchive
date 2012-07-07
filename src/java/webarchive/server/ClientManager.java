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

public class ClientManager implements Runnable {

	private Server sv;
	private Socket sock;
	
	private boolean doHandShake(Connection c) {
		Message h = null;
		try {
//			System.out.println("try sending handshake");

			Message m = new Message(Header.HANDSHAKE,new HandShake(Math.random()));

			c.send(m);
//			System.out.println("handshake sent, try receiving handshake");

			h = c.getConHandler().waitForAnswer(m,c);//Go to sleep

//			System.out.println("handshake received");

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
