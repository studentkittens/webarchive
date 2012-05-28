package webarchive.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author ccwelich
 */
//TODO Tests
//TODO javadoc

public class XmlErrorHandler implements ErrorHandler {
	
	public XmlErrorHandler() {
	}
	
	@Override
	public void warning(SAXParseException saxpe) throws SAXException {
		Logger.getLogger(XmlErrorHandler.class.getName()).log(
			Level.WARNING, null, saxpe);
	}
	
	@Override
	public void error(SAXParseException saxpe) throws SAXException {
		Logger.getLogger(XmlErrorHandler.class.getName()).log(
			Level.SEVERE, null, saxpe);
		
	}
	
	@Override
	public void fatalError(SAXParseException saxpe) throws SAXException {
		Logger.getLogger(XmlErrorHandler.class.getName()).log(
			Level.SEVERE, null, saxpe);
		
	}
}
