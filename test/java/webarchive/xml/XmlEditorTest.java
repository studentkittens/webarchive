/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import static org.junit.Assert.*;
import org.junit.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import webarchive.api.xml.TagName;

/**
 *
 * @author ccwelich
 */
public class XmlEditorTest {

	private XmlEditor instance;

	public XmlEditorTest() {
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
		XmlPrepare.restoreFiles();

		XmlMethodFactory fac = XmlPrepare.factory;
		XmlHandler hdl = fac.newXmlHandler(new FileDescriptorMockup(
			XmlPrepare.XML_TARGET));
		instance = hdl.newEditor();
		instance.setClient(new ClientAdapterMockup(hdl));
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of createElement method, of class XmlEditor.
	 */
	@Test
	public void testCreateElement() {
		System.out.println("createElement");
		TagName tagName = new TagName("content");
		Element result = instance.createElement(tagName);
		assertEquals("wa:content", result.getTagName());
		result.setTextContent("bla");
		assertTrue(result.getTextContent().equals("bla"));
	}

	/**
	 * Test of createDataElement method, of class XmlEditor.
	 */
	@Test
	public void testCreateDataElement() {
		System.out.println("createDataElement");
		TagName tagName = new TagName("testData1");
		DataElement result = instance.createDataElement(tagName);
		assertEquals("wa:testData1", result.getDataElement().getTagName());
	}

	/**
	 * Test of add- and getDataElement method, of class XmlEditor.
	 */
	@Test
	public void testAddGetListDataElement() throws Exception {
		System.out.println("addDataElement");
		TagName tagNameDE = new TagName("testData1");
		DataElement dataElement = instance.createDataElement(tagNameDE);
		TagName tagNameCo = new TagName("content");
		Element element = instance.createElement(tagNameCo);
		element.setTextContent("bla");
		dataElement.appendChild(element);
		DataElement expected1 = instance.getDataElement(tagNameDE);
		assertTrue(expected1 == null);
		instance.addDataElement(dataElement);
		expected1 = instance.getDataElement(tagNameDE);
		assertFalse(expected1.canWrite());
		assertTrue(dataElement.isEqualNode(expected1));

		tagNameDE = new TagName("testData2");
		dataElement = instance.createDataElement(tagNameDE);
		element = instance.createElement(tagNameCo);
		element.setTextContent("bla");
		dataElement.appendChild(element);

		instance.addDataElement(dataElement);
		DataElement expected2 = instance.getDataElement(tagNameDE);
		assertTrue(dataElement.isEqualNode(expected2));

		List<webarchive.api.xml.DataElement> l = instance.listDataElements();
		assertEquals(expected1, l.get(0));
		assertEquals(expected2, l.get(1));

		//illegal access
		//dublicate
		boolean hasThrown = false;
		try {
			instance.addDataElement(dataElement);
		} catch (SAXException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		//null
		hasThrown = false;
		try {
			instance.addDataElement(null);
		} catch (SAXException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		//writeprotected
		hasThrown = false;
		dataElement = instance.getDataElement(tagNameDE);
		try {
			instance.addDataElement(dataElement);
		} catch (SAXException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		//not valid
		tagNameDE = new TagName("testData3"); // name not in xsd
		dataElement = instance.createDataElement(tagNameDE);
		element = instance.createElement(tagNameCo);
		element.setTextContent("bla");
		dataElement.appendChild(element);
		try {
			instance.addDataElement(dataElement);
		} catch (SAXException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);

		// element not in file
		TagName tagName = new TagName("testData4");
		assertTrue(instance.getDataElement(tagName) == null);

	}
}
