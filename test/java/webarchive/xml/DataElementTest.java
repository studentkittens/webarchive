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
	private DataElement instance;
	
	public DataElementTest() {
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
		XmlHandler hdl = fac.newXmlHandler(
			new FileDescriptorMockup(new File(
			"test/xml/example.expected.xml")));
		XmlEditor editor = hdl.newEditor();
		instance = editor.getDataElement(new TagName("testData1"));
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
		Node node = null;
		DataElement instance = null;
		Node expResult = null;
		Node result = instance.appendChild(node);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of canWrite method, of class DataElement.
	 */
	@Test
	public void testCanWrite() {
		System.out.println("canWrite");
		DataElement instance = null;
		boolean expResult = false;
		boolean result = instance.canWrite();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDataElement method, of class DataElement.
	 */
	@Test
	public void testGetDataElement() {
		System.out.println("getDataElement");
		DataElement instance = null;
		Element expResult = null;
		Element result = instance.getDataElement();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getChildNodes method, of class DataElement.
	 */
	@Test
	public void testGetChildNodes() {
		System.out.println("getChildNodes");
		DataElement instance = null;
		NodeList expResult = null;
		NodeList result = instance.getChildNodes();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getElementsByTagName method, of class DataElement.
	 */
	@Test
	public void testGetElementsByTagName() {
		System.out.println("getElementsByTagName");
		String string = "";
		DataElement instance = null;
		NodeList expResult = null;
		NodeList result = instance.getElementsByTagName(string);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
