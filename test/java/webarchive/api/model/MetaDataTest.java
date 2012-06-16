/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.model;

import java.io.File;
import java.text.ParseException;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author ccwelich
 */
public class MetaDataTest {

	private CommitTag commitTag;
	private TimeStamp createTime;
	private String mime;
	private File path;
	private String title;
	private String url;
	private MetaData instance;

	public MetaDataTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws ParseException {
		url = "heise.de/index.html";
		mime = "text/html";
		title = "heise online";
		path = new File("heise.de/index.html");
		createTime = new TimeStamp("2012-05-15T17:30:00");
		commitTag = new CommitTag(1, new TimeStamp("2012-05-15T17:35:00"),
			"heise.de");
		instance = new MetaData(url, mime, title, path, createTime, commitTag);
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getCommitTag method, of class MetaData.
	 */
	@Test
	public void testGetCommitTag() {
		System.out.println("getCommitTag");
		CommitTag expResult = commitTag;
		CommitTag result = instance.getCommitTag();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getCreateTime method, of class MetaData.
	 */
	@Test
	public void testGetCreateTime() {
		System.out.println("getCreateTime");
		TimeStamp expResult = createTime;
		TimeStamp result = instance.getCreateTime();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getPath method, of class MetaData.
	 */
	@Test
	public void testGetPath() {
		System.out.println("getPath");
		File expResult = path;
		File result = instance.getPath();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getUrl method, of class MetaData.
	 */
	@Test
	public void testGetUrl() {
		System.out.println("getUrl");
		String expResult = url;
		String result = instance.getUrl();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getMimeType method, of class MetaData.
	 */
	@Test
	public void testGetMimeType() {
		System.out.println("getMimeType");
		String expResult = mime;
		String result = instance.getMimeType();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getTitle method, of class MetaData.
	 */
	@Test
	public void testGetTitle() {
		System.out.println("getTitle");
		String expResult = title;
		String result = instance.getTitle();
		assertEquals(expResult, result);
	}

	@Test
	public void testIllegalInit() {
		System.out.println("illegal init");
		boolean thrown = false;
		MetaData inst;
		// url asserted as not null
		try {
			inst = new MetaData(null, mime, title, path, createTime, commitTag);
		} catch(AssertionError exc) {
			thrown = true;
		}
		assertTrue(thrown);
		thrown = false; 
		
		// mimeType asserted not null
	
		try {
			inst = new MetaData(url, null, title, path, createTime, commitTag);
		} catch(AssertionError exc) {
			thrown = true;
		}
		assertTrue(thrown); 
		//title. Asserted as either null or not empty.
		thrown = false;
		inst = new MetaData(url, mime, null, path, createTime, commitTag); //legal
		try {
			inst = new MetaData(url, mime, "", path, createTime, commitTag);
		} catch(AssertionError exc) {
			thrown = true;
		}
		assertTrue(thrown); 
		// path, asserted not null.
	 
		thrown = false;
		try {
			inst = new MetaData(url, mime, title, null, createTime, commitTag);
		} catch(AssertionError exc) {
			thrown = true;
		}
		assertTrue(thrown);
		// createTime, asserted not null.
	
		thrown = false;
		try {
			inst = new MetaData(url, mime, title, path, null, commitTag);
		} catch(AssertionError exc) {
			thrown = true;
		}
		assertTrue(thrown); 
		//commitTag, asserted not null.
		thrown = false;
		try {
			inst = new MetaData(url, mime, title, path, createTime, null);
		} catch(AssertionError exc) {
			thrown = true;
		}
		
	}
	@Test
	public void testEqualsHash() {
		System.out.println("equals and hash");
		assertEquals(instance, instance);
		final MetaData m2 = new MetaData(url, mime, null, path, createTime,
		 commitTag);
		assertEquals(instance, m2);
		assertEquals(instance.hashCode(), m2.hashCode());
		CommitTag c2 = new CommitTag(2, createTime, "wikipedia.de");
		assertFalse(instance.equals(null));
		final MetaData m3 = new MetaData("wikipedia.de/index.html", mime, null, path, createTime,
		 commitTag);
		assertFalse(instance.equals(m3));
		assertFalse(instance.hashCode()==m3.hashCode());
		final MetaData m4 = new MetaData(url, mime, null, path, createTime,
		 c2);
		assertFalse(instance.equals(m4));
		assertFalse(instance.hashCode()==m4.hashCode());
	}
}
