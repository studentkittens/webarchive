package webarchive.server;

import java.io.File;
import java.util.List;

import webarchive.api.model.MetaData;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Message;

public class LsProcessor implements MessageProcessor {

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		MetaData meta = (MetaData) msg.getData();
		FileDescriptor tmp = new FileDescriptor(meta, null);
		cH.getLocker().lock(tmp);
		List<File> list = cH.getIo().getFileTree(meta);
		cH.getLocker().unlock(tmp);
		Message answer = new Message(msg, list);
		try {
			cH.send(answer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
