/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.select;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author ccwelich
 */
public class SelectMetaDataTest {

	private final int N = 4;
	private Select[] inst = new Select[N];
	private String whereMimeType;
	private String whereMeta;
	private String whereDomain;
	private String whereHistory;
	private String whereCommitTag;
	private String[] orderBy;

	public SelectMetaDataTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		whereMimeType = "mime";
		whereMeta = "meta";
		whereDomain = "domain";
		whereHistory = "history";
		whereCommitTag = "commit";
		orderBy = new String[]{"orderBy"};
		inst[0] = new SelectMetaData(whereMimeType, whereMeta, whereDomain,
			whereHistory, whereCommitTag, orderBy);
		inst[1] = new SelectMetaData(null, null, null, null, null, null);
		inst[2] = new SelectMetaData(null, null, null, null, null, orderBy);
		inst[3] = new SelectMetaData(whereMimeType, null, null, null, null, null);



	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getOrderBy method, of class Select.
	 */
	@Test
	public void testGetOrderBy() {
		System.out.println("getOrderBy");
		String[][] expResult = {
			orderBy,
			null,
			orderBy,
			null,};
		for (int i = 0; i < N; i++) {
			assertArrayEquals(expResult[i], inst[i].getOrderBy());
		}
	}

	/**
	 * Test of getWhere method, of class Select.
	 */
	@Test
	public void testGetWhere() {
		System.out.println("getWhere");
		String[][] expResult = {
			new String[]{whereMimeType, whereMeta, whereDomain, whereHistory,
				whereCommitTag},
			new String[]{null, null, null, null, null},
			new String[]{null, null, null, null, null},
			new String[]{whereMimeType, null, null, null, null},};
		for (int i = 0; i < N; i++) {
			assertArrayEquals(expResult[i], inst[i].getWhere());
		}
	}
}
