package webarchive.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import webarchive.api.xml.XmlEditor;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Header;
import webarchive.transfer.Message;
import webarchive.xml.XmlHandler;

public class GetXmlProcessor implements MessageProcessor {

	private ServerConnectionHandler cH;
	private Message msg;

	@Override
	public void process(Message msg, ServerConnectionHandler cH) {
		new Thread(new GetXmlProcessor(cH,msg)).start();

	}

	private GetXmlProcessor(ServerConnectionHandler cH, Message msg) {
		super();
		this.cH = cH;
		this.msg = msg;
	}

	public GetXmlProcessor() {
	}

	@Override
	public void run() {
		FileDescriptor fd = (FileDescriptor) msg.getData();
		XmlHandler xmlH;
		Message answer;
		try {
			xmlH = cH.getXmlHandler(fd);
			XmlEditor xmlEd = xmlH.newEditor();
			answer = new Message(msg, xmlEd);
			answer.setHeader(Header.GETXMLEDIT);
			
		} catch (SAXException ex) {
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
