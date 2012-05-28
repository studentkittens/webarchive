/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.model;

import java.io.File;
import java.net.URL;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author ccwelich
 */
public class MetaDataTest {
	
	public MetaDataTest() {
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
	 * Test of getCommitTag method, of class MetaData.
	 */
	@Test
	public void testGetCommitTag() {
		System.out.println("getCommitTag");
		MetaData instance = null;
		CommitTag expResult = null;
		CommitTag result = instance.getCommitTag();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCreateTime method, of class MetaData.
	 */
	@Test
	public void testGetCreateTime() {
		System.out.println("getCreateTime");
		MetaData instance = null;
		TimeStamp expResult = null;
		TimeStamp result = instance.getCreateTime();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class MetaData.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		MetaData instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPath method, of class MetaData.
	 */
	@Test
	public void testGetPath() {
		System.out.println("getPath");
		MetaData instance = null;
		File expResult = null;
		File result = instance.getPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getUrl method, of class MetaData.
	 */
	@Test
	public void testGetUrl() {
		System.out.println("getUrl");
		MetaData instance = null;
		URL expResult = null;
		URL result = instance.getUrl();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMimeType method, of class MetaData.
	 */
	@Test
	public void testGetMimeType() {
		System.out.println("getMimeType");
		MetaData instance = null;
		String expResult = "";
		String result = instance.getMimeType();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTitle method, of class MetaData.
	 */
	@Test
	public void testGetTitle() {
		System.out.println("getTitle");
		MetaData instance = null;
		String expResult = "";
		String result = instance.getTitle();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
