package webarchive.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import webarchive.server.LockHandler;
import webarchive.server.Server;
import webarchive.transfer.FileDescriptor;

/**
 *
 * @author ccwelich
 */
public class XmlIOHandler {

	private static TransformerFactory transformerFactory = TransformerFactory.
		newInstance();
	private final XmlConf conf;
	private final FileDescriptor xmlFile;
	private Transformer transformer;
	private StreamResult streamResult;
	private LockHandler locker;

	public void checkout() {
		locker.checkout(xmlFile);
	}

	public void lock() {
		locker.lock(xmlFile);
	}
	

	XmlIOHandler(XmlConf conf, FileDescriptor xmlPath) throws
		TransformerConfigurationException {
		assert conf != null;
		assert xmlPath != null;
		this.conf = conf;
		this.xmlFile = xmlPath;
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		
//		this.locker = (LockHandler)Server.getInstance().getHandlers().get("LockHandler");
		
		streamResult = new StreamResult(xmlFile.getAbsolutePath());
	}

	public Document buildDocument() throws ParserConfigurationException,
		IOException, SAXException {
		DocumentBuilder db = conf.getDocumentBuilderFactory().newDocumentBuilder();
		db.setErrorHandler(conf.getXmlErrorHandler());
		Document document = db.parse(xmlFile.getAbsolutePath());
		return document;
	}

	public void write(Document document) throws TransformerException {
		System.out.println("XmlIOHandler::write transformer props = " + transformer.
			getOutputProperties());
		DOMSource source = new DOMSource(document);
		transformer.transform(source, streamResult);

	}

	public void unlock() {
		locker.unlock(xmlFile);
	}
}
