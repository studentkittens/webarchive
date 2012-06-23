package webarchive.xml;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import webarchive.api.xml.TagName;
import webarchive.handler.Handlers;

/**
 * Xml controller on the server side. It controlls the loading of
 * webarchive-xml-files into dom-objects, validation and adding of new elements.
 * Each XmlHandler has responsibility for one xml-file.
 *
 * @author ccwelich
 */
//TODO javadoc
public class XmlHandler {

	private Document document;
	private Element dataNode;
	private final XmlIOHandler ioHandler;
	private AutoValidatingMode mode;

	public Document getDocument() {
		return document;
	}

	XmlHandler(XmlIOHandler ioHandler) throws SAXException  {
		// preconditions
		assert ioHandler != null;

		this.ioHandler = ioHandler;
		XmlConf conf = Handlers.get(XmlConf.class);
		this.mode = conf.getAutoValidatingMode();

		// build document
		ioHandler.lock();
		buildDocument();
		ioHandler.unlock();


	}

	public XmlIOHandler getIoHandler() {
		return ioHandler;
	}

	private void buildDocument() throws SAXException {
		try {
			document = ioHandler.buildDocument();
		} catch (ParserConfigurationException | IOException | SAXException ex) {
			Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null,
				ex);
		}
		System.out.println("XmlHandler: document = " + document);
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_BUILT_DOM) {
			validate();
		}
		this.dataNode = (Element) (document.getElementsByTagName(
			TagName.DATA_TAG.toString()).item(0));
		assert dataNode != null;	
	}

	public XmlEditor newEditor() {
		return new XmlEditor(document, dataNode);
	}

	private void validate() throws SAXException  {
		Validator v = ((XmlMethodFactory) Handlers.get(XmlMethodFactory.class)).
			newXmlValidator();
		try {
			v.validate(new DOMSource(document));
		} catch (IOException ex) {
			Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null,
				ex);
		}
	}

	public void addDataElement(DataElement dataElement) throws SAXException  {
		assert dataElement != null;

		ioHandler.lock();
		buildDocument();
		final String tagName = dataElement.getDataElement().getTagName();
		System.out.println("XmlHandler: tagName=" + tagName);
		System.out.println("XmlHandler: data = " + dataNode);


		// checking for duplicates
		NodeList dataNodes = dataNode.getChildNodes();
		final int length = dataNodes.getLength();
		for (int i = 0; i < length; i++) {
			if (dataNodes.item(i).getNodeName().equals(tagName)) {
				throw new IllegalArgumentException(
					"element " + tagName + " already exists");
			}
		}
		// append in sequence
		dataNode.appendChild(document.adoptNode(dataElement.getDataElement()));
		// validate
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_UPDATE) {
			validate();
		}
		try {
			// write to disk
			ioHandler.write(document);
		} catch (TransformerException ex) {
			Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null,
				ex);
		}
		ioHandler.unlock();
	}
}
