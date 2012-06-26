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

/**
 * Mid-level controller, used to access XML-files. XmlIOHandler builds
 * w3c.dom.Document objects from XML-files and writes them back vice versa. It
 * has also access methods for file-locking. Usually new XmlIOHandlers are
 * created via {@link XmlMethodFactory}
 *
 * @author ccwelich
 */
public class XmlIOHandler {

	private Transformer transformer;
	private final FileDescriptor xmlFile;
	private LockHandler locker;

	/**
	 * create new XmlIOHandler
	 *
	 * @param xmlPath XML-path in archive
	 * @param transformer transformer, used for Dom-output
	 * @param locker locker, used for file locking operations
	 */
	XmlIOHandler(FileDescriptor xmlPath, Transformer transformer,
		LockHandler locker) {
		assert xmlPath != null;
		assert transformer != null;
		assert locker != null;
		this.xmlFile = xmlPath;
		this.transformer = transformer;
		this.locker = locker;

	}

	/**
	 * build new Document
	 *
	 * @return document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	Document buildDocument() throws ParserConfigurationException,
		SAXException, IOException {
		DocumentBuilder db = Handlers.get(XmlMethodFactory.class).
			newDocumentBuilder();
		Document document = db.parse(xmlFile.getAbsolutePath());
		return document;
	}

	/**
	 * write dom-object to disc
	 *
	 * @param document
	 * @throws TransformerException
	 */
	public void write(Document document) throws TransformerException {
		DOMSource source = new DOMSource(document);
		StreamResult streamResult = new StreamResult(xmlFile.getAbsolutePath());
		transformer.transform(source, streamResult);
	}

	/**
	 * macro-operation for file-locking, includes checkout
	 */
	public void lock() {
		//TODO check correctness
		locker.lock(xmlFile);
		locker.checkout(xmlFile);
	}

	/**
	 * macro-operation for file-unlocking, includes commit and checkout
	 */
	public void unlock() {
		//TODO check correctness
		locker.commit(xmlFile);
		locker.checkoutMaster();
		locker.unlock(xmlFile);
	}
}
