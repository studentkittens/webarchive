package webarchive.server;

import webarchive.connection.Connection;
import webarchive.connection.NetworkModule;
import webarchive.dbaccess.DbConfigHandler;
import webarchive.dbaccess.SqlHandler;
import webarchive.dbaccess.SqliteAccess;
import webarchive.handler.Handlers;
import webarchive.init.ConfigHandler;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable, NetworkModule {

	private static Server sv = null;
	private int listenPort;
	private ServerSocket svSock;
	private final List<Connection> cList;
	private final List<Connection> observers;
	private Boolean running = false;
	private Thread thread;

	public Thread getThread() {
		return thread;
	}

	private Server() {

		this.listenPort = new Integer(((SvConfigHandler) Handlers.get(
			SvConfigHandler.class)).getValue("port"));
		this.cList = new ArrayList<>();
		this.observers = new ArrayList<>();

	}

	public static Server getInstance() {
		if (sv == null) {
			sv = new Server();
		}
		return sv;

	}

	public boolean start() {
		synchronized (running) {
			if (isRunning()) {
				return false;
			}
			thread = new Thread(sv);
			thread.start();
		}
		return isRunning();
	}

	public boolean stop() {
		synchronized (running) {
			if (!isRunning()) {
				return false;
			}
			try {
				System.out.println("closing svSock");
				svSock.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		return true;
	}

	@Override
	public void run() {


		synchronized (running) {
			if (isRunning()) {
				return;
			}
			setRunning(true);
		}
		accept();
		synchronized (running) {
			setRunning(false);
		}
	}

	private void setRunning(boolean running) {
		this.running = running;
	}

	private boolean isRunning() {
		return running;
	}

	private void disconnectClients() {
		System.out.println("disconnecting Clients " + cList.size());
		synchronized (cList) {
			for (Connection c : cList) {
				try {
					c.getSocket().close();
				} catch (IOException ex) {
					Logger.getLogger(Server.class.getName()).
						log(Level.SEVERE, null, ex);
				}
			}
			cList.clear();
		}
		System.out.println("Clients disconnected");
	}

	private void accept() {
		try {
			this.svSock = new ServerSocket(this.listenPort);
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println("creating svSocket");

		while (true) {
			Socket sock = null;

			try {
				System.out.println("awaiting incomming connection");
				sock = svSock.accept();
			} catch (SocketException e) {
				//TODO
				disconnectClients();
				break;

			} catch (IOException ex) {
				// TODO Auto-generated catch block
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null,
					ex);
				continue;
			}

			ObjectInputStream ois = null;
			ObjectOutputStream oos = null;
			System.out.println("trying to get streams");
			try {
				oos = new ObjectOutputStream(sock.getOutputStream());
				ois = new ObjectInputStream(sock.getInputStream());
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null,
					ex);
			}
			System.out.println("trying to safe connection");
			Connection c = new Connection(sock, oos, ois);
			c.setConHandler(new ServerConnectionHandler(c, this));

			new Thread(c).start();

			if (doHandShake(c)) {
				addNewConnection(c);
				System.out.println("HANDSHAKE SUCCESS");
			} else {
				System.out.println("HANDSHAKE FAILED");
				try {
					sock.close();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE,
						null, ex);
				}
				continue;
			}



		}
	}

	private void addNewConnection(Connection c) {
		synchronized (cList) {
			cList.add(c);
		}
	}

	private boolean doHandShake(Connection c) {
		Message h = null;
		try {
			System.out.println("try sending handshake");

			Message m = new Message(Header.HANDSHAKE);

			c.send(m);
			System.out.println("handshake sent, try receiving handshake");

			h = c.waitForAnswer(m);//Go to sleep

			System.out.println("handshake received");

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}

		if ((h != null) && (h.getHeader() == Header.HANDSHAKE)) {
			return true;
		}

		return false;
	}

	@Override
	public void removeConnection(Connection c) {
		synchronized (cList) {
			cList.remove(c);
		}
		synchronized (observers) {
			observers.remove(c);
		}
	}

	public List<Connection> getObservers() {
		return observers;
	}
}
