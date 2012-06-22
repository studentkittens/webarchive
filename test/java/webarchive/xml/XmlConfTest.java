/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import org.junit.*;
import static org.junit.Assert.*;
import webarchive.handler.Handlers;

/**
 *
 * @author ccwelich
 */
public class XmlConfTest {
	private XmlConf instance;
	
	public XmlConfTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		HandlerBuiltMockup.builtHandlers();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
		instance = (XmlConf) Handlers.get(XmlConf.class);
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of getAutoValidatingMode method, of class XmlConf.
	 */
	@Test
	public void testGetAutoValidatingMode() {
		System.out.println("getAutoValidatingMode");
		AutoValidatingMode expResult = AutoValidatingMode.ALWAYS;
		AutoValidatingMode result = instance.getAutoValidatingMode();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getSchemaPath method, of class XmlConf.
	 */
	@Test
	public void testGetSchemaPath() {
		System.out.println("getSchemaPath");
		File expResult = new File("test/xml/file.xsd");
		File result = instance.getSchemaPath();
		assertEquals(expResult, result);
	}
	/**
	 * Test of getDataTag method, of class XmlConf.
	 */
	@Test
	public void testGetDataTag() {
		System.out.println("getDataTag");
		String expResult = "data";
		String result = instance.getDataTag();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getNamespace method, of class XmlConf.
	 */
	@Test
	public void testGetNamespace() {
		System.out.println("getNamespace");
		String expResult = "http://www.hof-university.de/webarchive";
		String result = instance.getNamespace();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getPrefix method, of class XmlConf.
	 */
	@Test
	public void testGetPrefix() {
		System.out.println("getPrefix");
		String expResult = "wa";
		String result = instance.getPrefix();
		assertEquals(expResult, result);
	}
}
