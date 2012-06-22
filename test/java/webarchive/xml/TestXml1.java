/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import webarchive.transfer.FileDescriptor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import webarchive.xml.DataElement;
import webarchive.xml.DataElement;
import webarchive.xml.XmlConf;
import webarchive.xml.XmlConf;
import webarchive.xml.XmlEditor;
import webarchive.xml.XmlEditor;
import webarchive.xml.XmlHandler;
import webarchive.xml.XmlHandler;

/**
 *
 * @author ccwelich
 */
public class TestXml1 {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws
		TransformerConfigurationException, IllegalArgumentException,
		SAXException, IOException, TransformerException,
		ParserConfigurationException {

//		// restore init conditions
//		File src = new File("test/xml/example.backup.xml"),
//			dest = new File("test/xml/example.xml");
//		Files.copy(src.toPath(), dest.toPath(),
//			StandardCopyOption.REPLACE_EXISTING);
		// xml file to read
		FileDescriptor fd = new FileDescriptor(null) {

			@Override
			public File getAbsolutePath() {
				return new File("test/xml/example1.xml");
			}
		};
		System.out.println(fd.getAbsolutePath());
		if (!fd.getAbsolutePath().exists()) {
			System.exit(-1);
		}

		// init xml helper classes

		XmlConf conf = new XmlConf();
		conf.setAutoValidatingMode(XmlHandler.AutoValidatingMode.ALWAYS);
		final File xsdFile = new File("test/xml/file1.xsd");
		System.out.println("xsd: " + xsdFile + ", exists" + xsdFile.exists());
		
		conf.setSchemaPath(xsdFile);
		XmlValidator v = conf.getXmlValidator();
		XmlIOHandler io = new XmlIOHandler(conf, fd);
		final Document doc = io.buildDocument();
		//XmlDomPrinter.printNodes("", doc);
		//v.validate(doc);
		
		

		// start XmlEditor
//		XmlEditor edit = h.newEditor();

//		// create data elements and add some content
//		DataElement de2 = edit.createDataElement("testData2");
//		Element sub2 = edit.createElement("content");
//		sub2.setTextContent("bla");
//		de2.appendChild(sub2);
//		
//		DataElement de1 = edit.createDataElement("testData1");
//		Element sub = edit.createElement("content");
//		sub.setTextContent("bla");
//		de1.appendChild(sub);
//
//		
//
//		// add the DataElement to the XML File
//		System.out.println("write data:");
//		System.out.println("-----------");
//		h.addDataElement(de1);
//		h.addDataElement(de2);

//			XmlPrinter.printNodes("", h.getDocument());


		// result will be printed in console...

//		XmlPrinter.printNodes("", h.newEditor().getDataElement("testData").getDataElement());
//		h.addDataElement(de); // throws exception, since de element is already in XML
		//		XmlPrinter.printNodes("", h.getDocument());

	}
}
