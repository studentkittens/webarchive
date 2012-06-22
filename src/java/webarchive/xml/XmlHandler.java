package webarchive.xml;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import webarchive.transfer.FileDescriptor;

/**
 * Xml controller on the server side. It controlls the loading of
 * webarchive-xml-files into dom-objects, validation and adding of new elements.
 * Each XmlHandler has responsibility for one xml-file.
 *
 * @author ccwelich
 */
//TODO c2 finish implementation
//TODO tests
//TODO javadoc
public class XmlHandler {

	/**
	 * the auto modi for validating the xml documents.
	 */
	public enum AutoValidatingMode {

		/**
		 * documents will never be validated.
		 */
		NEVER,
		/**
		 * documents will be validated after each change and when loaded.
		 */
		ALWAYS,
		/**
		 * documents will be validated after each changed.
		 */
		AFTER_UPDATE,
		/**
		 * documents will be validated after each built of the dom.
		 */
		AFTER_BUILT_DOM;
	}
	private final XmlConf conf;
	private Document document;
	private Element data;
	private final XmlIOHandler ioHandler;

	public Document getDocument() {
		return document;
	}

	public XmlHandler(FileDescriptor xmlPath, XmlConf conf) throws
		ParserConfigurationException, SAXException, IOException,
		TransformerConfigurationException {
		// preconditions
		assert xmlPath != null;
		assert conf != null;
		this.conf = conf;

		// set ioHandler
		this.ioHandler = conf.getIOHandler(xmlPath);

		// build document
		ioHandler.checkout();
		buildDocument();
		ioHandler.unlock();

		
	}

	public XmlIOHandler getIoHandler() {
		return ioHandler;
	}

	private void buildDocument() throws ParserConfigurationException,
		IOException, SAXException {
		document = ioHandler.buildDocument();
		XmlHandler.AutoValidatingMode mode = conf.getAutoValidatingMode();
		if (mode == XmlHandler.AutoValidatingMode.ALWAYS || mode == XmlHandler.AutoValidatingMode.AFTER_BUILT_DOM) {
			validate();
		}
		this.data = (Element) (document.getElementsByTagName(conf.getDataTag()).item(0));
	}

	public XmlEditor newEditor() {
		return new XmlEditor(document, conf);
	}

	private void validate() throws SAXException, IOException {
		conf.getXmlValidator().validate(document);
	}

	public void addDataElement(DataElement e) throws IllegalArgumentException,
		SAXException, IOException, TransformerException,
		ParserConfigurationException {
		assert e != null;
		ioHandler.checkout();
		buildDocument();
		final String tagName = e.getDataElement().getTagName();
		// checking for duplicate
		if (data.getElementsByTagName(tagName).getLength() > 0) {
			throw new IllegalArgumentException(
				"element " + tagName + " already exists");
		}
		// append in sequence
		data.appendChild(document.adoptNode(e.getDataElement()));
		// validate
		AutoValidatingMode mode = conf.getAutoValidatingMode();
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_UPDATE) {
			validate();
		}
		// write to disk
		ioHandler.write(document);
		ioHandler.unlock();
	}
}
