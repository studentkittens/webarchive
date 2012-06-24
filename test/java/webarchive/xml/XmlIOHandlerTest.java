/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.xml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import webarchive.handler.Handlers;
import webarchive.server.LockHandlerMockup;
import webarchive.server.LockHandlerMockup.State;
import webarchive.server.LockHandlerMockup.StateType;

/**
 *
 * @author ccwelich
 */
public class XmlIOHandlerTest {
	private XmlIOHandler instance;
	private File targetFile;
	private long size0;
	private LockHandlerMockup locker;
	
	public XmlIOHandlerTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		XmlPrepare.builtHandlers();
		
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		XmlPrepare.killHandlers();
	}
	
	@Before
	public void setUp() throws IOException {
		XmlPrepare.restoreFiles();
		targetFile = new File("test/xml/example.xml");
		size0 = Files.size(targetFile.toPath());
		FileDescriptorMockup target = new FileDescriptorMockup(targetFile);
		locker = Handlers.get(LockHandlerMockup.class);
		
		instance = Handlers.get(XmlMethodFactory.class).newXmlIOHandler(target);
		
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of buildDocument method, of class XmlIOHandler.
	 */
	@Test
	public void testBuildDocument() throws Exception {
		System.out.println("buildDocument");
		Document result = instance.buildDocument();
		assertTrue(result!=null);
	}

	/**
	 * Test of write method, of class XmlIOHandler.
	 */
	@Test
	public void testWrite() throws Exception {
		System.out.println("write");
		Document result = instance.buildDocument();
		Element e =result.createElement("bla");
		result.appendChild(e);
		instance.write(result);
		long size1 = Files.size(targetFile.toPath());

		assertTrue(size1>size0);
	}

	/**
	 * Test of lock method, of class XmlIOHandler.
	 */
	@Test
	public void testLock() {
		System.out.println("lock");
		instance.lock();
		State[] state = locker.fetchStates();
		assertEquals(StateType.LOCK, state[0].stateType);
		assertEquals(StateType.CHECKOUT, state[1].stateType);

	}

	/**
	 * Test of unlock method, of class XmlIOHandler.
	 */
	@Test
	public void testUnlock() {
		System.out.println("unlock");
		instance.unlock();
		State[] state = locker.fetchStates();
		assertEquals(StateType.COMMIT,state[0].stateType);
		assertEquals(StateType.CHECKOUT_MASTER,state[1].stateType);
		assertEquals(StateType.UNLOCK,state[2].stateType);
	}
}
