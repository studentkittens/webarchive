package webarchive.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import webarchive.transfer.FileBuffer;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

public class WriteProcessor implements MessageProcessor {

	private Message msg;
	private ServerConnectionHandler cH;

	private WriteProcessor(Message msg, ServerConnectionHandler cH) {
		super();
		this.msg = msg;
		this.cH = cH;
	}
	
	public WriteProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		
		new Thread(new WriteProcessor(msg,cH)).start();
	}

	@Override
	public void run() {
		FileBuffer buf = (FileBuffer) msg.getData();
		cH.getLocker().lock(buf.getFd());
		cH.getIo().write(buf);
		cH.getLocker().unlock(buf.getFd());
		Message answer = new Message(msg, null);
		answer.setHeader(Header.SUCCESS);
		try {
			cH.send(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
