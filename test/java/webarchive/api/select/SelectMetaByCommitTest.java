/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.select;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;
import webarchive.api.model.CommitTag;
import webarchive.api.model.TimeStamp;

/**
 *
 * @author ccwelich
 */
public class SelectMetaByCommitTest {
	List<CommitTag> commits;
	public SelectMetaByCommitTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
					commits = new LinkedList<>();

		try {
			commits.add(new CommitTag(1, new TimeStamp("2012-05-15T17:30:00"),  "www.heise.de"));
			commits.add(new CommitTag(2, new TimeStamp("2012-05-15T17:35:00"),  "www.wikipedia.de"));
		} catch (ParseException ex) {
			Logger.getLogger(SelectMetaByCommitTest.class.getName()).log(Level.SEVERE, null, ex);
		}

		
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of getCommit method, of class SelectMetaByCommit.
	 */
	@Test
	public void testGetCommit() {
		System.out.println("getCommit");
		
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	/**
	 * Test of getOrderBy method, of class Select.
	 */
	@Test
	public void testGetOrderBy() {
		System.out.println("getOrderBy");
		Select instance = null;
		String[] expResult = null;
		String[] result = instance.getOrderBy();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getWhere method, of class Select.
	 */
	@Test
	public void testGetWhere() {
		System.out.println("getWhere");
		Select instance = new SelectMetaByCommit(commits, "historyAdd", "mime", "meta", null);
		System.out.println(Arrays.toString(instance.getWhere()));
		String[] expResult = null;
		String[] result = instance.getWhere();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
