package webarchive.transfer;

import webarchive.headers.Header;
import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 7455459151713375986L;

	private Header h;
	private Object data;
	
	private Integer id = null;
	private static int mId=0;
	
	public Message(Header h, Object data) {
		this.h = h;
		this.data = data;
		this.markAsQuery();
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

	private void markAsQuery() {
		id = incId();
	}
	
	public void setBroadCast() {
		id = null;
	}
	
	private synchronized  int incId() {
		mId = (mId+2) % Integer.MAX_VALUE;
		return mId;
	}
}
