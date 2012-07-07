package webarchive.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import webarchive.transfer.FileDescriptor;

public class LockHandlerImpl extends LockHandler   {

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
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
			}
		}
		
		try {
			this.sock = new Socket(ip,port);
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		}
		try {
			out = new PrintWriter(sock.getOutputStream());
			in = new Scanner(sock.getInputStream());
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		}
		
	}
	
	
	@Override
	public void lock(FileDescriptor fd) {
		String domain = fd.getMetaData().getCommitTag().getDomain();
		lockDomain(domain);
		checkout(fd);
	}

	@Override
	public void unlock(FileDescriptor fd) {
		commit(fd);
		String domain = fd.getMetaData().getCommitTag().getDomain();
		unlockDomain(domain);
		checkoutMaster(domain);
	}
	
	private void lockDomain(String domain) {
		out.write("lock "+ domain+"\n");
		out.flush();
		String answer = in.nextLine();
		try {
			processAnswer(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		}
	}
	
	private void unlockDomain(String domain) {
		out.write("unlock "+ domain+"\n");
		out.flush();
		String answer = in.nextLine();
		//System.out.println("Unlock "+domain);
		try {
			processAnswer(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		}
	}
	@Override
	public void checkout(FileDescriptor fd) {
		String domain = fd.getMetaData().getCommitTag().getDomain();
		String commitTag = fd.getMetaData().getCommitTag().getCommitTime().getXmlFormat();
		
		out.write("checkout "+ domain+" "+commitTag+"\n");
		out.flush();
		String answer = in.nextLine();
		if(answer.substring(0, 3).equals("ACK")) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Checkout returned: "+answer+" "+domain+" "+commitTag);
			return;
		}
		answer = in.nextLine();
		try {
			processAnswer(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
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
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		}
		
	}

	public void list_branches(String domain) {
		out.write("list_branches "+ domain+"\n");
		out.flush();
		String answer = in.nextLine();
		while (!answer.equals("OK")) {
				answer = in.nextLine();
		}
		try {
			processAnswer(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);

		}
		
	}
	public void list_commits(String domain) {
		//System.out.println(domain);
		out.write("list_commits "+ domain+"\n");
		out.flush();
		String answer = in.nextLine();
		while (!answer.equals("OK")) {
				answer = in.nextLine();
		}
		try {
			processAnswer(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		}
		
	}
	
	@SuppressWarnings("unused")
	private boolean tryLockDomain(String domain) {
		out.write("try_lock "+ domain+"\n");
		out.flush();
		String answer = in.nextLine();
		if(answer.equals("OK")) {
			return true;
		}
		return false;
	}
	
	private void processAnswer(String answer) throws Exception {
		//System.out.println(answer);
		if(answer.equals("OK") || answer.equals("ACK commit returned 1"))
			return;
		throw new Exception(answer);
	}

	private void checkoutMaster(String domain) {
		out.write("checkout "+domain+" master\n");
		out.flush();
		String answer = in.nextLine();
		if(answer.substring(0, 3).equals("ACK")) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Checkout returned: "+answer+" "+domain+" master");
			return;
		}
		answer = in.nextLine();
		try {
			processAnswer(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		}
	}
	
	
}
