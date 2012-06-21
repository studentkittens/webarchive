/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.select;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.*;
import webarchive.api.model.CommitTag;
import webarchive.api.model.TimeStamp;

/**
 *
 * @author ccwelich
 */
public class SelectMetaByCommitTest {

	int N;
	List<CommitTag> commits;
	private Select[] inst;
	private String whereHistory;
	private String whereMime;
	private String whereMeta;
	private String[] orderBy;
	private CommitTag ct1;
	private CommitTag ct2;

	public SelectMetaByCommitTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws ParseException {
		commits = new LinkedList<>();
		ct1 = new CommitTag(1, new TimeStamp(
			"2012-05-15T17:30:00"),
			"www.heise.de");
		commits.add(ct1);
		ct2 = new CommitTag(2, new TimeStamp(
			"2012-05-15T17:35:00"),
			"www.wikipedia.de");
		commits.add(ct2);
		whereHistory = "history";
		whereMime = "mime";
		whereMeta = "meta";
		orderBy = new String[]{"orderBy"};
		inst = new Select[]{
			new SelectMetaByCommit(commits, whereHistory, whereMime, whereMeta,
			orderBy),
			new SelectMetaByCommit(null, whereHistory, whereMime, whereMeta,
			orderBy),
			new SelectMetaByCommit(null, null, null, null)
		};
		int N = inst.length;

	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getCommit method, of class SelectMetaByCommit.
	 */
	@Test
	public void testGetCommit() {
		SelectMetaByCommit tmp = (SelectMetaByCommit) inst[0];
		assertEquals(ct1, tmp.getCommit(1));
		assertTrue(tmp.getCommit(42)==null);
	}

	/**
	 * Test of getOrderBy method, of class Select.
	 */
	@Test
	public void testGetOrderBy() {
		System.out.println("getOrderBy");
		String[][] expResult = {
			orderBy,
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
		String commitIn = "commitId = IN(" + commits.get(0).getId() + ", " + commits.
			get(0).getId() + ")";
		String[][] expResult = {
			new String[]{whereMime, whereMeta, commitIn+"AND ("+whereHistory+")"},
			new String[]{whereMime, whereMeta, whereHistory},
			new String[]{null, null, null},
			new String[]{null, null, null}};
		for (int i = 0; i < N; i++) {
			assertArrayEquals(expResult[i], inst[i].getWhere());
		}
	}
}
