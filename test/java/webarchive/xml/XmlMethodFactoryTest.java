/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.validation.Validator;
import org.junit.*;
import static org.junit.Assert.*;
import org.xml.sax.ErrorHandler;
import webarchive.handler.Handlers;
import webarchive.transfer.FileDescriptor;

/**
 *
 * @author ccwelich
 */
public class XmlMethodFactoryTest {

	private XmlMethodFactory instance;
	private FileDescriptor xmlPath;

	public XmlMethodFactoryTest() {
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
		instance = XmlPrepare.factory;
		xmlPath = new FileDescriptorMockup(new File(
			"test/xml/example.backup.xml"));
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of newXmlHandler method, of class XmlMethodFactory.
	 */
	@Test
	public void testNewHandler() throws Exception {
		System.out.println("newHandler");
		XmlHandler result = instance.newXmlHandler(xmlPath);
		assertTrue(result != null);
		boolean thrown = false;
		try {
			instance.newXmlHandler(null);
		} catch (AssertionError e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	/**
	 * Test of newTransformer method, of class XmlMethodFactory.
	 */
	@Test
	public void testNewTransformer() throws Exception {
		System.out.println("newTransformer");
		Transformer result = instance.newTransformer();
		assertTrue(result != null);
	}

	/**
	 * Test of newDocumentBuilder method, of class XmlMethodFactory.
	 */
	@Test
	public void testNewDocumentBuilder() throws Exception {
		System.out.println("newDocumentBuilder");
		DocumentBuilder result = instance.newDocumentBuilder();
		assertTrue(result != null);
	}

	/**
	 * Test of getErrorHandler method, of class XmlMethodFactory.
	 */
	@Test
	public void testSetGetXmlErrorHandler() {
		System.out.println("getXmlErrorHandler");
		ErrorHandler result = instance.getErrorHandler();
		assertTrue(result == null); //default

		System.out.println("setXmlErrorHandler");
		final XmlErrorHandler xmlErrorHandler = new XmlErrorHandler();
		instance.setXmlErrorHandler(xmlErrorHandler);
		result = instance.getErrorHandler();
		assertEquals(xmlErrorHandler, result);
		instance.setXmlErrorHandler(null);
		result = instance.getErrorHandler();

		assertTrue(result == null);
	}

	/**
	 * Test of newXmlValidator method, of class XmlMethodFactory.
	 */
	@Test
	public void testNewXmlValidator() {
		System.out.println("newXmlValidator");
		Validator result = instance.newXmlValidator();
		assertTrue(result!=null);
	}
}
