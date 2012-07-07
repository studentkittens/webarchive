package webarchive.connection;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import webarchive.server.ServerConnectionHandler;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

public class ConnectionHandlerTest {
	private ServerConnectionHandler cH;
	private Message m[],a[];
	private Runnable t[];
	private Thread t1[],t2[];
	private Random rnd;
	@Before
	public void setUp() throws Exception {
		cH = new ServerConnectionHandler();
		rnd = new Random();
		setMessages();
	}

	private void setMessages() {
		m = new Message[1500];
		a = new Message[m.length];
		t = new Runnable[m.length];
		for(int i=0;i<m.length;i++) {
			m[i] = new Message(Header.PING, i);
			t[i] = new Runnable() {

				@Override
				public void run() {
					
				}
				
			};
		}
		
		
	}
	@Test(timeout=30000)
	public void testWaitForAnswerNormal() {
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//first run through: wait first, then wake em all up
		this.waitForIt();
		this.wakeEmUp();
		this.joinEm();
		assertTrue(new Integer(cH.getMap().size()).equals(0));
	}
	@Test(timeout=30000)
	public void testWaitForAnswerReverse() {
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//second run through: wake up first, then wait for it
		this.wakeEmUp();
		this.waitForIt();
		this.joinEm();
		assertTrue(new Integer(cH.getMap().size()).equals(0));
	}
	@Test(timeout=30000)
	public void testWaitForAnswerDoubleWakeUp() {
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//third run through:  wake up, check map, wake up,  wait for it
		this.wakeEmUp();
		this.joinEm();
		assertFalse(new Integer(cH.getMap().size()).equals(0));
		this.wakeEmUp();
		this.joinEm();
		this.waitForIt();
		this.joinEm();
		assertTrue(new Integer(cH.getMap().size()).equals(0));
	}
	@Test(timeout=30000)
	public void testWaitForAnswerConcurrentWakeUp() {
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//fourth run through: wakeup several times, then wait for it
		this.wakeEmUp();
		this.wakeEmUp();
		this.wakeEmUp();
		this.wakeEmUp();
		this.wakeEmUp();
		this.joinEm();
		assertTrue(new Integer(cH.getMap().size()).equals(m.length));
		this.waitForIt();
		this.joinEm();
		
		assertTrue(new Integer(cH.getMap().size()).equals(0));
	}
	@Test(timeout=30000)
	public void testWaitForAnswerConcurrentWait() {
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//fifth run through: wait several times, then wakeup all;
		Thread[] t3 = new Thread[3];
		for(Thread t : t3) {
			t = new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = m[rnd.nextInt(m.length)];
					Message a = cH.waitForAnswer(msg, this);
					assertEquals(a,msg);
				}
				
			});
			t.start();
		}
		this.wakeEmUp();
		this.wakeEmUp();
		this.joinEm();
		this.waitForIt();
		this.joinEm();
		assertTrue(new Integer(cH.getMap().size()).equals(0));
	}
	
	protected void wakeEmUp() {
		t2 = new Thread[m.length];
		for(int i =0;i<m.length;i++) {
			final int k = i;
			t2[k] = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(rnd.nextInt(5)+1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					cH.wakeUp(m[k]);
					synchronized(cH.getMap()) {
						assertFalse(new Integer(cH.getMap().size()).equals(0));
					}
				}
				
			});
			t2[k].start();
		}
	}

	protected void waitForIt() {
		t1 = new Thread[m.length];
		for(int i =0;i<m.length;i++) {
			final int k = i;
			
			t1[k] = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(rnd.nextInt(5)+1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					a[k] = cH.waitForAnswer(m[k], t[k]);	
					assertEquals(a[k].getData(),m[k].getData());
				}
				
			});
			t1[k].start();
		}
	}
	

	
	private void joinEm() {
		for(int i =0;i<m.length;i++) {
			try {
				if(t1!=null) {
					t1[i].join();
					assertNotNull(a[i]);
				}	
				if(t2!=null) {
					t2[i].join();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
	}
	
}
