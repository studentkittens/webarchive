package webarchive.server;

import java.sql.SQLException;
import java.util.List;

import webarchive.api.model.MetaData;
import webarchive.api.select.Select;
import webarchive.api.xml.XmlEditor;
import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.connection.NetworkModule;
import webarchive.dbaccess.SqlHandler;
import webarchive.handler.HandlerCollection;
import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Header;
import webarchive.transfer.Message;
import webarchive.xml.XmlHandler;

public class ServerConnectionHandler extends ConnectionHandler {

	private FileHandler io;
	private SqlHandler sql;
	public ServerConnectionHandler(Connection c, NetworkModule netMod) {
		super(c, netMod);
		this.io = (FileHandler) netMod.getHandlers().get("FileHandler");
		this.sql = (SqlHandler) netMod.getHandlers().get("SqlHandler");
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
				List<MetaData> list =null;
				try {
					list = sql.select((Select)msg.getData());
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message answer = new Message(msg,list);
				try {
					send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
					send(answer);
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
					send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				break;
			case GETXMLEDIT:
			{
				XmlHandler xml;
				XmlEditor xmlEd = null;
				//TODO XMLHANDLING
				//<-
				
				//ccwelich
				
				//->
				Message answer = new Message(msg,xmlEd);
				try {
					send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				break;
			case ADDXMLEDIT:
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
