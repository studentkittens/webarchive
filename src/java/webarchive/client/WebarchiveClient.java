package webarchive.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Observable;

import webarchive.api.WebarchiveObserver;
import webarchive.api.model.CommitTag;
import webarchive.api.model.MetaData;
import webarchive.api.select.Select;
import webarchive.api.xml.XmlEditor;
import webarchive.connection.Connection;
import webarchive.transfer.*;

public class WebarchiveClient extends Observable implements webarchive.api.WebarchiveClient {

	private Client cl;
	private Connection con;
	
	protected WebarchiveClient() {
		cl = Client.getInstance();
		con = cl.getConnection();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MetaData> select(Select select) throws Exception {
		Object answer = queryServer(Header.SQL,select);
		assert answer instanceof List<?>;
		return (List<MetaData>) answer;
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
		return new MyBAOS(new FileBuffer(new FileDescriptor(meta,file)),(ClientConnectionHandler) con.getConHandler());
	}


	public XmlEditor getXMLEdit(MetaData meta) throws Exception {
		Object answer = queryServer(Header.GETXMLEDIT,new FileDescriptor(meta,null //TODO));
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
	
	
	private Object queryServer(Header head, Object toSend) throws Exception {
		Message msg = new Message(head, toSend);
		con.send(msg);
		return  con.waitForAnswer(msg).getData();
	}

}
