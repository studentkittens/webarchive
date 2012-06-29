package webarchive.server;

import webarchive.transfer.FileBuffer;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

public class WriteProcessor implements MessageProcessor {

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
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
