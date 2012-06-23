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
import org.junit.*;
import static org.junit.Assert.*;
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
		HandlerBuiltMockup.builtHandlers();
			// restore init conditions
		File src = new File("test/xml/example.backup.xml"),
			dest = new File("test/xml/example.xml");
		Files.copy(src.toPath(), dest.toPath(),	StandardCopyOption.REPLACE_EXISTING);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {
		XmlMethodFactory fac = Handlers.get(XmlMethodFactory.class);
		XmlHandler hdl = fac.newXmlHandler(new FileDescriptorMockup(new File(
			"test/xml/example.xml")));
		instance = hdl.newEditor();
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
	 * Test of addDataElement method, of class XmlEditor.
	 */
	@Test
	public void testAddDataElement() throws Exception {
		//TODO inside client
		System.out.println("addDataElement");
		TagName tagName = new TagName("testData1");
		DataElement dataElement = instance.createDataElement(tagName);
		tagName = new TagName("content");
		Element element = instance.createElement(tagName);
		element.setTextContent("bla");
		dataElement.appendChild(element);
		instance.addDataElement(dataElement);
		tagName = new TagName("testData2");
		dataElement = instance.createDataElement(tagName);
		tagName = new TagName("content");
		element = instance.createElement(tagName);
		element.setTextContent("bla");
		dataElement.appendChild(element);
		instance.addDataElement(dataElement);
	}

	

	

	
}
