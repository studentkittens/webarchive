package webarchive.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * 
 * The EmuHandler processes all incoming data from the JavadapterEmu.
 * 
 * @author Schneider
 *
 */

public class EmuHandler {
	public String cmd;
	public String[] cmdSplit;
	public Socket sock;
	
	public EmuHandler(Socket sock2) {
		this.sock=sock2;
	}

	public void getCmd() {
		cmd = JavadapterEmu.cmd;
		cmdSplit = cmd.split(" ");
	}

	public void testCommand() throws Exception{
		String first = cmd.split(" ")[0];
		switch(first) {
		case "lock": checkLock();
			break;
		case "unlock": checkUnlock();
			break;
		case "checkout": checkCheckout();
			break;
		case "commit": checkCommit();
			break;
		case "list_branches": checkBranches();
			break;
		case "list_commits": checkCommits();
			break;
		case "try_lock": checkTryLock();
			break;
		default: throw new Exception("unknown command");
		}
	}
	private  void checkTryLock() throws Exception{
		if(cmdSplit.length<2) throw new Exception("Parameter count not right!");;
		if(!JavadapterEmu.domains.containsKey(cmdSplit[1])) {
			send("ACK Invalid Domain.");
			return;
		}
		if(JavadapterEmu.domains.get(cmdSplit[1])) {
			send("ACK Already locked.");
			return;
		}
			
		JavadapterEmu.domains.put(cmdSplit[1], true);
		send("OK");
		
	}
	private  void checkCommits() throws Exception{
		if(cmdSplit.length<2 ) throw new Exception("Parameter count not right!");	
		if(!JavadapterEmu.domains.containsKey(cmdSplit[1])) {
			send("ACK Invalid Domain.");
			return;
		}
		for(int i= new Random().nextInt(10)+1;i>0;i--)
			send("commit-tag "+i);
		send("OK");	
		
	}
	private  void checkBranches()throws Exception {
		if(cmdSplit.length<2 )throw new Exception("Parameter count not right!");	
		if(!JavadapterEmu.domains.containsKey(cmdSplit[1])) {
			send("ACK Invalid Domain.");
			return;
		}
		for(int i= new Random().nextInt(10)+1;i>0;i--)
			send("branch-id "+i);
		send("OK");		
		
	}
	private  void checkCommit() throws Exception{
		if((cmdSplit.length<2 ))throw new Exception("Parameter count not right!");;	
		if(!JavadapterEmu.domains.containsKey(cmdSplit[1])) {
			send("ACK Invalid Domain.");
			return;
		}
		send("OK");		
	}
	private  void checkCheckout() throws Exception{
		if (cmdSplit.length<3)throw new Exception("Parameter count not right!");	
		if(!JavadapterEmu.domains.containsKey(cmdSplit[1])) {
			send("ACK Invalid Domain.");
			return;
		}
		send("/some/path/to/"+cmdSplit[1]);
		send("OK");
	}
	private  void checkUnlock()throws Exception {
		if(cmdSplit.length<2)throw new Exception("Parameter count not right!");;	
		if(!JavadapterEmu.domains.containsKey(cmdSplit[1])) {
			send("ACK Invalid Domain.");
			return;
		}
		if(!JavadapterEmu.domains.get(cmdSplit[1])) {
			send("ACK No previous lock.");
			return;
		}
			
		JavadapterEmu.domains.put(cmdSplit[1], false);
		send("OK");
	}
	private  void checkLock() throws Exception {
		if(cmdSplit.length<2)throw new Exception("Parameter count not right!");;
		if(!JavadapterEmu.domains.containsKey(cmdSplit[1])) {
			send("ACK Invalid Domain.");
			return;
		}
		while(JavadapterEmu.domains.get(cmdSplit[1]))
			Thread.sleep(10);
			
		JavadapterEmu.domains.put(cmdSplit[1], true);
		send("OK");
	}
	
	void send(String answer) throws IOException {
		PrintWriter pw = new PrintWriter(sock.getOutputStream());
		JavadapterEmu.answer=answer;
		pw.write(answer+"\n");
		pw.flush();
	}

}
