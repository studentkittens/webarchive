/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.dbaccess;

import java.io.File;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import webarchive.api.model.CommitTag;
import webarchive.api.model.TimeStamp;
import webarchive.api.select.Select;

/**
 *
 * @author ccwelich
 */
public class SqlHandlerTest {

	private static SqlHandler instance;
	private final SqliteAccess dbaccess;

	public SqlHandlerTest() {
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
		instance = new SqlHandler(dbaccess);

	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of select method, of class SqlHandler.
	 */
	@Test
	public void testSelect() throws Exception {
		System.out.println("select");
		Select select = new webarchive.api.select.SelectCommitTag("commitId = 1",
			null);
		List<CommitTag> result = instance.select(select);
		System.out.println(result);
		assertTrue(result.size() == 1);
		assertEquals(new CommitTag(1, new TimeStamp("2012-05-15T17:30:00"),
			"www.heise.de"), result.get(0));

		// illegal access
		boolean thrown = false;
		try {
			instance.select(null);
		} catch (NullPointerException exc) {
			thrown = true;
		}
		assertTrue(thrown);
		thrown = false;
		try {
			instance.select(new IllegalSelect(null, null));
		} catch (UnsupportedOperationException exc) {
			thrown = true;
		}
		assertTrue(thrown);

	}
}

class IllegalSelect extends webarchive.api.select.Select {

	public IllegalSelect(String[] where, String[] orderBy) {
		super(where, orderBy);
	}
}
