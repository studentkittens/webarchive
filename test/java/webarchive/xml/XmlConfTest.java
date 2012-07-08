/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import webarchive.handler.Handlers;
import webarchive.init.ConfigHandler;

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
		XmlPrepare.shutDownFactory();
	}
	
	@Before
	public void setUp() {
		instance = XmlPrepare.xmlConf;
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
	public void testGetSchemaPath() throws ParserConfigurationException, SAXException, IOException {
		System.out.println("getSchemaPath");
		File expResult = new File("test/xml/file.xsd");
		assertTrue(expResult.exists());
		File result = instance.getSchemaPath();
		assertEquals(expResult, result);
		ConfigHandler conf = new ConfigHandler(new File("test/java/webarchive/xml/mockup.conf.xml"));
		instance = new XmlConf(conf);
		expResult = new File("/tmp/archive/xml/file.xsd");
		result = instance.getSchemaPath();
		assertEquals(expResult, result);

	}

}
