package webarchive.xml;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import webarchive.api.xml.TagName;
import webarchive.client.Client;
import webarchive.client.ClientConnectionHandler;
import webarchive.connection.Connection;
import webarchive.transfer.DataElementContainer;
import webarchive.transfer.FileDescriptor;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

/**
 * XmlEditor is used on client-side to read and add DataElements.
 * usually XmlEditor is created by {@link XmlHandler}
 * @see webarchive.api.xml.XmlEditor for interface details
 * @author ccwelich
 */
public class XmlEditor implements webarchive.api.xml.XmlEditor, Serializable {

	private Document document;
	private Element dataNode;
	private ClientAdapter client;
	private ClientConnectionHandler connection;
	private FileDescriptor xmlFile;

	/**
	 * create XmlEditor
	 * @param fileDescriptor 
	 *
	 * @param document document used for editing
	 * @param conf config data
	 */
	XmlEditor(FileDescriptor fileDescriptor, org.w3c.dom.Document document, org.w3c.dom.Element dataNode) {
		assert document != null;
		assert dataNode != null;
		assert fileDescriptor != null;
		this.document = document;
		this.dataNode = dataNode;
		this.xmlFile = fileDescriptor;
		this.client = new DefaultClientAdapter();
	}

	/**
	 * sets the clientAdapter. Usage: testing.
	 *
	 * @param client
	 */
	void setClient(ClientAdapter client) {
		this.client = client;
	}

	private void updateDocument(Document document) {
		if(document!=null) {
		this.document = document;
		dataNode = (Element) document.getElementsByTagName(
			TagName.DATA_TAG.toString()).item(0);
		}
	}

	@Override
	public Element createElement(TagName tagName) {
		return document.createElementNS(TagName.NAMESPACE, tagName.
			getAbsoluteName());
	}

	@Override
	public DataElement createDataElement(TagName tagName) {
		return new DataElement(
			document.createElementNS(TagName.NAMESPACE.toString(), tagName.
			getAbsoluteName()), true);
	}

	@Override
	public DataElement getDataElement(TagName tagName) {
		NodeList list = dataNode.getChildNodes();
		final int length = list.getLength();
		for (int i = 0; i < length; i++) {
			if (list.item(i).getNodeName().equals(tagName.getAbsoluteName())) {
				return new DataElement((Element) list.item(i), false);
			}
		}
		return null;
	}

	@Override
	public void addDataElement(webarchive.api.xml.DataElement element) throws Exception {
		
		// send element to XmlHandler in server
		updateDocument(client.syncDocument(element));
	}

	/**
	 * The default ClientAdapter. connects with webarchive.client.Client
	 * singleton.
	 */
	class DefaultClientAdapter implements ClientAdapter {

		@Override
		public Document syncDocument(webarchive.api.xml.DataElement element) throws Exception {
			Message msg = new Message(Header.ADDXMLEDIT, new DataElementContainer((DataElement) element, xmlFile));
			try {
				connection.send(msg);
			} catch (Exception ex) {
				Logger.getLogger(XmlEditor.class.getName()).
					log(Level.SEVERE, null, ex);
			}
			Message answer = connection.waitForAnswer(msg,connection.getConnection());
			if(answer.getHeader()==Header.EXCEPTION) throw (Exception) answer.getData();
			return (Document) answer.getData();
		}
	}

	public void setConnection(ClientConnectionHandler clientConnectionHandler) {
		this.connection = clientConnectionHandler;
	}
}
