package webarchive.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import webarchive.transfer.FileDescriptor;

public class LockHandlerImpl extends LockHandler   {

	private PrintWriter out = null;
	private Scanner in = null;
	
	private InetAddress ip;
	private int port;
	private Socket sock;
	private int bla=-1;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void unlockDomain(String domain) {
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
		System.out.println(answer);
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
		
	}
	
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
		System.out.println("Thread "+bla+" "+answer);
		if(answer.equals("OK"))
			return;
		System.out.print("Thread "+bla+" caused:");
		throw new Exception(answer);
	}

	private void checkoutMaster(String domain) {
		out.write("checkout "+domain+" master\n");
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
	
	public static void main(String args[]) throws UnknownHostException, InterruptedException {
		final String domain = "www.stackoverflow.com";
		final LockHandlerImpl l = new LockHandlerImpl(InetAddress.getLocalHost(),42421);

		l.lockDomain(domain);
		System.out.println("l locked");
		for(int i=0;i<2;i++) {
			final LockHandlerImpl j = new LockHandlerImpl(InetAddress.getLocalHost(),42421);
			j.bla=i;
			new Thread(new Runnable() {
				public void run() {
					j.lockDomain(domain);
					System.out.println(j.bla+" locked");
					System.out.println("Lock file actually exists: "+new File("/tmp/archive/content/www.stackoverflow.com.lock").exists());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					j.unlockDomain(domain);
					System.out.println(j.bla+" unlocked");
				}
			}).start();
		}
		l.unlockDomain(domain);
		System.out.println("l unlocked");
	}
	
}
