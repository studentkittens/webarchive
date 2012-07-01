package webarchive.server;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import webarchive.api.model.MetaData;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Message;

public class LsProcessor implements MessageProcessor {

	private Message msg;
	private ServerConnectionHandler cH;


	private LsProcessor(Message msg, ServerConnectionHandler cH) {
		super();
		this.msg = msg;
		this.cH = cH;
	}

	public LsProcessor() {
	}

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		new Thread(new LsProcessor(msg,cH)).start();
	}

	@Override
	public void run() {
		MetaData meta = (MetaData) msg.getData();
		FileDescriptor tmp = new FileDescriptor(meta, null);
		cH.getLocker().lock(tmp);
		List<File> list = cH.getIo().getFileTree(meta);
		cH.getLocker().unlock(tmp);
		Message answer = new Message(msg, list);
		try {
			cH.send(answer);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,null,e);
		}		
	}

}
