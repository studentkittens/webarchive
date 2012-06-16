/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.dbaccess;

import java.io.File;
import java.sql.ResultSet;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.*;
import webarchive.api.model.CommitTag;
import webarchive.api.model.TimeStamp;

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
		String whereCommitTag = "commitTime='2012-05-15T17:30:00'";
		String whereDomain = "domainName='www.heise.de'";
		String[] orderBy = {"domainName ASC"};
		webarchive.api.select.SelectCommitTag select = new webarchive.api.select.SelectCommitTag(
			whereCommitTag, whereDomain, orderBy);
				
		CommitTag expect = new CommitTag(1,new TimeStamp("2012-05-15T17:30:00"),"www.heise.de");
		List<CommitTag> result = instance.select(select);
		assertTrue(result.size()==1);
		assertEquals(expect, result.get(0));
				
	}

}
