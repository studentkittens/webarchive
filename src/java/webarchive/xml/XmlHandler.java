package webarchive.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import webarchive.transfer.FileDescriptor;

/**
 * Xml controller on the server side.
 * It controlls the loading of webarchive-xml-files into dom-objects,
 * validation and adding of new elements.
 * Each XmlHandler has responsibility for one xml-file.
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
	private final Element data;
	private final XmlValidator validator;
	private final FileDescriptor xmlPath;
	private final XmlErrorHandler err;
	private final XmlDomWriter writer;

	public Document getDocument() {
		return document;
	}

	public XmlHandler(FileDescriptor xmlPath, XmlErrorHandler err, XmlConf conf,
		XmlValidator validator, XmlDomWriter writer) throws
		ParserConfigurationException, SAXException, IOException {
		// TODO FileHandler access
		// preconditions
		assert err != null;
		assert xmlPath != null;
		assert conf != null;
		assert validator != null;
		assert writer != null;
		// set ErrorHandler and Path
		this.err = err;
		this.xmlPath = xmlPath;
		
		// build document
		document = null;
		buildDocument();

		// other members
		this.writer = writer;
		this.validator = validator;
		this.data = (Element) (document.getElementsByTagName(conf.getDataTag()).
			item(0));
		this.conf = conf;
	}

	public void rebuildDocument() throws ParserConfigurationException,
		IOException, SAXException {
				//TODO lock

		buildDocument();
		//TODO unlock

	}

	private void buildDocument() throws ParserConfigurationException,
		IOException, SAXException {
	
		DocumentBuilder db = conf.getDocumentBuilderFactory().newDocumentBuilder();
		db.setErrorHandler(err);
		document = db.parse(xmlPath.getAbsolutePath());
		AutoValidatingMode mode = conf.getAutoValidatingMode();
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_BUILT_DOM) {
			validate();
		}
	}

	public XmlEditor getEditor() {
		return new XmlEditor(document, conf);
	}

	public void validate() throws SAXException, IOException {
		validator.validate(document);
	}

	public void addDataElement(DataElement e) throws IllegalArgumentException,
		SAXException, IOException, TransformerException, ParserConfigurationException {
		assert e != null;
		//TODO lock
		buildDocument();
		final String tagName = e.getDataElement().getTagName();
		// checking for duplicate
		if (data.getElementsByTagName(tagName).getLength() > 0) {
			throw new IllegalArgumentException(
				"element " + tagName + " already exists");
		}
		// append in sequence
		data.appendChild(e.getDataElement());
		// validate
		AutoValidatingMode mode = conf.getAutoValidatingMode();
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_UPDATE) {
			validate();
		}
		// write to disk
		writer.write(document);
		//TODO unlock
	}
}
