package webarchive.xml;

import java.io.IOException;
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
import webarchive.server.LockHandler;
import webarchive.server.LockHandlerImpl;
import webarchive.server.Server;
import webarchive.transfer.FileDescriptor;

/**
 * Config class for all Xml-related classes.
 *
 * @author ccwelich
 */
//TODO tests
public class XmlMethodFactory extends Handler {
	// static -- singleton
	private static XmlMethodFactory instance;
	
	public static XmlMethodFactory getInstance() {
		assert instance != null : "call init() first";
		return instance;
	}
	
	public static void init(XmlConf conf, LockHandler locker) throws SAXException { 
		assert conf != null;
		
		instance = new XmlMethodFactory(conf, locker);
	}
	// member
	private final DocumentBuilderFactory documentBuilderFactory;
	private ErrorHandler xmlErrorHandler;
	private Schema schema;
	private XmlConf conf;
	private final TransformerFactory transformerFactory;
	private LockHandlerImpl locker;

	public XmlConf getConf() {
		return conf;
	}

	
	private XmlMethodFactory(XmlConf conf, LockHandler locker) throws SAXException {
		this.conf = conf;
		buildSchema();
		xmlErrorHandler=null;
		//build final documentBuilderFactory
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setIgnoringComments(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		documentBuilderFactory.setExpandEntityReferences(true);
		documentBuilderFactory.setNamespaceAware(true);
		//build transformer factory
		transformerFactory = TransformerFactory.newInstance();
		//build default locker
		this.locker = (LockHandlerImpl)Server.getInstance().getHandlers().get("LockHandler");
	}
	
	
	public XmlHandler newHandler(FileDescriptor xmlPath) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {
		XmlIOHandler ioHandler = new XmlIOHandler(xmlPath, newTransformer(), locker);
		return new XmlHandler(ioHandler);
		
	}
	
	Transformer newTransformer() throws TransformerConfigurationException {
		Transformer transformer;
		synchronized(transformerFactory) {
			transformer = transformerFactory.newTransformer();
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


	private void buildSchema() throws SAXException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		schema = factory.newSchema(conf.getSchemaPath());
	}

	
}
