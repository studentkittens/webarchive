
package webarchive.server;

import java.net.InetAddress;
import webarchive.server.LockHandler;
import webarchive.transfer.FileDescriptor;

/**
 *
 * @author ccwelich
 */
public class LockHandlerMockup implements LockHandler {
	public enum StateType {
		CHECKOUT,COMMIT,LOCK,RECONNECT,UNLOCK;
	}
	public class State {
		public StateType stateType;
		public FileDescriptor fd;

		public State(StateType stateType, FileDescriptor fd) {
			this.stateType = stateType;
			this.fd = fd;
		}
		
	}
	private State state;

	
	@Override
	public void checkout(FileDescriptor fd) {
		state = new State(StateType.CHECKOUT, fd);
	}

	@Override
	public void commit(FileDescriptor fd) {
		state = new State(StateType.COMMIT, fd);
	}

	@Override
	public void lock(FileDescriptor fd) {
		state = new State(StateType.LOCK, fd);
	}

	@Override
	public void reconnect() {
		state = new State(StateType.RECONNECT, null);
	}

	@Override
	public void unlock(FileDescriptor fd) {
		state = new State(StateType.UNLOCK, fd);
	}
	public State fetchState() {
		State rc = state;
		state = null;
		return rc;
	}
	public static void main(String args) {
		LockHandlerMockup l = new LockHandlerMockup();
		l.lock(null);
		LockHandlerMockup.State s = l.fetchState();
		System.out.println("l state ="+s.stateType+", fd="+s.fd);
		s = l.fetchState();
		System.out.println("l state="+s);
	}
	

}
