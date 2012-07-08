package webarchive.transfer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import webarchive.client.ClientConnectionHandler;
/**
 * A special ByteArrayOutputStream that after is closed invokes a finish-sequence leading to sending a FileBuffer to the Server.
 * An instance of this Class is returned when calling getOutputStream() in the Webarchive-API.
 * The Client can just use this outputstream as if it were a direct stream to the webarchive.
 * Upon Closing the stream a new Message with the contents of the Stream is created and sent to the Server.
 * This way the Caller of getOutputStream is not confornted with sepcial method calls, but just uses a standard stream to write Files into the Archive.
 * @author Schneider
 *
 */
public class BAOS extends ByteArrayOutputStream  implements Serializable{
	
	private FileBuffer buf;
	private ClientConnectionHandler cH;
	public BAOS (FileBuffer buf, ClientConnectionHandler cH) {
		this.buf=buf;
		this.cH=cH;
	}
	/**
	 * MUST be called to actually send the new Data to the Archive.
	 */
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
