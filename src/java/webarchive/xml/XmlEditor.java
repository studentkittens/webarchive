package webarchive.xml;

import java.io.File;
import java.io.Serializable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import webarchive.api.xml.TagName;
import webarchive.client.Client;
import webarchive.connection.Connection;
import webarchive.handler.Handlers;
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
public class XmlEditor implements webarchive.api.xml.XmlEditor, Serializable {

	private Document document;
	private Element dataNode; 

	/**
	 * create XmlEditor
	 *
	 * @param document document used for editing
	 * @param conf config data
	 */
	XmlEditor(org.w3c.dom.Document document, org.w3c.dom.Element dataNode) {
		assert document != null;
		assert dataNode!=null;
		this.document = document;
		this.dataNode = dataNode;
	}


	private void updateDocument(Document document) {
		this.document = document;
		dataNode = (Element) document.getElementsByTagName(
			TagName.NAMESPACE.toString()).item(0);
	}

	@Override
	public Element createElement(TagName tagName) {
		return document.createElementNS(TagName.NAMESPACE, tagName.getName());
	}

	@Override
	public DataElement createDataElement(TagName tagName) {
		return new DataElement(
			document.createElementNS(TagName.NAMESPACE.toString(), tagName.getName()), true);
	}

	@Override
	public DataElement getDataElement(TagName tagName) {
		NodeList list = dataNode.getChildNodes();
		final int length = list.getLength();
		for (int i = 0; i < length; i++) {
			if (list.item(i).getNodeName().equals(tagName.getName())) {
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
		Message msg = new Message(Header.ADDXMLEDIT, element);
		c.send(msg);
		Message answer = c.waitForAnswer(msg);
		updateDocument((Document) answer.getData());
	}

}
