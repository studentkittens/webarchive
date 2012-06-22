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
		Handlers.clear();
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
		Handlers.add(h1);
		Handlers.add(h2);

		System.out.println("get");
		assertTrue(h1 == Handlers.get(TestHandler1.class));
		assertTrue(h2 == Handlers.get(TestHandler2.class));
		assertTrue(Handlers.get(TestHandler.class)==null);
	
	}
	/**
	 * Test of add and get method, of class Handlers.
	 */
	@Test
	public void testIllegal() {
		System.out.println("illegal");
		boolean thrown = false;
		try {
			Handlers.add(null);
		} catch (AssertionError ex) {
			thrown = true;
		}
		assertTrue(thrown);
		thrown = false;
		Handler h1 = new TestHandler1(1);
		Handlers.add(h1);
		try {
			Handlers.add(h1);
		} catch (AssertionError ex) {
			thrown = true;
		}
		assertTrue(thrown);
	}
}
