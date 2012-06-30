package webarchive.server;

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
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		FileDescriptor fd = (FileDescriptor) msg.getData();
		
		cH.getLocker().lock(fd);
		FileBuffer buf = cH.getIo().read(fd);
		cH.getLocker().unlock(fd);
		Message answer = new Message(msg, buf);
		try {
			cH.send(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
