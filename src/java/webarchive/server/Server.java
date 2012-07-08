package webarchive.server;

import webarchive.connection.Connection;
import webarchive.connection.NetworkModule;
import webarchive.handler.Handlers;
import webarchive.init.ConfigHandler;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Core of the Java Frontend. This represents the NetworkModule on the Webarchive.
 * It is designed as a Thread and accepts Connections from WebarchiveClients.
 * Every time a Client connects a new ClientManager-Thread is started to handshake nad register the Client.
 * The Server can be stopped and started at any Time. Running Notifications or Message-Transfers will be terminated  upon calling stop().
 * The Server is designed as a Singleton. You can get the current running instance by invoking Server.getInstance().
 * 
 * The Server also hold a reference to the HandlerCollection. You can get it by calling Server.getInstance().getCollection().
 * 
 * 
 * @author Schneider
 *
 */
public class Server implements Runnable, NetworkModule {

	private static Server sv = null;
	private int listenPort;
	private ServerSocket svSock;
	private final List<Connection> cList;
	private final List<Connection> observerList;
	private Boolean running = false;
	private Thread thread;

	private Handlers collection;
	/**
	 * @return Handler Collection with Handlers like ConfigHandler
	 */
	public Handlers getCollection() {
		return collection;
	}
	/**
	 * @return Current running Server Thread accepting Sockets
	 */
	public Thread getThread() {
		return thread;
	}

	private Server(Handlers col) {
		this.collection = col;
		this.listenPort = new Integer(((ConfigHandler) collection.get(
			ConfigHandler.class)).getValue("webarchive.server.port"));
		this.cList = new ArrayList<>();
		this.observerList = new ArrayList<>();

	}
	/**
	 * Main Method of getting an instance of the current running server-object.
	 * @return
	 */
	public static Server getInstance() {
		return sv;

	}
	/**
	 * creates a new instance of the Server. Must be called ONCE and only ONCE during Launch-Sequence
	 * @param col HandlerCollection created during Launchsequence
	 */
	public static void init(Handlers col) {
		sv = new Server(col);
	}
	/**
	 * starts the Server if it is not running yet.
	 * @return false on fail, true on success
	 */
	public boolean start() {
		if(checkRunning())
			return false;
		thread = new Thread(sv);
		thread.start();
		return checkRunning();
	}
	/**
	 * stops the server and disconnects all Clients if it is already running.
	 * @return false on fail, true on success
	 */
	public boolean stop() {
			if(!checkRunning())
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
	/**
	 * Prevents starting the server several times;
	 */
	private void setRunning(boolean running) {
		this.running = running;
	}
	/**
	 * Prevents starting the server several times;
	 */
	private synchronized boolean checkRunning() {
		if (isRunning()) {
			return true;
		}
		return false;
	}
	/**
	 * Prevents starting the server several times;
	 */
	private synchronized boolean initRunning() {
		if (isRunning()) {
			return true;
		}
		setRunning(true);
		return false;
	}
	/**
	 * Prevents starting the server several times;
	 */
	private synchronized void finalizeRunning() {
		setRunning(false);
	}
	/**
	 * Prevents starting the server several times;
	 */
	private boolean isRunning() {
		return running;
	}
	/**
	 * Disconnects all Clients in the cList and clears it afterwards.
	 */
	private void disconnectClients() {
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
	/**
	 * Accepts incomming Connections and starts a ClientManager-Thread upon success.
	 */
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
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null,
						e);
				disconnectClients();
				break;

			} catch (IOException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.INFO, null,
					ex);
				continue;
			}
			new Thread(new ClientManager(this,sock)).start();
		}
	}
	/**
	 * Adds the specified Connection(Client) to the cList.
	 * Mainly called by different ClientManagerThreads.
	 * @param c Connection to be added
	 */
	synchronized void addNewConnection(Connection c) {
			cList.add(c);
	}

	/**
	 * Removes a Connection from the cList and optionally from the observerList.
	 */
	@Override
	public void removeConnection(Connection c) {
		synchronized (cList) {
			cList.remove(c);
		}
		synchronized (observerList) {
			observerList.remove(c);
		}
	}
	/**
	 * Pings all Connections in the ObserverList and removes them if the Connection is broken.
	 * Then creates an Array containing all left Connections in observerList and returns it to the Notifier.
	 * @return a Connection[] containing Observers
	 */
	public Connection[] getObserverArray() {
		Connection[] cons=null;
			
			synchronized (observerList) {
				Message ping = new Message(Header.PING);
				ping.setBroadCast();
				for(Connection c : observerList) {
					try {
						c.send(ping);
					} catch (Exception e) {
						observerList.remove(c);
						cList.remove(c);
						try {
							c.getSocket().close();
						} catch (IOException e1) {
						}
						Logger.getLogger(Server.class.getName()).log(Level.INFO,"Client "+c+" was not reachable and has been removed!");
					}
				}
				cons = new Connection[observerList.size()];
				cons = observerList.toArray(cons);
			}
		return cons;
	}
	/**
	 * Gets the observerList reference.
	 * Mainly used by the DeleteObserverProcessor.
	 * @return reference to observerList
	 */
	public List<Connection> getObservers() {
		return observerList;
	}
}
