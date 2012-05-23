/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.dbaccess;

import java.io.File;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
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
		Select select = new webarchive.api.select.SelectCommitTag(null, null);
		List result = instance.select(select);
		System.out.println(result);
		result = instance.select(new NotImplementedSelect(null, null));
		
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	class NotImplementedSelect extends Select {

		public NotImplementedSelect(String[] where, String[] orderBy) {
			super(where, orderBy);
		}
		
	}
}
