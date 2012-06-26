package webarchive.server;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import webarchive.api.model.MetaData;
import webarchive.api.select.Select;
import webarchive.xml.DataElement;
import webarchive.api.xml.XmlEditor;
import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.connection.NetworkModule;
import webarchive.dbaccess.SqlHandler;
import webarchive.handler.Handlers;
import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Header;
import webarchive.transfer.Message;
import webarchive.xml.XmlConf;
import webarchive.xml.XmlHandler;
import webarchive.xml.XmlMethodFactory;

public class ServerConnectionHandler extends ConnectionHandler {
	
	private FileHandler io;
	private SqlHandler sql;
	private LockHandler locker;
	
	public ServerConnectionHandler(Connection c, NetworkModule netMod) {
		super(c, netMod);
		this.io = (FileHandler) Handlers.get(FileHandler.class);
		this.sql = (SqlHandler) Handlers.get(SqlHandler.class);
		this.locker = (LockHandlerImpl) Handlers.get(LockHandlerImpl.class);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void handle(Message msg) {
		
		switch (msg.getHeader()) {
			case SUCCESS:
			case HANDSHAKE: {
				wakeUp(msg);
			}
			break;
			case EXCEPTION: {
			}
			break;
			case SQL: {
				List<MetaData> list = null;
				try {
					list = sql.select((Select) msg.getData());
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message answer = new Message(msg, list);
				try {
					send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			case WRITEFILE: {
				FileBuffer buf = (FileBuffer) msg.getData();
				locker.lock(buf.getFd());
				io.write(buf);
				locker.unlock(buf.getFd());
				Message answer = new Message(msg, null);
				answer.setHeader(Header.SUCCESS);
				try {
					send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			break;
			case READFILE: {
				FileDescriptor fd = (FileDescriptor) msg.getData();
				
				locker.lock(fd);
				FileBuffer buf = io.read(fd);
				locker.unlock(fd);
				Message answer = new Message(msg, buf);
				try {
					send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			case GETXMLEDIT: {
				FileDescriptor fd = (FileDescriptor) msg.getData();
				XmlHandler xmlH;
				Message answer;
				try {
					xmlH = getXmlHandler(fd);
					XmlEditor xmlEd = xmlH.newEditor();
					answer = new Message(msg, xmlEd);
					answer.setHeader(Header.GETXMLEDIT);
					
				} catch (SAXException ex) {
					answer = new Message(msg, ex);
					answer.setHeader(Header.EXCEPTION);
				}
				
				try {
					send(answer);
				} catch (Exception ex) {
					Logger.getLogger(ServerConnectionHandler.class.getName()).
						log(Level.SEVERE, null, ex);
				}
			}
			break;
			case ADDXMLEDIT: {
				DataElement element = (DataElement) msg.getData();
				FileDescriptor fd = (FileDescriptor) msg.getData();
				Message answer;
				try {					
					XmlHandler xmlH = getXmlHandler(fd);
					xmlH.addDataElement(element);
					answer = new Message(msg, xmlH.getDocument());
					answer.setHeader(Header.ADDXMLEDIT);
				} catch (Exception ex) {
					answer = new Message(msg, ex);
					answer.setHeader(Header.EXCEPTION);
				}
				try {
					send(answer);
				} catch (Exception ex) {
					Logger.getLogger(ServerConnectionHandler.class.getName()).
						log(Level.SEVERE, null, ex);
				}
				
			}
			break;
			case LS: {
				MetaData meta = (MetaData) msg.getData();
				FileDescriptor tmp = new FileDescriptor(meta, null);
				locker.lock(tmp);
				List<File> list = io.getFileTree(meta);
				locker.unlock(tmp);
				Message answer = new Message(msg, list);
				try {
					send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			case REGISTER_OBSERVER: {
				List<Connection> l = Server.getInstance().getObservers();
				synchronized (l) {
					l.remove(c);
					l.add(c);
				}
				Message answer = new Message(msg, null);
				answer.setHeader(Header.SUCCESS);
				try {
					send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			case DELETE_OBSERVER: {
				List<Connection> l = Server.getInstance().getObservers();
				synchronized (l) {
					l.remove(c);
				}
				Message answer = new Message(msg, null);
				answer.setHeader(Header.SUCCESS);
				try {
					send(answer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			default:
				break;
		}
	}
	
	private XmlHandler getXmlHandler(FileDescriptor fd) throws SAXException {
		XmlHandler xmlH = null;
		xmlH = ((XmlMethodFactory) Handlers.get(
			XmlMethodFactory.class)).newXmlHandler(fd);
		
		return xmlH;
	}
	
	@Override
	public void send(Message msg) throws Exception {
		c.send(msg);
	}
}
