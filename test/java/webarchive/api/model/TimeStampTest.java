/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author ccwelich
 */
public class TimeStampTest {
	private TimeStamp tByStr;
	private TimeStamp tByDate;
	private Date date;
	private String xmlFormat;
	
	public TimeStampTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() throws ParseException {
		DateFormat df = DateFormat.getDateTimeInstance();
		xmlFormat = "2012-06-15T19:05:50";
		tByStr = new TimeStamp(xmlFormat);
		date = df.parse("15.06.2012 19:05:50");
		tByDate = new TimeStamp(date);
	}
	
	@After
	public void tearDown() {
	}
	/**
	 * Test illegal init.
	 */
	@Test
	public void testIllegalInit() {
		System.out.println("illegalInit");
		TimeStamp ts;
		boolean thrown = false;
		//null values
		try {
			ts = new TimeStamp((Date)null);
		} catch (AssertionError exc) {
			thrown = true;
		}
		assertTrue(thrown);
		thrown = false;
		try {
			ts = new TimeStamp((String)null);
		} catch (AssertionError exc) {
			thrown = true;
		} catch (ParseException exc) {
			assert false;
		}
		assertTrue(thrown);
		thrown = false;
		try {
			ts = new TimeStamp("2012-06-15 19:05:50");
		} catch (ParseException exc) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	/**
	 * Test equals and hash.
	 */
	@Test
	public void testEqualsHash() {
		System.out.println("equals and hash");
		assertTrue(tByDate.equals(tByDate));
		assertTrue(tByDate.equals(tByStr));
		assertTrue(tByStr.equals(tByDate));
		assertTrue(tByStr.equals(tByStr));
		assertFalse(tByStr.equals(null));
		assertEquals(tByDate.hashCode(), tByStr.hashCode());
		TimeStamp tmp = new TimeStamp(new Date());
		assertFalse(tByDate.equals(tmp));
		assertFalse(tmp.equals(tByDate));
		assertFalse(tmp.hashCode()==tByDate.hashCode());
	}
	/**
	 * Test of getDate method, of class TimeStamp.
	 */
	@Test
	public void testGetDate() {
		System.out.println("getDate");
		assertEquals(date, tByStr.getDate());
		assertEquals(date, tByDate.getDate());
	}

	/**
	 * Test of getXmlFormat method, of class TimeStamp.
	 */
	@Test
	public void testGetXmlFormat() {
		System.out.println("getXmlFormat");
		assertEquals(xmlFormat, tByDate.getXmlFormat());
		assertEquals(xmlFormat, tByStr.getXmlFormat());
	}
}
