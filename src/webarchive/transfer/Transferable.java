package webarchive.transfer;

import webarchive.headers.Header;

import java.io.Serializable;


public interface Transferable extends Serializable {
	
	public Header getHeader();
	public Object getData();
	
}
