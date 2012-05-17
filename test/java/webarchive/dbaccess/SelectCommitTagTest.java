/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.dbaccess;

import java.io.File;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.*;
import webarchive.api.CommitTag;

/**
 *
 * @author ccwelich
 */
public class SelectCommitTagTest {
	SelectCommitTag instance = null;
	public SelectCommitTagTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
		File path = new File("test/sql/testdb");
		assert path.exists();
		instance = new SelectCommitTag(new SqliteAccess(path));
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of select method, of class SelectCommitTag.
	 */
	@Test
	public void testSelect() throws Exception {
		System.out.println("select");
		String[] where = {"domainName='www.heise.de'"};
		String[] orderBy = {"commitTime DESC","domainName ASC"};
		
		List<CommitTag> expResult = null;
		List<CommitTag> result = instance.select(where, orderBy);
		for(CommitTag tag : result) {
			System.out.println("  "+tag.getId() + ", " +tag);
		}	
		
		assertEquals(expResult, result);
		fail("The test case is a prototype.");
	}

}
