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
public class CommitTagTest {

	public CommitTagTest() {
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
	 * Test of init with null.
	 */
	@Test
	public void testIllegalInit() {
		boolean thrown = false;
		CommitTag c = null;
		try {
			c = new CommitTag(0, new TimeStamp(new Date()), "bla");
		} catch (AssertionError e) {
			thrown = true;
		}
		assertTrue(thrown);
		
	thrown = false;
		c = null;
		try {
			c = new CommitTag(1, null, "bla");
		} catch (AssertionError e) {
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown = false;
		c = null;
		try {
			c = new CommitTag(2, new TimeStamp(new Date()), null);
		} catch (AssertionError e) {
			thrown = true;
		}
		assertTrue(thrown);

	}

	/**
	 * Test of getId method, of class CommitTag.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		CommitTag instance = null;
		int expResult = 0;
		int result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCommitTime method, of class CommitTag.
	 */
	@Test
	public void testGetCommitTime() {
		System.out.println("getCommitTime");
		CommitTag instance = null;
		TimeStamp expResult = null;
		TimeStamp result = instance.getCommitTime();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDomain method, of class CommitTag.
	 */
	@Test
	public void testGetDomain() {
		System.out.println("getDomain");
		CommitTag instance = null;
		String expResult = "";
		String result = instance.getDomain();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class CommitTag.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		CommitTag instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
