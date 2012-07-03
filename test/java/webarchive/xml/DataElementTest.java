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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import webarchive.api.xml.TagName;
import webarchive.handler.Handlers;

/**
 *
 * @author ccwelich
 */
public class DataElementTest {

	private DataElement instWP;
	private XmlEditor editor;
	private DataElement instW;

	public DataElementTest() {
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
	public void setUp() throws ParserConfigurationException, SAXException,
		IOException, TransformerConfigurationException {
		XmlMethodFactory fac = XmlPrepare.factory;
		XmlHandler hdl = fac.newXmlHandler(
			new FileDescriptorMockup(new File(
			"test/xml/example.expected.xml")));
		editor = hdl.newEditor();
		instWP = editor.getDataElement(new TagName("testData1"));
		instW = editor.createDataElement(new TagName("wprotected"));

	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of appendChild method, of class DataElement.
	 */
	@Test
	public void testAppendChild() {
		System.out.println("appendChild");
		Node node = editor.createElement(new TagName("bla"));
		Node expResult = node;
		Node result = instW.appendChild(node);
		assertEquals(expResult, result);
		// write protected
		boolean hasThrown = false;
		try {
			DataElement wprotected = editor.getDataElement(new TagName(
				"testData1"));
			wprotected.appendChild(editor.createElement(new TagName("someNode")));
		} catch (IllegalArgumentException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	/**
	 * Test of canWrite method, of class DataElement.
	 */
	@Test
	public void testCanWrite() {
		System.out.println("canWrite");
		assertFalse(instWP.canWrite());
		assertTrue(instW.canWrite());
	}

	/**
	 * Test of getDataElement method, of class DataElement.
	 */
	@Test
	public void testGetDataElement() {
		System.out.println("getDataElement");
		Element result = instWP.getDataElement();
		assertTrue(result != null);
	}

	/**
	 * Test of getChildNodes method, of class DataElement.
	 */
	@Test
	public void testGetChildNodes() {
		System.out.println("getChildNodes");
		NodeList result = instWP.getChildNodes();
		assertTrue(result != null);
	}

	/**
	 * Test of getElementsByTagName method, of class DataElement.
	 */
	@Test
	public void testGetElementsByTagName() {
		System.out.println("getElementsByTagName");
		NodeList result = instWP.getElementsByTagName(new TagName("content"));
		assertTrue(result.getLength()==1);
		result = instWP.getElementsByTagName(new TagName("testData3"));
		assertTrue(result.getLength() == 0);
	}
}
