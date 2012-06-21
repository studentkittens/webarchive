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
import webarchive.api.model.MetaData;
import webarchive.api.model.TimeStamp;

/**
 *
 * @author ccwelich
 */
public class SelectMetaDataTest {
	
	private Select instance;
	
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
		instance = new SelectMetaData(new SqliteAccess(path));
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of fromResultSet method, of class SelectMetaData.
	 */
	@Test
	public void testSelect() throws Exception {
		System.out.println("select");
		String whereMime = "mimeName='text/html'";
		String whereDomain = "domainName='www.heise.de'";
		String whereMeta = null;
		String whereHistory = null;
		String whereCommitTag = "commitTime = '2012-05-15T17:30:00'";
		String orderBy = "createTime ASC";
		
		webarchive.api.select.SelectMetaData select = new webarchive.api.select.SelectMetaData(
			whereMime, whereMeta, whereDomain, whereHistory, whereCommitTag,
			orderBy);
		List<MetaData> actual = instance.select(select);
		MetaData[] expected = {
			new MetaData(
			"www.heise.de/index.html",
			"text/html",
			"heise online",
			new File("www.heise.de/index.html"),
			new TimeStamp("2012-05-15T17:28:42"),
			new CommitTag(1, new TimeStamp("2012-05-15T17:30:00"),
			"www.heise.de")),
			new MetaData(
			"www.heise.de/a/a.html",
			"text/html",
			null,
			new File("www.heise.de/a/a.html"),
			new TimeStamp("2012-05-15T17:29:15"),
			new CommitTag(1, new TimeStamp("2012-05-15T17:30:00"),
			"www.heise.de")),
		};
		for(int i=0; i<actual.size(); i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}
	
}
