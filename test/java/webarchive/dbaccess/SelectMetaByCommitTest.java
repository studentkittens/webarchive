/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.dbaccess;

import java.io.File;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;
import webarchive.api.model.CommitTag;
import webarchive.api.model.MetaData;
import webarchive.api.model.TimeStamp;

/**
 *
 * @author ccwelich
 */
public class SelectMetaByCommitTest {

	List<CommitTag> commits;
	DbAccess dbaccess;

	public SelectMetaByCommitTest() {
		File path = new File("test/sql/testdb");
		assert path.exists();
		dbaccess = new SqliteAccess(path);
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
			commits.add(new CommitTag(1, new TimeStamp("2012-05-15T17:30:00"), "www.heise.de"));
			commits.add(new CommitTag(2, new TimeStamp("2012-05-15T17:35:00"), "www.wikipedia.de"));
		} catch (ParseException ex) {
			Logger.getLogger(webarchive.api.select.SelectMetaByCommitTest.class.getName()).log(Level.SEVERE, null, ex);
		}


	}

	/**
	 * Test of fromResultSet method, of class SelectMetaByCommit.
	 */
	@Test
	public void testSelect() throws Exception {
		System.out.println("select");
		webarchive.api.select.SelectMetaByCommit select = new webarchive.api.select.SelectMetaByCommit(commits, null, null, null);
		SelectMetaByCommit instance = new SelectMetaByCommit(dbaccess);
		final List<MetaData> select1 = instance.select(select.getWhere(), select.getOrderBy(), select);
		System.out.println(select1);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
