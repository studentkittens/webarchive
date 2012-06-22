/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import sun.org.mozilla.javascript.ast.XmlMemberGet;

/**
 *
 * @author ccwelich
 */
public class TestValidator {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
		String xsd = "test/xml/file.xsd",
			xml = "test/xml/example1.xml";

        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        File schemaLocation = new File(xsd);
        Schema schema = factory.newSchema(schemaLocation);
        Validator validator = schema.newValidator();
        //Source source = new StreamSource(xml);
		XmlConf conf = new XmlConf();
		XmlMethodFactory.init(conf, new LockHandlerMockup());
		Document doc = XmlMethodFactory.getInstance().newDocumentBuilder().parse(new File(xml));
		Source source = new DOMSource(doc);
        try {
            validator.validate(source);
            System.out.println(xml + " is valid.");
        }
        catch (SAXException ex) {
            System.out.println(xml + " is not valid because ");
            System.out.println(ex.getMessage());
        }  
        
    }

}
	
