/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import static org.junit.Assert.*;
import org.junit.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import webarchive.api.xml.TagName;
import webarchive.handler.Handlers;

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
		XmlPrepare.killHandlers();
	}
	
	@Before
	public void setUp() throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {
				XmlPrepare.restoreFiles();

		XmlMethodFactory fac = Handlers.get(XmlMethodFactory.class);
		XmlHandler hdl = fac.newXmlHandler(new FileDescriptorMockup(XmlPrepare.XML_TARGET));
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
	public void testAddGetDataElement() throws Exception {
		System.out.println("addDataElement");
		TagName tagNameDE = new TagName("testData1");
		DataElement dataElement = instance.createDataElement(tagNameDE);
		TagName tagNameCo = new TagName("content");
		Element element = instance.createElement(tagNameCo);
		element.setTextContent("bla");
		dataElement.appendChild(element);
		DataElement expected = instance.getDataElement(tagNameDE);
		assertTrue(expected==null);
		instance.addDataElement(dataElement);
		expected = instance.getDataElement(tagNameDE);
		assertFalse(expected.canWrite());
		assertTrue(dataElement.isEqualNode(expected));

		tagNameDE = new TagName("testData2");
		dataElement = instance.createDataElement(tagNameDE);
		element = instance.createElement(tagNameCo);
		element.setTextContent("bla");
		dataElement.appendChild(element);
		
		instance.addDataElement(dataElement);
				expected = instance.getDataElement(tagNameDE);
				assertTrue(dataElement.isEqualNode(expected));


		
		//illegal access
		//dublicate
		boolean hasThrown = false;
		try {
			instance.addDataElement(dataElement);
		} catch (IllegalArgumentException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		//null
		hasThrown = false;
		try {
			instance.addDataElement(null);
		} catch (NullPointerException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		//writeprotected
		hasThrown = false;
		dataElement = instance.getDataElement(tagNameDE);
		try{
			instance.addDataElement(dataElement);
		} catch (IllegalArgumentException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		//not valid
		tagNameDE = new TagName("testData3"); // name not in xsd
		dataElement = instance.createDataElement(tagNameDE);
		element = instance.createElement(tagNameCo);
		element.setTextContent("bla");
		dataElement.appendChild(element);
		try{
			instance.addDataElement(dataElement);
		} catch (SAXException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		
		// element not in file
		TagName tagName = new TagName("testData4");
		assertTrue(instance.getDataElement(tagName)==null);
		
	}
	

	
}