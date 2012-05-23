/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.dbaccess;

import java.io.File;
import static org.junit.Assert.fail;
import org.junit.*;

/**
 *
 * @author ccwelich
 */
public class SelectMetaDataTest {

	DbAccess dbaccess;

	public SelectMetaDataTest() {
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
		webarchive.api.select.SelectMetaData select = new webarchive.api.select.SelectMetaData(
			null, null, null, null, null);
		SelectMetaData instance = new SelectMetaData(dbaccess);
		System.out.println(instance.select(select.getWhere(),
			select.getOrderBy(), null));
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}
