/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.select;

import java.util.Arrays;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author ccwelich
 */
public class SelectCommitTagTest {

	private final int N = 5;
	private Select[] inst = new Select[N];
	private String[] orderBy;
	private String whereCommitTag;
	private String whereDomain;

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
		whereCommitTag = "whereCommitTag";
		whereDomain = "whereDomain";
		orderBy = new String[]{"orderby1",
			"orderby2"};
		inst[0] = new SelectCommitTag(null, null, null);
		inst[1] = new SelectCommitTag(whereCommitTag, null, null);
		inst[2] = new SelectCommitTag(null, whereDomain, null);
		inst[3] = new SelectCommitTag(null, null, orderBy);
		inst[4] = new SelectCommitTag(whereCommitTag, whereDomain, orderBy);


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
			null,
			null,
			null,
			orderBy,
			orderBy,};
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
			new String[]{null, null},
			new String[]{null, whereCommitTag},
			new String[]{whereDomain, null},
			new String[]{null, null},
			new String[]{whereDomain, whereCommitTag},};
		for (int i = 0; i < N; i++) {
			assertArrayEquals(expResult[i], inst[i].getWhere());
		}
	}
}
