package webarchive.server;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import webarchive.connection.Connection;
import webarchive.connection.ConnectionHandler;
import webarchive.connection.NetworkModule;
import webarchive.dbaccess.SqlHandler;
import webarchive.dbaccess.SqliteAccess;
import webarchive.handler.Handlers;
import webarchive.init.ConfigHandler;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Message;
import webarchive.xml.XmlConf;
import webarchive.xml.XmlHandler;
import webarchive.xml.XmlMethodFactory;

public class ServerConnectionHandler extends ConnectionHandler {
	
	//Handlers
	private FileHandler io;
	private SqlHandler sql;
	private XmlMethodFactory xmlMeth;
	private LockHandler locker;
	
	//Processors
	private Map<String,MessageProcessor> processors;
	
	private static final String SQL = "Sql";
	private static final String ADDXML = "AddXml";
	private static final String GETXML = "GetXml";
	private static final String LS = "Ls";
	private static final String READ = "Read";
	private static final String WRITE = "Write";
	private static final String REGISTER = "ResgisterObserver";
	private static final String DELETE = "DeleteObserver";
	private static final String HANDSHAKER= "HandShake";

	public ServerConnectionHandler(Connection c, NetworkModule netMod) {
		super(c, netMod);
		System.out.println("Creating handlers for new connection");
		Handlers col = ((Server)netMod).getCollection();
		System.out.println("creating FileHandler");
		this.io = col.get(FileHandler.class);
		
		 String absPathDb = FileDescriptor.root+"/"+((ConfigHandler) col.get(ConfigHandler.class)).getValue("webarchive.db.path");
		 System.out.println(absPathDb);
		 System.out.println(FileDescriptor.root);
		 System.out.println("creating sqlHandler");
		this.sql = (new SqlHandler(new SqliteAccess(new File(absPathDb))));
		System.out.println("creating lockhandler");
		try {
			this.locker = new LockHandlerImpl(InetAddress.getLocalHost(), 
					new Integer(col.get(ConfigHandler.class).getValue("webarchive.javadapter.port")));
		} catch (NumberFormatException | UnknownHostException e) {
			Logger.getLogger(ServerConnectionHandler.class.getName()).log(Level.SEVERE, null, e);
		}
		System.out.println("creating xmlhandler");
		this.xmlMeth = new XmlMethodFactory(this.locker,col.get(XmlConf.class));
		System.out.println("creating processors");
		this.processors=new HashMap<String,MessageProcessor>();
		
		processors.put(SQL, new SqlProcessor());
		processors.put(ADDXML, new AddXmlProcessor());
		processors.put(GETXML, new GetXmlProcessor());
		processors.put(LS, new LsProcessor());
		processors.put(READ, new ReadProcessor());
		processors.put(WRITE, new WriteProcessor());
		processors.put(REGISTER, new RegisterObserverProcessor());
		processors.put(DELETE, new DeleteObserverProcessor());
		processors.put(HANDSHAKER, new HandshakeProcessor());
		
	}
	
	@Override
	public void handle(Message msg) {
		
		switch (msg.getHeader()) {
			case SUCCESS:
				break;
			case HANDSHAKE: 
				processors.get(HANDSHAKER).process(msg, this);
			break;
			case EXCEPTION: {
			}
			break;
			case SQL: 
				processors.get(SQL).process(msg, this);
			break;
			case WRITEFILE: 
				processors.get(WRITE).process(msg, this);
			break;
			case READFILE: 
				processors.get(READ).process(msg, this);
			break;
			case GETXMLEDIT: 
				processors.get(GETXML).process(msg, this);
			break;
			case ADDXMLEDIT: 
				processors.get(ADDXML).process(msg, this);
			break;
			case LS: 
				processors.get(LS).process(msg, this);
			break;
			case REGISTER_OBSERVER: 
				processors.get(REGISTER).process(msg, this);
			break;
			case DELETE_OBSERVER: 
				processors.get(DELETE).process(msg, this);
			break;
			default:
				break;
		}
	}
	
	XmlHandler getXmlHandler(FileDescriptor fd) throws SAXException {
		XmlHandler xmlH = null;
		xmlH = xmlMeth.newXmlHandler(fd);

		return xmlH;
	}
	
	@Override
	public void send(Message msg) throws Exception {
		c.send(msg);
	}

	public SqlHandler getSql() {
		return sql;
	}
	public LockHandler getLocker() {
		return locker;
	}
	public XmlMethodFactory getXmlMeth() {
		return xmlMeth;
	}
	public FileHandler getIo() {
		return io;
	}
}
