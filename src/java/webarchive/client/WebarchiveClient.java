package webarchive.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import webarchive.api.WebarchiveObserver;
import webarchive.api.model.MetaData;
import webarchive.api.select.Select;
import webarchive.api.xml.XmlEditor;
import webarchive.connection.Connection;
import webarchive.transfer.*;

public class WebarchiveClient implements webarchive.api.WebarchiveClient {

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
		Object answer = queryServer(Header.GETXMLEDIT,meta);
		assert answer instanceof XmlEditor;
		return (XmlEditor) answer;
	}

	@Override
	public void addObserver(WebarchiveObserver o) {
		// TODO Auto-generated method stub

	}

	@Override
	public int countObservers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteObserver(WebarchiveObserver o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteObservers() {
		// TODO Auto-generated method stub

	}
	
	
	private Object queryServer(Header head, Object toSend) throws Exception {
		Message msg = new Message(head, toSend);
		con.send(msg);
		return  con.waitForAnswer(msg).getData();
	}

}
