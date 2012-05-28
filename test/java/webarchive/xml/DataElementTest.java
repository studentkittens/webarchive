/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ccwelich
 */
public class DataElementTest {
	
	public DataElementTest() {
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
	 * Test of getTextContent method, of class DataElement.
	 */
	@Test
	public void testGetTextContent() {
		System.out.println("getTextContent");
		DataElement instance = null;
		String expResult = "";
		String result = instance.getTextContent();
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
