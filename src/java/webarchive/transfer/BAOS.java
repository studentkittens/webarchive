package webarchive.transfer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import webarchive.client.ClientConnectionHandler;

public class BAOS extends ByteArrayOutputStream  implements Serializable{
	
	private FileBuffer buf;
	private ClientConnectionHandler cH;
	public BAOS (FileBuffer buf, ClientConnectionHandler cH) {
		this.buf=buf;
		this.cH=cH;
	}
	
	@Override
	public void close() throws IOException
	{
		super.close();
		buf.finish(this);
		Message msg = new Message(Header.WRITEFILE,buf);
		try {
			cH.send(msg);
		} catch (Exception e) {
			if(e instanceof IOException )
				throw (IOException)e;
			e.printStackTrace();
		}
		
		Message answer = cH.waitForAnswer(msg,cH.getConnection());
		if(answer.getHeader() == Header.EXCEPTION)
		{
			Exception e = (Exception)answer.getData();
			if(e instanceof IOException )
				throw (IOException)e;
			e.printStackTrace();
		}
	}
	
}
