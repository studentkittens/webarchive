package webarchive.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import webarchive.client.Client;
import webarchive.connection.Connection;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

/**
 * XmlEditor is used on client-side to read and add DataElements.
 *
 * @see webarchive.api.xml.XmlEditor for interface details
 * @author ccwelich
 */
//TODO finish implementation
//TODO tests
public class XmlEditor implements webarchive.api.xml.XmlEditor {


	private Document document;
	private Element data; 	// the data-node

	private XmlConf conf;

	/**
	 * create XmlEditor
	 *
	 * @param document document used for editing
	 * @param conf config data
	 */
	XmlEditor(Document document, XmlConf conf) {
		this.document = document;
		data = (Element) document.getElementsByTagName(
			conf.addPrefixTo("data")).item(0);
		this.conf = conf;
	}

	@Override
	public Element createElement(String tagName) {
		tagName = conf.addPrefixTo(tagName);
		return document.createElementNS(conf.getNamespace(), tagName);
	}

	@Override
	public DataElement createDataElement(String tagName) {
		tagName = conf.addPrefixTo(tagName);
		return new DataElement(
			document.createElementNS(conf.getNamespace(), tagName), true);
	}

	@Override
	public DataElement getDataElement(String tagName) {
		tagName = conf.addPrefixTo(tagName);
		NodeList list = data.getChildNodes();
		final int length = list.getLength();
		for (int i = 0; i < length; i++) {
			if (list.item(i).getNodeName().equals(tagName)) {
				return new DataElement((Element) list.item(i), false);
			}
		}
		return null;
	}

	@Override
	public void addDataElement(DataElement element) throws NullPointerException,
		IllegalArgumentException, 
		Exception {
		if (element == null) {
			throw new NullPointerException();
		}
		if (!element.canWrite()) {
			throw new IllegalArgumentException("write protected");
		}
		// send element to XmlHandler in server
		Client cl = Client.getInstance();
		Connection c = cl.getConnection();
		Message msg = new Message(Header.ADDXMLEDIT,element);
		// TODO return updated document
		c.send(msg);
	}

	@Override
	public String addPrefixTo(String name) {
		return conf.addPrefixTo(name);
	}

	@Override
	public void close() throws Exception {
		Connection con = Client.getInstance().getConnection();
		Message msg = new Message(Header.CLOSE_XMLEDIT, null);
		msg.setNoAnswer();
		con.send(msg);
	}
}
