package webarchive.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import webarchive.transfer.FileBuffer;
import webarchive.transfer.Header;
import webarchive.transfer.Message;
/**
 * Proceses Write-File Requests by Clients.
 * @author Schneider
 *
 */
public class WriteProcessor implements MessageProcessor {

	private Message msg;
	private ServerConnectionHandler cH;

	private WriteProcessor(Message msg, ServerConnectionHandler cH) {
		super();
		this.msg = msg;
		this.cH = cH;
	}
	
	public WriteProcessor() {
	}

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		
		new Thread(new WriteProcessor(msg,cH)).start();
	}

	@Override
	public void run() {
		FileBuffer buf = (FileBuffer) msg.getData();
		cH.getLocker().lock(buf.getFd());
		try {
			cH.getIo().write(buf);
		} catch (Exception e1) {
			Message exception = new Message(msg,e1);
			exception.setHeader(Header.EXCEPTION);
			try {
				cH.send(exception);
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "Could not send the exception to the client!\n"+e );
			}
			cH.getLocker().unlock(buf.getFd());
			return;
		}
		cH.getLocker().unlock(buf.getFd());
		Message answer = new Message(msg, null);
		answer.setHeader(Header.SUCCESS);
		try {
			cH.send(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,"Could not send an answer to the client!\n " + e);
		}		
	}

}
