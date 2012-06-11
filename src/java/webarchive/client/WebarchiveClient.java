package webarchive.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import webarchive.api.WebarchiveObserver;
import webarchive.api.XmlEdit;
import webarchive.api.model.MetaData;
import webarchive.api.select.Select;
import webarchive.headers.Header;
import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Message;
import webarchive.transfer.MyBAOS;

public class WebarchiveClient implements webarchive.api.WebarchiveClient {

	private Client cl;
	private ClientConnectionHandler cH;
	
	protected WebarchiveClient() {
		cl = new Client();
		cH = (ClientConnectionHandler) cl.getConnection().getConHandler();
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
		return new MyBAOS(new FileBuffer(new FileDescriptor(meta,file)),cH);
	}

	@Override
	public XmlEdit getXMLEdit(MetaData meta) throws Exception {
		Object answer = queryServer(Header.XMLEDIT,meta);
		assert answer instanceof XmlEdit;
		return (XmlEdit) answer;
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
		cH.send(msg);
		return  cH.waitForAnswer(msg).getData();
	}

}
