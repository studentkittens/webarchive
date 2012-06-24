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
 * Top-Level Xml-controller on the server side. The usage is asserted as one
 * XmlHandler for each client and operation. operations are: {@link #addDataElement(webarchive.xml.DataElement)
 * }, {@link #newEditor() } </br> XmlHander also controlls:
 * <ul>
 *	<li>building of w3c.dom.Documents from xml-files.</li>
 *	<li>triggering validation, see {@link AutoValidatingMode}</li>
 *	<li>adding new elements and saving to disk, lock operations inclusive</li>
 *	<li>exception handling</li> 
 * </ul> Each XmlHandler has responsibility for one
 * xml-file. XmlHandlers are usually accessed by {@link XmlMethodFactory}
 *
 * @author ccwelich
 */
public class XmlHandler {

	private Document document;
	private Element dataNode;
	private final XmlIOHandler ioHandler;
	private AutoValidatingMode mode;

	/**
	 * create a new XmlHandler via a XmlIOHandler.
	 *
	 * @param ioHandler encapsules the build, write and lock operations.
	 * @throws SAXException in case of invalid xml
	 */
	XmlHandler(XmlIOHandler ioHandler) throws SAXException {
		// preconditions
		assert ioHandler != null;

		this.ioHandler = ioHandler;
		XmlConf conf = Handlers.get(XmlConf.class);
		this.mode = conf.getAutoValidatingMode();
		//lazy document binding
		document = null;
	}

	private void ensureDocument() throws SAXException {
		if (document == null) {
			ioHandler.lock();
			buildDocument();
			ioHandler.unlock();
		}
	}

	/**
	 * @return the document
	 * @throws SAXException in case of invalid xml
	 */
	public Document getDocument() throws SAXException {
		ensureDocument();
		return document;
	}

	/**
	 *
	 * @return the XMlIOHandler used in this XmlHandler
	 */
	public XmlIOHandler getIoHandler() {
		return ioHandler;
	}

	private void buildDocument() throws SAXException {
		try {
			document = ioHandler.buildDocument();
		} catch (ParserConfigurationException |
			IOException |
			SAXException ex) {
			Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null,
				ex);
		}
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_BUILT_DOM) {
			validate();
		}
		this.dataNode = (Element) (document.getElementsByTagName(
			TagName.DATA_TAG.toString()).item(0));
		assert document != null;
		assert dataNode != null;
	}

	/**
	 * builds a new XmlEditor for clients
	 *
	 * @return the new Editor
	 * @throws SAXException in case of invalid xml
	 */
	public XmlEditor newEditor() throws SAXException {
		ensureDocument();
		return new XmlEditor(document, dataNode);
	}

	private void validate() throws SAXException {
		Validator v = ((XmlMethodFactory) Handlers.get(XmlMethodFactory.class)).
			newXmlValidator();
		try {
			v.validate(new DOMSource(document));
		} catch (IOException ex) {
			Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null,
				ex);
		}
	}

	/**
	 * adds dataElement to the data-node
	 *
	 * @param dataElement add this dataElement
	 * @throws SAXException in case of invalid xml, according to
	 * autoValidatingMode
	 * @throws IllegalArgumentException if there is already a DataElement where
	 * tagName is equal (No dublicates)
	 */
	public void addDataElement(DataElement dataElement) throws SAXException,
		IllegalArgumentException {
		assert dataElement != null;
		final String tagName = dataElement.getDataElement().getTagName();

		// BEGIN CRITICAL SECTION
		ioHandler.lock();
		buildDocument(); // rebuild in case of changes

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

		try {
			// write to disk
			ioHandler.write(document);
		} catch (TransformerException ex) {
			Logger.getLogger(XmlHandler.class.getName()).log(Level.SEVERE, null,
				ex);
		}
		ioHandler.unlock();
		// END CRITICAL SECTION

		// validate
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_UPDATE) {
			validate();
		}
	}
	
}
