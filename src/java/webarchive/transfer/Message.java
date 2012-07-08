package webarchive.transfer;

import java.io.Serializable;
/**
 * Base Class for Information that can be sent from a NetWorkmodule to another one. (Server->Client, Client->Server)
 * Each Message contains a Header Enum, an Object with some Serializable Data and an id.
 * The Header defines how the target NetMod reacts to this Message.
 * The data Object must be casted to a desired Class by using the Header as the defining Information.
 * The id is responsible for Message assignment. Every Message from a NetMod will be Stored in a Hashmap on the sender by using this id.
 * As soon as the Receiver answers with a Message having the same id, the answer is stored in the Hashmap 
 * on the sender side and the sending Thread that was waiting for an answer is notified and can fetch this message from the Hashmap.
 * This way the problematic of timed out connections that suddenly send an answer to an old Message is eliminated, 
 * since on the sender side this message id is not present in the HashMap and can be ignored or stored for a retry.
 * For Example:
 * 
 * There are two NetworkModules: sv and cl
 * 
 * cl sends a Read Request in the Message m1 with id=1.
 * sv receives this message and tries to read the files. cl is waiting. sv takes too long to read the file
 * and cl decides to timeout the request and wake up from the wait.
 * Now cl send a different Request with a new Message m2 with id=2.
 * sv however just finished the m1-read and sends an answer a1 with id=1;
 * cl receives this a1 and searches for a waiting thread in the hashmap. Because there is none this a1 can be disposed.
 * If the messages weren't marked with an id, cl would have been woken up by a message it didnt expect.
 * 
 * 
 * @author Schneider
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 7455459151713375986L;

	private Header h;
	private Object data;
	
	private Integer id = null;
	private static volatile int mId=0;
	
	public Message(Header h, Object data) {
		this.h = h;
		this.data = data;
		this.markAsQuery();
	}
	
	public Message(Message query, Object data) {
		this.h = query.getHeader();
		this.data = data;
		this.id = query.id;
	}

	public Message(Header h) {
		this(h,null);
	}

	public Header getHeader() {
		return h;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data=data;
	}

	public void setHeader(Header h) {
		this.h=h;
	}

	public Integer getId() {
		return id;
	}
	/**
	 * Marks current Message with an id.
	 */
	private void markAsQuery() {
		id = incId();
	}
	/**
	 * Used for Notifications where the sender does NOT expect an answer.
	 */
	public void setBroadCast() {
		id = null;
	}
	
	private synchronized  int incId() {
		mId = ++mId % Integer.MAX_VALUE;
		return mId;
	}
	/**
	 * Used for Notifications where the sender does NOT expect an answer.
	 */
	public void setNoAnswer() {
		this.setBroadCast();
	}
	
	public String toString() {
		return "["+this.h+"] "+this.data;
	}
}
