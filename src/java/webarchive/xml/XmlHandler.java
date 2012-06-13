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

import webarchive.handler.Handler;

/**
 *
 * @author ccwelich
 */
//TODO c2 finish implementation
//TODO tests
//TODO javadoc
public class XmlHandler {

	private final AutoValidatingMode mode;

	public enum AutoValidatingMode {

		NEVER, ALWAYS, AFTER_UPDATE, AFTER_BUILT_DOM;
	}
	private final XmlConf conf;
	private Document document;
	private final Element data;
	private final XmlValidator validator;
	private final File xmlPath;
	private final XmlErrorHandler err;
	private final XmlDomWriter writer;

	public Document getDocument() {
		return document;
	}

	public XmlHandler(File xmlPath, XmlErrorHandler err, XmlConf conf,
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
		this.mode = conf.getAutoValidatingMode();
		this.writer = writer;
		this.validator = validator;
		this.data = (Element) (document.getElementsByTagName(conf.getDataTag()).
			item(0));
		this.conf = conf;
	}

	public void rebuildDocument() throws ParserConfigurationException,
		IOException, SAXException {
		buildDocument();

	}

	private void buildDocument() throws ParserConfigurationException,
		IOException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setExpandEntityReferences(true);
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setErrorHandler(err);
		document = db.parse(xmlPath);
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
		SAXException, IOException, TransformerException {
		assert e != null;

		final String tagName = e.getDataElement().getTagName();
		// checking for duplicate
		if (data.getElementsByTagName(tagName).getLength() > 0) {
			throw new IllegalArgumentException(
				"element " + tagName + " already exists");
		}
		// append in sequence
		data.appendChild(e.getDataElement());
		// validate
		if (mode == AutoValidatingMode.ALWAYS || mode == AutoValidatingMode.AFTER_UPDATE) {
			validate();
		}
		// write to disk
		writer.write(document);
	}
}
