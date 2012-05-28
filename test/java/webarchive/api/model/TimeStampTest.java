/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.model;

import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author ccwelich
 */
public class TimeStampTest {
	
	public TimeStampTest() {
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
	 * Test of toString method, of class TimeStamp.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		TimeStamp instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDate method, of class TimeStamp.
	 */
	@Test
	public void testGetDate() {
		System.out.println("getDate");
		TimeStamp instance = null;
		Date expResult = null;
		Date result = instance.getDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getXmlFormat method, of class TimeStamp.
	 */
	@Test
	public void testGetXmlFormat() {
		System.out.println("getXmlFormat");
		TimeStamp instance = null;
		String expResult = "";
		String result = instance.getXmlFormat();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
