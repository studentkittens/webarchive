/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.model;

import java.util.Date;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author ccwelich
 */
public class CommitTagTest {
	private String domain;

	private CommitTag inst1;
	private CommitTag inst2;
	private TimeStamp timeStamp;

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
		timeStamp = new TimeStamp(new Date());
		domain = "heise.de";
		inst1 = new CommitTag(1, timeStamp, domain);
		inst2 = new CommitTag(2, timeStamp, domain); // distinguished by id

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
		try {
			c = new CommitTag(1, null, "bla");
		} catch (AssertionError e) {
			thrown = true;
		}
		assertTrue(thrown);

		thrown = false;
		try {
			c = new CommitTag(2, new TimeStamp(new Date()), null);
		} catch (AssertionError e) {
			thrown = true;
		}
		assertTrue(thrown);

	}
	
	@Test
	public void testEqualsHash() {
		System.out.println("equalsHash");
		assertEquals(inst1, inst1);
		final CommitTag tmp = new CommitTag(1, timeStamp, domain);
		assertEquals(inst1, tmp);
		assertFalse(inst1.equals(inst2));
		assertFalse(inst1.hashCode()==inst2.hashCode());
		assertEquals(inst1.hashCode(), tmp.hashCode());
		assertFalse(inst1.equals(null));

	}
	/**
	 * Test of getId method, of class CommitTag.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		int expResult = 1;
		int result = inst1.getId();
		assertEquals(expResult, result);

	}

	/**
	 * Test of getCommitTime method, of class CommitTag.
	 */
	@Test
	public void testGetCommitTime() {
		System.out.println("getCommitTime");
		TimeStamp result = inst1.getCommitTime();
		assertEquals(timeStamp, result);
	}

	/**
	 * Test of getDomain method, of class CommitTag.
	 */
	@Test
	public void testGetDomain() {
		System.out.println("getDomain");
		String expResult = domain;
		String result = inst1.getDomain();
		assertEquals(expResult, result);
	}

}
