/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author ccwelich
 */
public class XmlHandlerTest {

	public XmlHandlerTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
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
		XmlHandler instance = null;
		Document expResult = null;
		Document result = instance.getDocument();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getEditor method, of class XmlHandler.
	 */
	@Test
	public void testGetEditor() {
		System.out.println("getEditor");
		XmlHandler instance = null;
		XmlEditor expResult = null;
		XmlEditor result = instance.getEditor();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addDataElement method, of class XmlHandler.
	 */
	@Test
	public void testAddDataElement() {
		System.out.println("addDataElement");
		DataElement e = null;
		XmlHandler instance = null;
//		instance.addDataElement(e);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
