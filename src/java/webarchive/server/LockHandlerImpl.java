package webarchive.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import webarchive.handler.Handler;
import webarchive.transfer.FileDescriptor;

public class LockHandlerImpl extends Handler implements LockHandler {

	private PrintWriter out = null;
	private Scanner in = null;
	
	private InetAddress ip;
	private int port;
	private Socket sock;
	
	public LockHandlerImpl(InetAddress ip, int port) {
		this.ip = ip;
		this.port=port;
		
		reconnect();
		
	}
	
	@Override
	public void reconnect() {
		if(sock != null && !sock.isClosed()) {
			try {
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			this.sock = new Socket(ip,port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out = new PrintWriter(sock.getOutputStream());
			in = new Scanner(sock.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void lock(FileDescriptor fd) {
		String domain = fd.getMetaData().getCommitTag().getDomain();
		out.write("lock "+ domain+"\n");
		out.flush();
		String answer = in.nextLine();
		try {
			processAnswer(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkout(fd);
	}

	@Override
	public void unlock(FileDescriptor fd) {
		commit(fd);
		String domain = fd.getMetaData().getCommitTag().getDomain();
		out.write("unlock "+ domain+"\n");
		out.flush();
		String answer = in.nextLine();
		try {
			processAnswer(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void checkout(FileDescriptor fd) {
		String domain = fd.getMetaData().getCommitTag().getDomain();
		String commitTag = fd.getMetaData().getCommitTag().toString();
		out.write("checkout "+ domain+" "+commitTag+"\n");
		out.flush();
		String answer = in.nextLine();
		if(answer.substring(0, 3).equals("ACK")) {
			//TODO
			return;
		}
		answer = in.nextLine();
		try {
			processAnswer(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void commit(FileDescriptor fd) {
		String domain = fd.getMetaData().getCommitTag().getDomain();
		out.write("commit "+ domain+"\n");
		out.flush();
		String answer = in.nextLine();
		try {
			processAnswer(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.write("checkout "+domain+" master\n");
		out.flush();
		answer = in.nextLine();
		if(answer.substring(0, 3).equals("ACK")) {
			//TODO
			return;
		}
		answer = in.nextLine();
		try {
			processAnswer(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void processAnswer(String answer) throws Exception {
		if(answer.equals("OK"))
			return;
		throw new Exception(answer.substring(4));
	}
	
}
