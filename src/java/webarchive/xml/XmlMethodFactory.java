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
//TODO tests
public class XmlMethodFactory extends Handler {
	private final DocumentBuilderFactory documentBuilderFactory;
	private ErrorHandler xmlErrorHandler;
	private Schema schema;
	private final TransformerFactory transformerFactory;
	private LockHandlerImpl locker;

	private XmlMethodFactory(LockHandler locker) throws SAXException {
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
		XmlConf conf = (XmlConf) Handlers.get(XmlConf.class);
		schema = factory.newSchema(conf.getSchemaPath());
	}

	
}
