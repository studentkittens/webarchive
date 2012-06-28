package webarchive.server;

import java.net.InetAddress;
import java.util.*;
import webarchive.handler.Handler;
import webarchive.server.LockHandler;
import webarchive.transfer.FileDescriptor;

/**
 * used to bypass LockHandlerImpl
 * @author ccwelich
 */
public class LockHandlerMockup extends LockHandler {

	

	public enum StateType {

		CHECKOUT, COMMIT, LOCK, RECONNECT, UNLOCK, CHECKOUT_MASTER;
	}

	public class State {

		public StateType stateType;
		public FileDescriptor fd;

		public State(StateType stateType, FileDescriptor fd) {
			this.stateType = stateType;
			this.fd = fd;
		}

		@Override
		public String toString() {
			return "State{" + "stateType=" + stateType + ", fd=" + fd + '}';
		}

		private State(StateType stateType) {
			this(stateType, null);
		}
	}
	private Queue<State> states;

	public LockHandlerMockup() {
		this.states = new LinkedList<>();
	}

	@Override
	public void checkout(FileDescriptor fd) {
		states.offer(new State(StateType.CHECKOUT, fd));
	}

	@Override
	public void commit(FileDescriptor fd) {
		states.offer(new State(StateType.COMMIT, fd));
	}

	@Override
	public void lock(FileDescriptor fd) {
		states.offer(new State(StateType.LOCK, fd));
	}

	@Override
	public void reconnect() {
		states.offer(new State(StateType.RECONNECT, null));
	}

	@Override
	public void unlock(FileDescriptor fd) {
		states.offer(new State(StateType.UNLOCK, fd));
	}

	public State[] fetchStates() {
		State[] rc = new State[states.size()];
		int i = 0;
		while (!states.isEmpty()) {
			rc[i++] = states.poll();
		}
		return rc;
	}

	public static void main(String args) {
		LockHandlerMockup l = new LockHandlerMockup();
		l.lock(null);
		LockHandlerMockup.State[] s = l.fetchStates();
		for (State state : s) {
			System.out.println(
				"l state =" + state.stateType + ", fd=" + state.fd);
		}
		s = l.fetchStates();
		for (State state : s) {
			System.out.println("l state=" + s);
		}
	}
}
