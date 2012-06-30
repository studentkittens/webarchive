package webarchive.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import webarchive.transfer.DataElementContainer;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Header;
import webarchive.transfer.Message;
import webarchive.xml.DataElement;
import webarchive.xml.XmlHandler;

public class AddXmlProcessor implements MessageProcessor {

	
	private Message msg;
	private ServerConnectionHandler cH;

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		new Thread(new AddXmlProcessor(msg,cH)).start();
	}

	@Override
	public void run() {
		DataElementContainer cont = (DataElementContainer) msg.getData();
		DataElement element = cont.getDataElement();
		FileDescriptor fd = cont.getXmlFile();
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
			System.out.println(answer);
			cH.send(answer);
		} catch (Exception ex) {
			Logger.getLogger(ServerConnectionHandler.class.getName()).
				log(Level.SEVERE, null, ex);
		}
	}

	private AddXmlProcessor(Message msg, ServerConnectionHandler cH) {
		super();
		this.msg = msg;
		this.cH = cH;
	}

	public AddXmlProcessor() {
	}

}
