package webarchive.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import webarchive.handler.Handler;
import webarchive.handler.Handlers;
import webarchive.server.LockHandler;
import webarchive.transfer.FileDescriptor;

/**
 * Factory class for all Xml-Helper and controllers.
 *
 * @author ccwelich
 */
public class XmlMethodFactory extends Handler {
	private final DocumentBuilderFactory documentBuilderFactory;
	private ErrorHandler xmlErrorHandler;
	private Schema schema;
	private final TransformerFactory transformerFactory;
	private LockHandler locker;
	/**
	 * create new XmlMethodFactory
	 * @param locker used for file locking {@link webarchive.server.LockHandler}
	 */
	public XmlMethodFactory(LockHandler locker) {
		xmlErrorHandler=null; // not used
		buildSchema();
		//build final documentBuilderFactory
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setIgnoringComments(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		documentBuilderFactory.setExpandEntityReferences(true);
		documentBuilderFactory.setNamespaceAware(true);
		//build transformer factory
		transformerFactory = TransformerFactory.newInstance();
		this.locker = locker;
	}
	
	/**
	 * create new XmlHandler
	 * @param xmlPath path of xml-file
	 * @return new XmlHandler
	 * @throws SAXException 
	 */
	public XmlHandler newXmlHandler(FileDescriptor xmlPath) throws SAXException {
		XmlIOHandler ioHandler = newXmlIOHandler(xmlPath);
		return new XmlHandler(ioHandler);
	}
	
	Transformer newTransformer() {
		Transformer transformer = null;
		synchronized(transformerFactory) {
			try {
				transformer = transformerFactory.newTransformer();
			} catch (TransformerConfigurationException ex) {
				Logger.getLogger(XmlMethodFactory.class.getName()).
					log(Level.SEVERE, null, ex);
			}
		}
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		return transformer;
	}
	
	DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilder rc;
		synchronized(documentBuilderFactory){
			rc = documentBuilderFactory.newDocumentBuilder();
		}
		assert rc!=null;
		rc.setErrorHandler(xmlErrorHandler);
		return rc;
	}

	/**
	 * get XmlErrorHandler
	 * @return xmlErroHandler, default null
	 */
	ErrorHandler getXmlErrorHandler() {
		return xmlErrorHandler;
	}
	/**
	 * set ErrorHandler.
	 * the error Handler is null by default.
	 * @param xmlErrorHandler 
	 */
	public void setXmlErrorHandler(ErrorHandler xmlErrorHandler) {
		this.xmlErrorHandler = xmlErrorHandler;
		buildSchema();
	}
	
	/**
	 * get xmlValidator
	 * @return xmlValidator
	 */
	Validator newXmlValidator() {
		// schema is threadsafe
		Validator v = schema.newValidator();
		v.setErrorHandler(xmlErrorHandler);
		return v;
	}

	private void buildSchema()  {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		factory.setErrorHandler(xmlErrorHandler);
		XmlConf conf = Handlers.get(XmlConf.class);
		try {
			schema = factory.newSchema(conf.getSchemaPath());
		} catch (SAXException ex) {
			Logger.getLogger(XmlMethodFactory.class.getName()).
				log(Level.SEVERE, null, ex);
		}
	}

	XmlIOHandler newXmlIOHandler(FileDescriptor xmlPath) {
		return new XmlIOHandler(xmlPath, newTransformer(), locker);
	}

	
}