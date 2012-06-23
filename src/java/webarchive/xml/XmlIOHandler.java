package webarchive.xml;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import webarchive.handler.Handlers;
import webarchive.server.LockHandler;
import webarchive.transfer.FileDescriptor;
//TODO Tests, javadoc

/**
 *
 * @author ccwelich
 */
public class XmlIOHandler {

	public Transformer transformer;
	private final FileDescriptor xmlFile;
	private LockHandler locker;

	XmlIOHandler(FileDescriptor xmlPath, Transformer transformer,
		LockHandler locker) {
		assert xmlPath != null;
		assert transformer != null;
		assert locker != null;
		this.xmlFile = xmlPath;
		this.transformer = transformer;
		this.locker = locker;

	}

	public Document buildDocument() throws ParserConfigurationException,
		SAXException, IOException {
		DocumentBuilder db = Handlers.get(XmlMethodFactory.class).
			newDocumentBuilder();
		Document document = db.parse(xmlFile.getAbsolutePath());
		return document;
	}

	public void write(Document document) throws TransformerException {
		DOMSource source = new DOMSource(document);
		StreamResult streamResult = new StreamResult(xmlFile.getAbsolutePath());
		transformer.transform(source, streamResult);
	}

	public void lock() {
		//TODO 
		locker.lock(xmlFile);
		locker.checkout(xmlFile);
	}

	public void unlock() {
		//TODO
		locker.checkoutMaster();
		locker.unlock(xmlFile);
	}
}
