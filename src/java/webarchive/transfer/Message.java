package webarchive.transfer;

import webarchive.headers.Header;
import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 7455459151713375986L;

	private Header h;
	private Object data;

	public Message(Header h, Object data)
	{
		this.h = h;
		this.data = data;
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

}
