/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import webarchive.api.xml.TagName;
import webarchive.handler.Handlers;

/**
 *
 * @author ccwelich
 */
public class XmlHandlerTest {

	public File target;
	private XmlHandler instance;
	private XmlHandler resultinst;

	public XmlHandlerTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		XmlPrepare.builtHandlers();

	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		XmlPrepare.shutDownFactory();
	}

	@Before
	public void setUp() throws SAXException, ParserConfigurationException,
		IOException, TransformerConfigurationException {
		XmlPrepare.restoreFiles();

		XmlMethodFactory factory = XmlPrepare.factory;
		target = new File("test/xml/example.xml");
		instance = factory.newXmlHandler(new FileDescriptorMockup(target));
		resultinst = factory.newXmlHandler(new FileDescriptorMockup(new File(
			"test/xml/example.expected.xml")));
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getDocument method, of class XmlHandler.
	 */
	@Test
	public void testGetDocument() throws SAXException {
		System.out.println("getDocument");
		Document result = instance.getDocument();
		assertTrue(result != null);
	}

	/**
	 * Test of getIoHandler method, of class XmlHandler.
	 */
	@Test
	public void testGetIoHandler() {
		System.out.println("getIoHandler");
		XmlIOHandler result = instance.getIoHandler();
		assertTrue(result != null);
	}

	/**
	 * Test of newEditor method, of class XmlHandler.
	 */
	@Test
	public void testNewEditor() throws SAXException {
		System.out.println("newEditor");
		XmlEditor result = instance.newEditor();
		assertTrue(result != null);
	}

	/**
	 * Test of addDataElement method, of class XmlHandler.
	 */
	@Test
	public void testAddDataElement() throws Exception {
		System.out.println("addDataElement");
		XmlEditor editor = instance.newEditor();
		// create tags
		TagName tagName = new TagName("testData1");
		DataElement dataElement = editor.createDataElement(tagName);
		tagName = new TagName("content");
		Element element = editor.createElement(tagName);
		element.setTextContent("bla");
		dataElement.appendChild(element);
		instance.addDataElement(dataElement);
		tagName = new TagName("testData2");
		dataElement = editor.createDataElement(tagName);
		tagName = new TagName("content");
		element = editor.createElement(tagName);
		element.setTextContent("bla");
		dataElement.appendChild(element);
		instance.addDataElement(dataElement);
		
		instance = XmlPrepare.factory.newXmlHandler(new FileDescriptorMockup(
			target));
		NodeList expected = resultinst.getDocument().getElementsByTagName(TagName.DATA_TAG.
			getAbsoluteName()).item(0).getChildNodes();
		NodeList result = instance.getDocument().getElementsByTagName(TagName.DATA_TAG.
			getAbsoluteName()).item(0).getChildNodes();
		int i = 0, j = 0;
		while (i < result.getLength() && j < expected.getLength()) {
			while (result.item(i).getNodeName().equals("#text")) {
				i++;
			}
			while (expected.item(j).getNodeName().equals("#text")) {
				j++;
			}
			assertTrue(expected.item(j).isEqualNode(result.item(i)));
			i++; j++;
		}
		
	}
}
