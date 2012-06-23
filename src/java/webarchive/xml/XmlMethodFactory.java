package webarchive.xml;

import java.io.IOException;
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
import webarchive.server.LockHandlerImpl;
import webarchive.server.Server;
import webarchive.transfer.FileDescriptor;

/**
 * Config class for all Xml-related classes.
 *
 * @author ccwelich
 */
public class XmlMethodFactory extends Handler {
	private final DocumentBuilderFactory documentBuilderFactory;
	private ErrorHandler xmlErrorHandler;
	private Schema schema;
	private final TransformerFactory transformerFactory;
	private LockHandler locker;

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
	
	
	public XmlHandler newXmlHandler(FileDescriptor xmlPath) throws SAXException {
		XmlIOHandler ioHandler = null;
		ioHandler = new XmlIOHandler(xmlPath, newTransformer(), locker);
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
	public ErrorHandler getXmlErrorHandler() {
		return xmlErrorHandler;
	}
	/**
	 * set xmlErrorHandler
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
	public Validator newXmlValidator() {
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

	
}
