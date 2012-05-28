package webarchive.transfer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import webarchive.client.ClientConnectionHandler;
import webarchive.headers.Header;

public class MyBAOS extends ByteArrayOutputStream {
	
	private Buffer buf;
	private ClientConnectionHandler cH;
	public MyBAOS (Buffer buf, ClientConnectionHandler cH) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Message answer = cH.waitForAnswer(msg);
		if(answer.getHeader() != Header.SUCCESS)
		{
			//EXCEPTION HANDLING UND SO
		}
	}
	
}
