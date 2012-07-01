package webarchive.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

public class ReadProcessor implements MessageProcessor {

	private ServerConnectionHandler cH;
	private Message msg;

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		new Thread(new ReadProcessor(cH,msg)).start();

	}

	private ReadProcessor(ServerConnectionHandler cH, Message msg) {
		super();
		this.cH = cH;
		this.msg = msg;
	}

	public ReadProcessor() {
	}

	@Override
	public void run() {
		FileDescriptor fd = (FileDescriptor) msg.getData();
		
		cH.getLocker().lock(fd);
		FileBuffer buf;
		try {
			buf = cH.getIo().read(fd);
		} catch (IOException e1) {
			Message exception = new Message(msg,e1);
			exception.setHeader(Header.EXCEPTION);
			try {
				cH.send(exception);
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "Could not send the exception to the client!\n"+e );
			}
			cH.getLocker().unlock(fd);
			return;
		}
		cH.getLocker().unlock(fd);
		Message answer = new Message(msg, buf);
		try {
			cH.send(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,"Could not send an answer to the client!\n " + e);
		}		
	}

}
