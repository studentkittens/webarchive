package webarchive.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import webarchive.transfer.FileDescriptor;
/**
 * Represents the counterpart to the Python Module "Javadapter".
 * It connects to an active  Javadapter and manages lock,unlock,checkout and commit requests by MessageProcessors.
 * Every connected Client gets a new LockHandler with an own connection to the Javadapter.
 * @author Schneider
 *
 */
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
	/**
	 * Closes and removes the Connection to the Javadapter.
	 * Then creates a new Connection using the set ip and port.
	 */
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
	
	/**
	 * Tells the Javadapter to lock and then checkout the domain and commit in the fd.getMetaData().
	 */
	@Override
	public void lock(FileDescriptor fd) {
		String domain = fd.getMetaData().getCommitTag().getDomain();
		lockDomain(domain);
		checkout(fd);
	}
	/**
	 * Tells the Javadapter to commit, checkout the master branche and then unlock the domain;
	 */
	@Override
	public void unlock(FileDescriptor fd) {
		commit(fd);
		String domain = fd.getMetaData().getCommitTag().getDomain();
		checkoutMaster(domain);
		unlockDomain(domain);
	}
	
	/**
	 * Implementation of the locking communication with the Javadapter.
	 * @param domain to Lock
	 */
	
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
	/**
	 * Implementation of the unlocking communication with the Javadapter.
	 * @param domain to Lock
	 */
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
	/**
	 * Implementation of the checkout communication with the Javadapter.
	 * @param fd FileDescriptor with metaData and CommitTag
	 */
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
	/**
	 * Implementation of the commit communication with the Javadapter.
	 * @param fd FileDescriptor with metaData and CommitTag
	 */
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
	/**
	 * Was only used for testing purposes. Works as intended.
	 * @param domain
	 */
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
	/**
	 * Was only used for testing purposes. Works as intended.
	 * @param domain
	 */
	public void list_commits(String domain) {
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
	/**
	 * Was only used for testing purposes. Works as intended.
	 * @param domain
	 */
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
	/**
	 * Reacts to messages from the Javadapter. Filters "OK" Messages and the nothing to commit Message.
	 * @param answer from Javadapter
	 * @throws Exception containing the Error Message directly from the Javadapter
	 */
	private void processAnswer(String answer) throws Exception {
		if(answer.equals("OK") || answer.equals("ACK commit returned 1"))
			return;
		throw new Exception(answer);
	}
	/**
	 * Checks out the master branch after all changes have been commited.
	 * @param domain
	 */
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
	/**
	 * Gets the current InetAddress.
	 * @return current InetAddress set in this LockHandlerImpl 
	 */
	public InetAddress getIp() {
		return ip;
	}
	/**
	 * Sets a new InetAddress for Connections to the Javadapter.
	 * Does not take effect until reconnect() has been called.
	 */
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	/**
	 * Get Current Port in this LockHandlerImpl.
	 * @return Current Port in this LockHandlerImpl.
	 */
	public int getPort() {
		return port;
	}
	/**
	 * Sets a new Port for Connections to the Javadapter.
	 * Does not take effect until reconnect() has been called.
	 */
	public void setPort(int port) {
		this.port = port;
	}

}
