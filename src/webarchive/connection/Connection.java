package webarchive.connection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import webarchive.transfer.Transferable;

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

	public void send(Transferable obj) throws Exception {
		oos.writeObject(obj);
	}

	public Transferable receive() throws Exception {
		return (Transferable) ois.readObject();
	}

	// YOU HAVE TO SET conHandler FIRST! OTHERWISE NULLPOINTEREXCEPTION :D
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (sock.isConnected()) {
			Transferable t = null;
			try {
				t = this.receive();
			} catch (EOFException end) {
				System.out.println("lost connection");
				//do something, like delete connection from cList
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}

			conHandler.handle(t);
		}
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
