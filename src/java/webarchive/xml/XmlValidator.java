
package webarchive.xml;

import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author ccwelich
 */
//TODO tests
//TODO javadoc
public class XmlValidator {
	private final Schema schema;
	private ErrorHandler err;

	public XmlValidator(XmlConf conf, XmlErrorHandler err) throws SAXException {
		// preconditions
		assert conf != null;
		assert err != null;
		// build schema 
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		schema = factory.newSchema(conf.getSchemaPath());
		this.err = err;
		System.out.println("schema="+schema);
	}
	public void validate(Document doc) throws SAXException, IOException {
		Validator v = schema.newValidator();
		v.setErrorHandler(err);
		v.validate(new DOMSource(doc));

	}

}
