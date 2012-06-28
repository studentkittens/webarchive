package webarchive.connection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import webarchive.transfer.Message;

/**
 * Represents an open connection to a 'NetworkModule' which can either be a Client or a Server.
 * Both sides (Client and Server) use this class to communicate over a network.
 * Every Connection holds a reference to a ConnectionHandler, which is responsible for handling incoming Messages.
 * 
 * A Connection is Runnable, which provides the ability to send and receive Messages at the same time.
 * Before starting a new Thread with this Runnable, you have to initialize and set the ConnectionHandler for this Connection, otherwise you WILL get NullpointerExceptions.
 * 
 * @author Schneider
 */
public class Connection implements Runnable {

	private ObjectInputStream ois;
	
	private ObjectOutputStream oos;
	
	private Socket sock;
	
	private ConnectionHandler conHandler;

	/**
	 * Gets the input stream.
	 *
	 * @return ObjectInputStream
	 */
	public ObjectInputStream getInputStream() {
		return ois;
	}

	/**
	 * Sets the input stream.
	 *
	 * @param ois the new input stream
	 */
	private void setInputStream(ObjectInputStream ois) {
		this.ois = ois;
	}

	/**
	 * Gets the output stream.
	 *
	 * @return the output stream
	 */
	public ObjectOutputStream getOutputStream() {
		return oos;
	}

	/**
	 * Sets the output stream.
	 *
	 * @param oos the new output stream
	 */
	private void setOutputStream(ObjectOutputStream oos) {
		this.oos = oos;
	}

	/**
	 * Gets the socket.
	 *
	 * @return the socket
	 */
	public Socket getSocket() {
		return sock;
	}

	/**
	 * Sets the socket.
	 *
	 * @param sock the new socket
	 */
	private void setSocket(Socket sock) {
		this.sock = sock;
	}

	/**
	 * Instantiates a new connection.
	 *
	 * @param sock the socket
	 * @param oos the OutputStream
	 * @param ois the InputStream
	 */
	public Connection(Socket sock, ObjectOutputStream oos, ObjectInputStream ois) {
		setSocket(sock);
		setInputStream(ois);
		setOutputStream(oos);
	}

	/**
	 * Gets the ConnectionHandler associated with this Connection.
	 *
	 * @return the ConnectionHandler
	 */
	public ConnectionHandler getConHandler() {
		return conHandler;
	}

	/**
	 * Sets the ConnectionHandler.
	 * This must be called before "starting" this Connection with Thread.start() .
	 *
	 * @param conHandler a reference to the new ConnectionHandler
	 */
	public void setConHandler(ConnectionHandler conHandler) {
		this.conHandler = conHandler;
	}

	/**
	 * Method to send Message-Objects over the socket.
	 * Throws a variety of different exceptions.
	 *
	 * @param msg the msg
	 * @throws InvalidClassException, NotSerializableException, IOException 
	 */
	public void send(Message msg) throws Exception {
		oos.writeObject(msg);
	}

	/**
	 * Receive Messages over the socket.
	 * Calling this blocks the calling Thread.
	 * Should not be called manually, instead start() a new Thread with this Connection-Instance.
	 * This Method will block the new Thread and delegates the received Message to the ConnectionHandler.
	 * 
	 * Throws a variety of different exceptions.
	 *
	 * @return incoming Message
	 * @throws ClassNotFoundException, InvalidClassException, StreamCorruptedException, OptionalDataException, IOException 
	 */
	private Message receive() throws Exception {
		return (Message) ois.readObject();
	}
	
	/**
	 * Convenience-Method that calls the waitForAnswer-Method of the ConnectionHandler.
	 * 
	 * @see webarchive.connection.ConnectionHandler#waitForAnswer(Message)
	 *
	 * @param m previously sent Message
	 * @return the answer-Message to m
	 */
	public Message waitForAnswer(Message m) {
		
		return conHandler.waitForAnswer(m);
	}

	// YOU HAVE TO SET conHandler FIRST! OTHERWISE NULLPOINTEREXCEPTION :D
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (sock.isConnected()) {
			Message msg = null;
			try {
				msg = this.receive();
			} catch (SocketException | EOFException end) {
				System.out.println("lost connection");
				conHandler.removeConnection(this);
				break;
			} catch (Exception e) {
				//TODO
				continue;
			}

			conHandler.handle(msg);
		}
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
