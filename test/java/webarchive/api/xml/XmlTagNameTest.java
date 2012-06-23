/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.xml;

import webarchive.api.xml.TagName;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author ccwelich
 */
public class XmlTagNameTest {

	private TagName instance;

	public XmlTagNameTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new TagName("testTag");
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test illegal values
	 */
	@Test
	public void testIllegal() {
		boolean hasThrown = false;
		try {
			new TagName(null);
		} catch (AssertionError e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	/**
	 * Test of toString method, of class TagName.
	 */
	@Test
	public void testToString() {		
		System.out.println("toString");
		TagName name = new TagName("content");
		assertEquals("wa:content", name.toString());

		name = new TagName("wa:content");
		assertEquals("wa:content", name.toString());

		name = new TagName(":content");
		assertEquals("wa:content", name.toString());

		name = new TagName("T:sub:content");
		assertEquals("wa:sub:content", name.toString());
		String expResult = "wa:testTag";
		String result = instance.toString();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getName method, of class TagName.
	 */
	@Test
	public void testGetName() {
		System.out.println("getName: like toString");
		String expResult = "wa:testTag";
		String result = instance.getName();
		assertEquals(expResult, result);
	}

	/**
	 * Test of addPrefixTo method, of class TagName.
	 */
	@Test
	public void testAddPrefixTo() {
		
	}
}
