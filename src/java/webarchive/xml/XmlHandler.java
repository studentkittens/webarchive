package webarchive.xml;

import java.io.IOException;
import java.util.logging.Handler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import webarchive.handler.Handlers;
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

	private Document document;
	private Element data;
	private final XmlIOHandler ioHandler;
	private AutoValidatingMode mode;

	public Document getDocument() {
		return document;
	}

	XmlHandler(XmlIOHandler ioHandler) throws
		ParserConfigurationException, SAXException, IOException {
		// preconditions
		assert ioHandler != null;
		
		this.ioHandler = ioHandler;
		this.mode = ((XmlConf)Handlers.get(XmlConf.class)).getAutoValidatingMode();
		
		// build document
		ioHandler.lock();
		buildDocument();
		ioHandler.unlock();

		
	}

	public XmlIOHandler getIoHandler() {
		return ioHandler;
	}

	private void buildDocument() throws ParserConfigurationException,
		IOException, SAXException {
		document = ioHandler.buildDocument();
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_BUILT_DOM) {
			validate();
		}
		final String dataTag = ((XmlConf)Handlers.get(XmlConf.class)).getDataTag();
		this.data = (Element) (document.getElementsByTagName(dataTag).item(0));
	}

	public XmlEditor newEditor() {
		return new XmlEditor(document);
	}

	private void validate() throws SAXException, IOException {
		Validator v = ((XmlMethodFactory)Handlers.get(XmlMethodFactory.class)).newXmlValidator();
		v.validate(new DOMSource(document));
	}

	public void addDataElement(DataElement dataElement) throws IllegalArgumentException,
		SAXException, IOException, TransformerException,
		ParserConfigurationException {
		assert dataElement != null;
		
		ioHandler.lock();
		buildDocument();
		final String tagName = dataElement.getDataElement().getTagName();
		// checking for duplicate
		if (data.getElementsByTagName(tagName).getLength() > 0) {
			throw new IllegalArgumentException(
				"element " + tagName + " already exists");
		}
		// append in sequence
		data.appendChild(document.adoptNode(dataElement.getDataElement()));
		// validate
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_UPDATE) {
			validate();
		}
		// write to disk
		ioHandler.write(document);
		ioHandler.unlock();
	}
	
}
