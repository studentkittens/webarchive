package webarchive.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
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
	private XmlMethodFactory xmlMethFac;
	/**
	 * create new XmlIOHandler
	 *
	 * @param xmlPath XML-path in archive
	 * @param transformer transformer, used for Dom-output
	 * @param locker locker, used for file locking operations
	 */
	XmlIOHandler(FileDescriptor xmlPath, Transformer transformer,
		LockHandler locker, XmlMethodFactory xmlMethFac) {
		assert xmlPath != null;
		assert transformer != null;
		assert locker != null;
		assert xmlMethFac != null;
		this.xmlFile = xmlPath;
		this.transformer = transformer;
		this.locker = locker;
		this.xmlMethFac = xmlMethFac;

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
		DocumentBuilder db = xmlMethFac.newDocumentBuilder();
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
		System.out.println("XmlIoHandler::write path="+xmlFile.getAbsolutePath() );
		DOMSource source = new DOMSource(document);
		StreamResult streamResult = new StreamResult(xmlFile.getAbsolutePath());
		transformer.transform(source, streamResult);
		Scanner sc = null;
		try {
			sc = new Scanner(new FileInputStream(xmlFile.getAbsolutePath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(sc.hasNextLine()) System.out.println(">>"+sc.nextLine());
	}

	/**
	 * macro-operation for file-locking, includes checkout
	 */
	public void lock() {
		locker.lock(xmlFile);
	}

	/**
	 * macro-operation for file-unlocking, includes commit and checkout
	 */
	public void unlock() {
		locker.unlock(xmlFile);
	}

	String getFileName() {
		return xmlFile.getAbsolutePath().toString();
	}

	public FileDescriptor getXmlFile() {
		return xmlFile;
	}
}
