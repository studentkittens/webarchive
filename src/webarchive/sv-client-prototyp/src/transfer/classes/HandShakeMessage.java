package transfer.classes;

import headers.Header;
import headers.classes.HandShakeHeader;
import transfer.Transferable;

public class HandShakeMessage implements Transferable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7455459151713375986L;

	private final Header h;
	private final Object data;
	public HandShakeMessage()
	{
		this.h = new HandShakeHeader();
		this.data = null;
	}
	@Override
	public Header getHeader() {
		// TODO Auto-generated method stub
		return h;
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return data;
	}

}
