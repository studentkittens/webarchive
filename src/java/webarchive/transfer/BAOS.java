package webarchive.transfer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import webarchive.client.ClientConnectionHandler;

public class BAOS extends ByteArrayOutputStream {
	
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
