/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.dbaccess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.*;
import webarchive.api.model.CommitTag;
import webarchive.api.model.MetaData;
import webarchive.api.model.TimeStamp;

/**
 *
 * @author ccwelich
 */
public class SelectMetaByCommitTest {
	
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
		instance = new SelectMetaByCommit(new SqliteAccess(path));
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
		List<CommitTag> commits = new ArrayList<CommitTag>();
		commits.add(new CommitTag(1, new TimeStamp("2012-05-15T17:30:00"), "www.heise.de"));
		commits.add(new CommitTag(2, new TimeStamp("2012-05-15T17:35:00"), "www.wikipedia.de"));
		commits.add(new CommitTag(3, new TimeStamp("2012-05-15T18:30:00"), "www.heise.de"));

		String whereMime = "mimeName='text/html'";
		String whereMeta = null;
		String whereHistoryAdd = "title not null";
		String[] orderBy = {"commitId ASC"};

		webarchive.api.select.SelectMetaByCommit select = new webarchive.api.select.SelectMetaByCommit(
			commits, whereHistoryAdd, whereMime, whereMeta, orderBy);
		List<MetaData> actual = instance.select(select);
		
		MetaData[] expected = {

			new MetaData("www.heise.de/index.html",	"text/html", "heise online", new File("www.heise.de/index.html"), new TimeStamp("2012-05-15T17:28:42"), commits.get(0)),
			new MetaData("www.wikipedia.de/index.html", "text/html", "wiki startseite", new File("www.wikipedia.de/index.html"), new TimeStamp("2012-05-15T17:35:27"), commits.get(1)),
			new MetaData("www.heise.de/index.html", "text/html", "Heise online", new File("www.heise.de/index.html"), new TimeStamp("2012-05-15T18:32:42"), commits.get(2))
			
		};
		for(int i=0; i<actual.size(); i++) {
			assertEquals(expected[i], actual.get(i));
		}
	}
	


}
