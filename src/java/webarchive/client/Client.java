package webarchive.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;

import webarchive.connection.Connection;
import webarchive.connection.NetworkModule;
import webarchive.transfer.Message;

/**
 * Client is a NetworkModule holding the Connection to the Server NetworkModule and can be used as an Observable.
 * 
 * It is a Singleton Object and should not be instantiated manually. Instead use the WebarchiveClientFactory to generate a WebarchiveClient-Object which holds a Client Object.
 * 
 * @author Schneider
 */
public class Client implements NetworkModule  {
	
	/** The Constant DEFAULT_PORT. */
	public static final int DEFAULT_PORT = 42420;

	private InetAddress ip;
	
	private int port;
	
	private Connection c;
	
	private Observable observ;
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public Connection getConnection() {
		return c;
	}

	/**
	 * Instantiates a new client.
	 */
	Client()
	{
		this.port = DEFAULT_PORT;
	}

	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public InetAddress getIp() {
		return ip;
	}

	/**
	 * Sets the ip.
	 *
	 * @param ip the new ip
	 */
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	
	/**
	 * Connect to server.
	 * @throws IOException 
	 */
	public void connectToServer() throws IOException
	{
		if(ip==null)
		{
			throw new NullPointerException();
		}
		try {
			this.connectToServer(ip,port);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Connect to server.
	 *
	 * @param ip the ip
	 * @param port the port
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void connectToServer(InetAddress ip, int port) throws IOException, ClassNotFoundException
	{
		
		try {
			System.out.println("trying to connect");
			Socket sock = new Socket(ip,port);
			System.out.println("connected");
			c = new Connection(sock, new ObjectOutputStream(sock.getOutputStream()) , new ObjectInputStream(sock.getInputStream()));
			c.setConHandler(new ClientConnectionHandler(c, this));
		} catch (IOException e) {
			throw e;
		}
		Message shake = (Message) c.getInputStream().readObject();
		try {
			c.send(shake);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Thread(c).start();
		
		
	}

	/* (non-Javadoc)
	 * @see webarchive.connection.NetworkModule#removeConnection(webarchive.connection.Connection)
	 */
	@Override
	public void removeConnection(Connection c) {
		//DONOTIN
	}
	
	/**
	 * Gets the observable.
	 *
	 * @return the observable
	 */
	public Observable getObservable() {
		return observ;
	}

	/**
	 * Sets the observable.
	 *
	 * @param observ the new observable
	 */
	public void setObservable(Observable observ) {
		this.observ = observ;
	}

	public void disconnect() {
		try {
			c.getSocket().close();
		} catch (IOException e) {
		}
	}
	
}
