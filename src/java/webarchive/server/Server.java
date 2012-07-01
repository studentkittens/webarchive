package webarchive.server;

import webarchive.connection.Connection;
import webarchive.connection.NetworkModule;
import webarchive.handler.Handlers;
import webarchive.init.ConfigHandler;
import webarchive.transfer.HandShake;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

	private Handlers collection;
	
	public Handlers getCollection() {
		return collection;
	}

	public Thread getThread() {
		return thread;
	}

	private Server(Handlers col) {
		this.collection = col;
		this.listenPort = new Integer(((ConfigHandler) collection.get(
			ConfigHandler.class)).getValue("webarchive.server.port"));
		this.cList = new ArrayList<>();
		this.observers = new ArrayList<>();

	}

	public static Server getInstance() {
		return sv;

	}
	
	public static void init(Handlers col) {
		sv = new Server(col);
	}

	public boolean start() {
		if(checkRunning())
			return false;
		thread = new Thread(sv);
		thread.start();
		return checkRunning();
	}

	public boolean stop() {
			if(checkRunning())
				return false;
			try {
				System.out.println("closing svSock");
				svSock.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
		return true;
	}

	@Override
	public void run() {
		if (initRunning())
			return;
		accept();
		finalizeRunning();
	}

	private void setRunning(boolean running) {
		this.running = running;
	}
	private synchronized boolean checkRunning() {
		if (isRunning()) {
			return true;
		}
		return false;
	}
	private synchronized boolean initRunning() {
		if (isRunning()) {
			return true;
		}
		setRunning(true);
		return false;
	}
	private synchronized void finalizeRunning() {
		setRunning(false);
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
				System.out.println("client connected!");
			} catch (SocketException e) {
				Logger.getLogger(Server.class.getName()).log(Level.INFO, null,
						e);
				disconnectClients();
				break;

			} catch (IOException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.INFO, null,
					ex);
				continue;
			}

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
			c.setConHandler(new ServerConnectionHandler(c, this));

			new Thread(c).start();

//			if (doHandShake(c)) {
				addNewConnection(c);
//				System.out.println("HANDSHAKE SUCCESS");
//			} else {
//				System.out.println("HANDSHAKE FAILED");
//				try {
//					sock.close();
//				} catch (IOException ex) {
//					Logger.getLogger(Server.class.getName()).log(Level.WARNING,
//						null, ex);
//				}
//				continue;
//			}



		}
	}

	private void addNewConnection(Connection c) {
		synchronized (cList) {
			cList.add(c);
		}
	}

	boolean doHandShake(Connection c) {
		Message h = null;
		try {
			System.out.println("try sending handshake");

			Message m = new Message(Header.HANDSHAKE,new HandShake(Math.random()));

			c.send(m);
			System.out.println("handshake sent, try receiving handshake");

			h = c.getConHandler().waitForAnswer(m,c);//Go to sleep

			System.out.println("handshake received");

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
