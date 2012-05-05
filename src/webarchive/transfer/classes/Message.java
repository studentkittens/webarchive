package webarchive.transfer.classes;

import webarchive.headers.Header;
import webarchive.transfer.Transferable;

public class Message implements Transferable {

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

	@Override
	public Header getHeader() {
		return h;
	}
	
	@Override
	public Object getData() {
		return data;
	}
	
	@Override
	public void setData(Object data) {
		this.data=data;		
	}
	
	@Override
	public void setHeader(Header h) {
		this.h=h;		
	}

}
