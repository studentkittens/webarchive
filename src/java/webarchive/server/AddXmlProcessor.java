package webarchive.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Header;
import webarchive.transfer.Message;
import webarchive.xml.DataElement;
import webarchive.xml.XmlHandler;

public class AddXmlProcessor implements MessageProcessor {

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		DataElement element = (DataElement) msg.getData();
		FileDescriptor fd = (FileDescriptor) msg.getData();
		Message answer;
		try {					
			XmlHandler xmlH = cH.getXmlHandler(fd);
			xmlH.addDataElement(element);
			answer = new Message(msg, xmlH.getDocument());
			answer.setHeader(Header.ADDXMLEDIT);
		} catch (Exception ex) {
			answer = new Message(msg, ex);
			answer.setHeader(Header.EXCEPTION);
		}
		try {
			cH.send(answer);
		} catch (Exception ex) {
			Logger.getLogger(ServerConnectionHandler.class.getName()).
				log(Level.SEVERE, null, ex);
		}

	}

}
