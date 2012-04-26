package transfer;

import headers.Header;

import java.io.Serializable;


public interface Transferable extends Serializable {
	
	public Header getHeader();
	public Object getData();
	
}
