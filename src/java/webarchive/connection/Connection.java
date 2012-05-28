package webarchive.connection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import webarchive.transfer.Message;

public class Connection implements Runnable {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket sock;
	private ConnectionHandler conHandler;

	public ObjectInputStream getInputStream() {
		return ois;
	}

	public void setInputStream(ObjectInputStream ois) {
		this.ois = ois;
	}

	public ObjectOutputStream getOutputStream() {
		return oos;
	}

	public void setOutputStream(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public Socket getSocket() {
		return sock;
	}

	public void setSocket(Socket sock) {
		this.sock = sock;
	}

	public Connection(Socket sock, ObjectOutputStream oos, ObjectInputStream ois) {
		setSocket(sock);
		setInputStream(ois);
		setOutputStream(oos);
	}

	public ConnectionHandler getConHandler() {
		return conHandler;
	}

	public void setConHandler(ConnectionHandler conHandler) {
		this.conHandler = conHandler;
	}

	public void send(Message msg) throws Exception {
		oos.writeObject(msg);
	}

	private Message receive() throws Exception {
		return (Message) ois.readObject();
	}
	
	public Message waitForAnswer(Message m) {
		
		return conHandler.waitForAnswer(m);
	}

	// YOU HAVE TO SET conHandler FIRST! OTHERWISE NULLPOINTEREXCEPTION :D
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (sock.isConnected()) {
			Message msg = null;
			try {
				msg = this.receive();
			} catch (SocketException sockExc) {
				System.out.println("lost connection");
				//do something, like delete connection from cList
				break;
			} catch (EOFException end) {
				System.out.println("lost connection");
				//do something, like delete connection from cList
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
