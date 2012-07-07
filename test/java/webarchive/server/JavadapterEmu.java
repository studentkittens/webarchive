package webarchive.server;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * This Class acts as an emulated Javadapter. Starting the real javadapter would mean starting also the crawler and creating a temporary archive.
 * So the JavadapterEmu makes testing the Lockhandler much easier and faster.
 * 
 * @author Schneider
 *
 */
public class JavadapterEmu implements Runnable {

	ServerSocket svSock;
	static String cmd;
	static String answer;
	static Exception exc = null;
	static final Map<String,Boolean> domains = new HashMap<String,Boolean>();
	public JavadapterEmu() {
		
		domains.put("www.heise.de",false);
		domains.put("www.golem.de",false);
		domains.put("www.hack.org",false);
		domains.put("hwluxx.de", false);
		
		
		try {
			svSock = new ServerSocket(42421);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized static void sendAnswer(Socket sock) {
		
	}
	@Override
	public void run() {
		Socket sock = null;
		while(!svSock.isClosed()) {
			try {
				sock = svSock.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			InputStream is = null;
			try {
				sock.getOutputStream();
				is = sock.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Scanner scan = new Scanner(is);
			while(!sock.isClosed()) {
				cmd = scan.nextLine();
				EmuHandler et = new EmuHandler(sock);
				et.getCmd();
				try {
					et.testCommand();
				} catch (Exception e) {
					exc = e;
					try {
						et.send("ACK "+e.getMessage());
					} catch (IOException e1) {
					}
				}
			}
		}
	}
	
	public static void main(String args[]) throws InterruptedException {
		Thread t = new Thread(new JavadapterEmu());
		t.start();
		t.join();
	}
}
