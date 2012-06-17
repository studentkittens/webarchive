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
import webarchive.transfer.FileDescriptor;

/**
 *
 * @author ccwelich
 */
public class XmlIOHandler {

	private static TransformerFactory transformerFactory = TransformerFactory.
		newInstance();
	private final XmlConf conf;
	private final File file;
	private Transformer transformer;
	private StreamResult streamResult;
	private boolean debug;

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	XmlIOHandler(XmlConf conf, FileDescriptor xmlPath) throws
		TransformerConfigurationException {
		assert conf != null;
		assert xmlPath != null;
		this.conf = conf;
		this.file = xmlPath.getAbsolutePath();
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");

		streamResult = new StreamResult(file);
	}

	public Document buildDocument() throws ParserConfigurationException,
		IOException, SAXException {
		//TODO lock
		DocumentBuilder db = conf.getDocumentBuilderFactory().newDocumentBuilder();
		db.setErrorHandler(conf.getXmlErrorHandler());
		Document document = db.parse(file);
		//TODO unlock
		return document;
	}

	public void write(Document document) throws TransformerException {
		//TODO lock
		System.out.println("XmlIOHandler::write transformer props = " + transformer.
			getOutputProperties());
		DOMSource source = new DOMSource(document);
		if (debug) {
			streamResult = new StreamResult(System.out);
		}
		transformer.transform(source, streamResult);
		//TODO unlock

	}
}
