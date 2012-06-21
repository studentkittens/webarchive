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
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import webarchive.xml.DataElement;
import webarchive.xml.XmlConf;
import webarchive.xml.XmlEditor;
import webarchive.xml.XmlHandler;

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

		// restore init conditions
		File src = new File("test/xml/example.backup.xml"),
			dest = new File("test/xml/example.xml");
		Files.copy(src.toPath(), dest.toPath(),
			StandardCopyOption.REPLACE_EXISTING);
		// xml file to read
		FileDescriptor fd = new FileDescriptor(null) {

			@Override
			public File getAbsolutePath() {
				return new File("test/xml/example.xml");
			}
		};
		System.out.println(fd.getAbsolutePath());
		if (!fd.getAbsolutePath().exists()) {
			System.exit(-1);
		}

		// init xml helper classes

		XmlConf conf = new XmlConf();
		conf.setAutoValidatingMode(XmlHandler.AutoValidatingMode.AFTER_BUILT_DOM);
		final File xsdFile = new File("test/xml/file.xsd");
		System.out.println("xsd: " + xsdFile + ", exists" + xsdFile.exists());
		Scanner scan = new Scanner(xsdFile);
		while (scan.hasNextLine()) {
			System.out.println(scan.nextLine());
		}
		conf.setSchemaPath(xsdFile);
		System.out.println("mode: " + conf.getAutoValidatingMode());

		// init XmlHandler
		System.out.println("start XmlHandler");
		XmlHandler h = new XmlHandler(fd, conf);
		h.getIoHandler().setDebug(false);
		// xml will be validated, since file is not manipulated
		System.out.println("validate");

		// start XmlEditor
		XmlEditor edit = h.newEditor();

		// get data element and add some content

		DataElement de1 = edit.createDataElement("testData1");
		Element sub = edit.createElement("content");
		sub.setTextContent("bla");
		de1.appendChild(sub);

		DataElement de2 = edit.createDataElement("testData2");
		Element sub2 = edit.createElement("content");
		sub2.setTextContent("bla");
		de2.appendChild(sub2);

		// add the DataElement to the XML File
		System.out.println("write data:");
		System.out.println("-----------");
		h.addDataElement(de1);
		h.addDataElement(de2);

//			XmlPrinter.printNodes("", h.getDocument());


		// result will be printed in console...

//		XmlPrinter.printNodes("", h.newEditor().getDataElement("testData").getDataElement());
//		h.addDataElement(de); // throws exception, since de element is already in XML
		//		XmlPrinter.printNodes("", h.getDocument());

	}
}
