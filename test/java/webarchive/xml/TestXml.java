/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author ccwelich
 */
public class TestXml {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws
		TransformerConfigurationException, IllegalArgumentException,
		SAXException, IOException, TransformerException,
		ParserConfigurationException {

		// xml file to read
		File f = new File("test/java/webarchive/xml/test.xml");
		System.out.println(f.getAbsoluteFile());
		if (!f.exists()) {
			System.exit(-1);
		}

		// init xml helper classes
		final XmlErrorHandler err = new XmlErrorHandler();
		final XmlConf conf = new XmlConf();
		conf.setAutoValidatingMode(XmlHandler.AutoValidatingMode.AFTER_BUILT_DOM);
		XmlValidator validator = new XmlValidator(conf, err);
		XmlDomWriter writer = new XmlDomWriter(System.out);

		// init XmlHandler
		System.out.println("start XmlHandler");
		XmlHandler h = new XmlHandler(f, err, conf, validator, writer);
		// xml will be validated, since file is not manipulated
		System.out.println("validate");
		
		// start XmlEditor
		XmlEditor edit = h.getEditor();
		// get data element and add some content
		DataElement de = edit.createDataElement("data1");
		Element sub = edit.createElement("subdata");
		sub.setTextContent("bla");
		de.appendChild(sub);
		// add the DataElement to the XML File
		System.out.println("write data:");
		System.out.println("-----------");
		h.addDataElement(de); // if autovalidating is on throws exception, since schema is not fitted to added elements
	
		
		// result will be printed in console...
		
//		XmlPrinter.printNodes("", h.getEditor().getDataElement("data1").getDataElement());
//		h.addDataElement(de); // throws exception, since de element is already in XML
		//		XmlPrinter.printNodes("", h.getDocument());

	}
}
