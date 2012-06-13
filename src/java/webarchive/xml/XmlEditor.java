package webarchive.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import webarchive.api.XmlEdit;
import webarchive.client.Client;
import webarchive.connection.Connection;
import webarchive.transfer.Header;
import webarchive.transfer.Message;

/**
 *
 * @author ccwelich
 */
//TODO finish implementation
//TODO tests
//TODO finish javadoc
public class XmlEditor implements XmlEdit {

	private Document document;
	private Element data;
	private XmlConf conf;

	XmlEditor(Document document, XmlConf conf) {
		this.document = document;
		data = (Element) document.getElementsByTagName(conf.prefix("data")).item(
			0);
		this.conf = conf;
	}

	public Element createElement(String tagName) {
		tagName = conf.prefix(tagName);
		return document.createElementNS(conf.getNamespace(), tagName);
	}

	public DataElement createDataElement(String tagName) {
		tagName = conf.prefix(tagName);
		return new DataElement(document.createElementNS(conf.getNamespace(),
			tagName), true);
	}

	public DataElement getDataElement(String tagName) {
		tagName = conf.prefix(tagName);
		NodeList list = data.getChildNodes();
		final int length = list.getLength();
		for (int i = 0; i < length; i++) {
			if (list.item(i).getNodeName().equals(tagName)) {
				return new DataElement((Element) list.item(i), false);
			}
		}
		return null;
	}

	public void addDataElement(DataElement e) throws NullPointerException,
		IllegalArgumentException {
		if (e == null) {
			throw new NullPointerException();
		}
		if (!e.canWrite()) {
			throw new IllegalArgumentException("write protected");
		}
		//TODO send e to XmlHandler in server
//		Client cl = Client.getInstance();
//		Connection c = cl.getConnection();
//		Message msg = new Message(Header.GETXMLEDIT,this);
//		msg.setNoAnswer();
//		try {
//			c.send(msg);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
	}

	public String prefix(String name) {
		return conf.prefix(name);
	}
}
