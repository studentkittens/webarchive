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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import webarchive.api.xml.TagName;
import webarchive.handler.Handlers;
import webarchive.transfer.FileDescriptor;

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
	public void setUp() throws SAXException, ParserConfigurationException, IOException, TransformerConfigurationException {
		XmlMethodFactory factory = Handlers.get(XmlMethodFactory.class);
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
	public void testGetDocument() {
		System.out.println("getDocument");
		Document result = instance.getDocument();
		assertTrue(result!=null);
	}

	/**
	 * Test of getIoHandler method, of class XmlHandler.
	 */
	@Test
	public void testGetIoHandler() {
		System.out.println("getIoHandler");
		XmlIOHandler result = instance.getIoHandler();
		assertTrue(result!=null);
	}

	/**
	 * Test of newEditor method, of class XmlHandler.
	 */
	@Test
	public void testNewEditor() {
		System.out.println("newEditor");
		XmlEditor result = instance.newEditor();
		assertTrue(result!=null);
	}

	/**
	 * Test of addDataElement method, of class XmlHandler.
	 */
	@Test
	public void testAddDataElement() throws Exception {
		System.out.println("addDataElement");
		XmlEditor editor = instance.newEditor();
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
		instance = Handlers.get(XmlMethodFactory.class).newXmlHandler(new FileDescriptorMockup(target));
		NodeList expected = resultinst.getDocument().getElementsByTagName(TagName.DATA_TAG.getName()).item(0).getChildNodes();
		NodeList result = instance.getDocument().getElementsByTagName(TagName.DATA_TAG.getName()).item(0).getChildNodes();
		int i=0, j=0;
		while(i < result.getLength() && j<expected.getLength()) {
			while(result.item(i).getNodeName().equals("#text")) i++;
			while(result.item(j).getNodeName().equals("#text")) j++;

			assertTrue(expected.item(i).isEqualNode(result.item(i)));
		}
		
	}
}
