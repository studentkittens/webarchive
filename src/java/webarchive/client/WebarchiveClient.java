package webarchive.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Observable;

import webarchive.api.WebarchiveObserver;
import webarchive.api.model.CommitTag;
import webarchive.api.model.MetaData;
import webarchive.api.select.Select;
import webarchive.api.xml.XmlEditor;
import webarchive.connection.Connection;
import webarchive.transfer.*;
/**
 * This WebarchiveClient Class delegates Webarchive-API-Calls to the NetworkModule Client and returns the answer to the caller.
 * @author Schneider
 *
 */
public class WebarchiveClient extends Observable implements webarchive.api.WebarchiveClient {

	private Client cl;
	private Connection con;
	
	public WebarchiveClient(InetAddress server, int port) throws IOException {
		cl = new Client();
		cl.setIp(server);
		cl.setPort(port);
		cl.connectToServer();
		con = cl.getConnection();
	}
	
	@Override
	public <T> List<T> select(Select<T> select) throws Exception {
		Object answer = queryServer(Header.SQL,select);
		return (List<T>) answer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<File> getFileList(MetaData meta) throws Exception {
		Object answer = queryServer(Header.LS,meta);
		assert answer instanceof List<?>;
		return (List<File>) answer;
	}

	@Override
	public InputStream getInputStream(MetaData meta, File file)
			throws Exception {
		
		Object answer = queryServer(Header.READFILE,new FileDescriptor(meta,file));
		assert answer instanceof FileBuffer; 
		return new ByteArrayInputStream(((FileBuffer) answer).getData());
	}

	@Override
	public OutputStream getOutputStream(MetaData meta, File file)
			throws Exception {
		return new BAOS(new FileBuffer(new FileDescriptor(meta,file)),(ClientConnectionHandler) con.getConHandler());
	}

	@Override
	public XmlEditor getXMLEditor(MetaData meta) throws Exception {
		Object answer = queryServer(Header.GETXMLEDIT,new FileDescriptor(meta,new File("data.xml")));
		assert answer instanceof XmlEditor;
		return (XmlEditor) answer;
	}

	@Override
	public void addObserver(WebarchiveObserver o) throws Exception {
		queryServer(Header.REGISTER_OBSERVER,null);
		cl.setObservable(this);
		super.addObserver(o);
	}
	
	void notifyClients(List<CommitTag> list) {
		super.setChanged();
		super.notifyObservers(list);
	}
	
	@Override
	public int countObservers() {
		return super.countObservers();
	}

	@Override
	public void deleteObserver(WebarchiveObserver o) throws Exception {
		queryServer(Header.DELETE_OBSERVER,null);
		cl.setObservable(null);
		super.deleteObserver(o);
	}

	@Override
	public void deleteObservers()  {
		try {
			queryServer(Header.DELETE_OBSERVER,null);
		} catch (Exception e) {
			//DONOTHING
		}
		cl.setObservable(null);
		super.deleteObservers();

	}
	/**
	 * Convenience Method, mainly used for disconnecting the NetworkModule
	 * @return instance of NetMod Client
	 */
	public Client getClient() {
		return cl;
	}
	/**
	 * 
	 * @param head Header enumaration, which defines the type of Message to be sent
	 * @param toSend THe Object, which shall be sent to the Server
	 * @return the Data-Object, which was received from the server as an answer
	 * @throws Exception the Exception, which the Server sent to the Client, indicating a problem with the prevous request
	 */
	private Object queryServer(Header head, Object toSend) throws Exception {
		Message msg = new Message(head, toSend);
		con.send(msg);
		Message answer = (Message) con.waitForAnswer(msg);
		if(answer.getHeader() == Header.EXCEPTION)
			throw (Exception) answer.getData();
		return  answer.getData();
	}

}
