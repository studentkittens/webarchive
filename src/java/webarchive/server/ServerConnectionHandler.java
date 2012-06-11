package webarchive.server;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.connection.NetworkModule;
import webarchive.headers.Header;
import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Message;

public class ServerConnectionHandler extends ConnectionHandler {

	private IOHandler io;
	public ServerConnectionHandler(Connection c, NetworkModule netMod) {
		super(c, netMod);
		this.io = new FileHandler();
	}

	@Override
	public void handle(Message msg) {

		switch (msg.getHeader()) {
			case HANDSHAKE: 
			{
				wakeUp(msg);
			}
				break;
			case EXCEPTION:
			{
				
			}
				break;
			case SUCCESS:
			{
				
			}
				break;
			case SQL:
			{
				
			}
				break;
			case WRITEFILE:
			{
				FileBuffer buf = (FileBuffer)msg.getData();
				io.lock(buf.getFd());
				io.write(buf);
				io.unlock(buf.getFd());
				Message answer = new Message(msg,null);
				answer.setHeader(Header.SUCCESS);
				try {
					c.send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
				break;
			case READFILE:
			{
				FileDescriptor fd = (FileDescriptor)msg.getData();
				io.lock(fd);
				FileBuffer buf = io.read(fd);
				io.unlock(fd);
				Message answer = new Message(msg,buf);
				try {
					c.send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				break;
			case XMLEDIT:
			{
				
			}
				break;
			case LS:
			{
				
			}
				break;
			default:
				break;
		}
	}

	@Override
	public void send(Message msg) throws Exception {
		c.send(msg);		
	}
	
	
}
