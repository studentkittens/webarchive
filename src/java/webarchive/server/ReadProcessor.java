package webarchive.server;

import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Message;

public class ReadProcessor implements MessageProcessor {

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
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
