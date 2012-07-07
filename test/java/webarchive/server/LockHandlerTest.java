package webarchive.server;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import webarchive.api.model.CommitTag;
import webarchive.api.model.MetaData;
import webarchive.api.model.TimeStamp;
import webarchive.transfer.FileDescriptor;
/**
 * The LockHandlerTest instantiates a JavadapterEmu and tries to get the LockHandlerImpl to connect to the Emulator.
 * Then it tests several locking commands and checks for unexpected behavior.
 * Assertions must be disabled for this test.
 * 
 * @author Schneider
 *
 */
public class LockHandlerTest {

	static JavadapterEmu emu;
	static LockHandlerImpl lock;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emu = new JavadapterEmu();
		new Thread(emu).start();
		Thread.sleep(300);
		lock = new LockHandlerImpl(InetAddress.getLocalHost(), 42421);
	}


	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		emu.svSock.close();
	}
	
	@Before
	public void setUp() throws UnknownHostException {
	}

	@After
	public void checkForFail() {
		checkException();
	}
	private FileDescriptor generateFd(String domain) {
		return new FileDescriptor(new MetaData(domain,null,null,null,new TimeStamp(new Date()),
				new CommitTag(new Random().nextInt(5000),new TimeStamp(new Date()),domain)),null);
	}
	private void checkException() {
		if(JavadapterEmu.exc != null) {
			String msg = JavadapterEmu.exc.toString();
			JavadapterEmu.exc = null;
			fail(msg);
		}
	}
	@Test
	public void testCheckout() {
		lock.checkout(generateFd("www.hack.org"));
		if(!JavadapterEmu.answer.equals("OK"))
			fail("Checkout Failed");
	}
	@Test
	public void testCheckoutInvalidDomain() {
		lock.checkout(generateFd("www.BLA.org"));
		if(!JavadapterEmu.answer.equals("ACK Invalid Domain."))
			fail("Accepted invalid domain");
	}
	@Test
	public void testCheckoutInvalidParamCount() {
		lock.checkout(generateFd("www.hack.org d"));
		if(!JavadapterEmu.answer.equals("OK"))
			fail("Checkout Failed");
	}
	@Test
	public void testCommit() {
		lock.commit(generateFd("www.hack.org"));
		if(!JavadapterEmu.answer.equals("OK"))
			fail("Commit Failed");
	}
	@Test
	public void testCommitInvalidDomain() {
		lock.commit(generateFd("www.BLUB.org")); //EXCEPTION "ACK Invalid Domain." expected
		if(!JavadapterEmu.answer.equals("ACK Invalid Domain."))
			fail("Commit Failed");
	}
	@Test
	public void testCommitInvalidParamCount() {
		lock.commit(generateFd("www.hack.org d"));
		if(!JavadapterEmu.answer.equals("OK"))
			fail("Commit Failed");
	}
	

	@Test
	public void testLock() {
		lock.lock(generateFd("www.hack.org"));
		if(!JavadapterEmu.answer.equals("OK"))
			fail("Lock Failed");
		lock.unlock(generateFd("www.hack.org"));
		if(!JavadapterEmu.answer.equals("OK"))
			fail("UnLock Failed");
	}
	
	@Test
	public void testLockInvalidDomain() {
		lock.lock(generateFd("www.MOEEP.org"));
		if(!JavadapterEmu.answer.equals("ACK Invalid Domain."))
			fail("Lock Failed");
	}
	@Test
	public void testLockInvalidCount() {
		lock.lock(generateFd("www.hack.org e"));
		if(!JavadapterEmu.answer.equals("OK"))
			fail("Lock Failed");
		lock.unlock(generateFd("www.hack.org"));
		if(!JavadapterEmu.answer.equals("OK"))
			fail("UnLock Failed");
	}
	

	@Test
	public void testList_branches() {
		lock.list_branches("www.golem.de");
		if(!JavadapterEmu.answer.equals("OK"))
			fail("list branches Failed");
	}

	@Test
	public void testList_commits() {
		lock.list_commits("www.golem.de");
		if(!JavadapterEmu.answer.equals("OK"))
			fail("list commits Failed");
	}

}
