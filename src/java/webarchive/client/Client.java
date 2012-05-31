package webarchive.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import webarchive.connection.Connection;

public class Client  {
	public static final int DEFAULT_PORT = 21000;

	private String ip;
	private int port;
	private Connection c;
	
	public Connection getConnection() {
		return c;
	}

	public Client()
	{
		this.port = DEFAULT_PORT;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	public void connectToServer()
	{
		if(ip==null)
		{
			throw new NullPointerException();
		}
		this.connectToServer(ip,port);
	}
	
	public void connectToServer(String ip, int port)
	{
		
		try {
			System.out.println("trying to connect");
			Socket sock = new Socket(InetAddress.getByName(ip),port);
			System.out.println("connected");
			c = new Connection(sock, new ObjectOutputStream(sock.getOutputStream()) , new ObjectInputStream(sock.getInputStream()));
			c.setConHandler(new ClientConnectionHandler(c));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//could not connect und so
			return;
		}
		
		new Thread(c).start();
		
	}
//###############################################################################
	
	public static void main(String args[])
	{
		Client cl = new Client();
		cl.setIp("localhost");
		cl.connectToServer();
	}
	
}
