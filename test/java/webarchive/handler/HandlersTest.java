/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.handler;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author ccwelich
 */
public class HandlersTest {
	private Handlers instance;

	class TestHandler extends Handler {

		int id;

		public TestHandler(int id) {
			this.id = id;
		}
	}

	class TestHandler1 extends TestHandler {

		public TestHandler1(int id) {
			super(id);
		}
	}

	class TestHandler2 extends TestHandler {

		public TestHandler2(int id) {
			super(id);
		}
	}

	public HandlersTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		instance = new Handlers();
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of add and get method, of class Handlers.
	 */
	@Test
	public void testAddGet() {
		System.out.println("add");
		Handler h1 = new TestHandler1(1), h2 = new TestHandler2(2);
		instance.add(h1);
		instance.add(h2);
		//test generic cast
		TestHandler1 h11 = instance.get(TestHandler1.class);
		assertEquals(h11,h1);
		
		System.out.println("get");
		assertTrue(h1 == instance.get(TestHandler1.class));
		assertTrue(h2 == instance.get(TestHandler2.class));
		assertTrue(instance.get(TestHandler.class)==null);
	
	}
	/**
	 * Test of add and get method, of class instance.
	 */
	@Test
	public void testIllegal() {
		System.out.println("illegal");
		boolean thrown = false;
		//add null
		try {
			instance.add(null);
		} catch (AssertionError ex) {
			thrown = true;
		}
		assertTrue(thrown);
		// add dublicate
		thrown = false;
		Handler h1 = new TestHandler1(1);
		instance.add(h1);
		try {
			instance.add(h1);
		} catch (AssertionError ex) {
			thrown = true;
		}
		assertTrue(thrown);
		
	}
}
