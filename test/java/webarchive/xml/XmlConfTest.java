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
		XmlPrepare.builtHandlers();
		XmlPrepare.restoreFiles();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		XmlPrepare.killHandlers();
	}
	
	@Before
	public void setUp() {
		instance = Handlers.get(XmlConf.class);
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

}
