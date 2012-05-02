package transfer.classes;

import headers.Header;
import headers.classes.HandShakeHeader;
import transfer.Transferable;

public class Message implements Transferable {

	private static final long serialVersionUID = 7455459151713375986L;

	private final Header h;
	private final Object data;
	public Message()
	{
		this.h = new HandShakeHeader();
		this.data = null;
	}
	@Override
	public Header getHeader() {
		return h;
	}

	@Override
	public Object getData() {
		return data;
	}

}
