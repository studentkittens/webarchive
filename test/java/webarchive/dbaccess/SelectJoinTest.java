/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.dbaccess;

import java.sql.ResultSet;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author ccwelich
 */
public class SelectJoinTest {

	private SelectJoinMockup inst1, inst2, inst3;

	public SelectJoinTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		inst1 = new SelectJoinMockup(null, new String[]{"a"}, null) {
		};
		inst2 = new SelectJoinMockup(null, new String[]{"a", "b"}, new String[]{
				"key(a,b)"}) {
		};
		inst3 = new SelectJoinMockup(null, new String[]{"a", "b", "c"},
			new String[]{"key(a,b)", "key(b,c)"}) {
		};

	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getSqlTemplate method, of class SelectJoin.
	 */
	@Test
	public void testGetSqlTemplate() {
		System.out.println("getSqlTemplate");

		assertEquals("SELECT * FROM a%1$s", inst1.getSqlTemplate());
		assertEquals(
			"SELECT * FROM a JOIN (SELECT * FROM b%2$s) USING(key(a,b))%1$s",
			inst2.getSqlTemplate());
		assertEquals(
			"SELECT * FROM a JOIN (SELECT * FROM b JOIN (SELECT * FROM c%3$s) USING(key(b,c))%2$s) USING(key(a,b))%1$s",
			inst3.getSqlTemplate());

	}

	/**
	 * Test of insertWhere method, of class SelectJoin.
	 */
	@Test
	public void testSelect() throws Exception {
		System.out.println("select");
		inst1.select(new String[]{"w(a)"}, new String[]{"o(a)"}, null);
		assertEquals("SELECT * FROM a WHERE w(a) ORDER BY o(a);",
			inst1.resultStmnt);
		assertEquals(null, inst1.resultArg);
		//sqlinjection
		inst1.select(new String[]{"w(a);insert..."}, new String[]{"o(a)"}, null);
		assertEquals("SELECT * FROM a WHERE w(a) ORDER BY o(a);",
			inst1.resultStmnt);
		assertEquals(null, inst1.resultArg);

		inst2.select(new String[]{"w(a,b)", "w(b)"}, null, 42);
		assertEquals(
			"SELECT * FROM a JOIN (SELECT * FROM b WHERE w(b)) USING(key(a,b)) WHERE w(a,b);",
			inst2.resultStmnt);
		assertEquals(42, inst2.resultArg);

		inst3.select(new String[]{"w(a,b,c)", "w(b,c)", "w(c)"}, null, null);
		assertEquals(
			"SELECT * FROM a JOIN (SELECT * FROM b JOIN (SELECT * FROM c WHERE w(c)) USING(key(b,c)) WHERE w(b,c)) USING(key(a,b)) WHERE w(a,b,c);",
			inst3.resultStmnt);
		assertEquals(null, inst3.resultArg);

		inst3.select(new String[]{"w(a,b,c)", null, "w(c)"}, null, null);
		assertEquals(
			"SELECT * FROM a JOIN (SELECT * FROM b JOIN (SELECT * FROM c WHERE w(c)) USING(key(b,c))) USING(key(a,b)) WHERE w(a,b,c);",
			inst3.resultStmnt);
		assertEquals(null, inst3.resultArg);

		inst3.select(new String[]{null, null, null}, null, null);
		assertEquals(
			"SELECT * FROM a JOIN (SELECT * FROM b JOIN (SELECT * FROM c) USING(key(b,c))) USING(key(a,b));",
			inst3.resultStmnt);
		assertEquals(null, inst3.resultArg);
		//sql injection
		inst3.select(new String[]{"w(a,b,c);insert...", "w(b,c);insert...",
				"w(c);insert..."}, new String[]{"o(a);insert...", ";insert..."},
			null);
		System.out.println("inst3.resultStmnt = " + inst3.resultStmnt);
		
		assertEquals(
			"SELECT * FROM a JOIN (SELECT * FROM b JOIN (SELECT * FROM c WHERE w(c)) USING(key(b,c)) WHERE w(b,c)) USING(key(a,b)) WHERE w(a,b,c) ORDER BY o(a);",
			inst3.resultStmnt);
		assertEquals(null, inst3.resultArg);
	}

	class SelectJoinMockup extends SelectJoin {

		public String resultStmnt;
		public Object resultArg;

		@Override
		protected Object fromResultSet(ResultSet rs, Object arg) throws
			Exception {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public SelectJoinMockup(DbAccess dbAccess, String[] tables,
			String[] keys) {
			super(dbAccess, tables, keys);
		}

		@Override
		protected List executeSelect(String selectStatement, Object arg) throws
			Exception {
			resultStmnt = selectStatement;
			resultArg = arg;
			return null;
		}
	}
}
