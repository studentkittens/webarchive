package webarchive.xml;

import java.io.Serializable;
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
public class XmlEditor implements webarchive.api.xml.XmlEditor, Serializable {

	private final String dataTagName;
	private Document document;
	private Element dataNode; 	// the data-node
	private final String prefix;
	private final String namespace;

	/**
	 * create XmlEditor
	 *
	 * @param document document used for editing
	 * @param conf config data
	 */
	XmlEditor(Document document) {
		assert document != null;
		final XmlConf conf = XmlMethodFactory.getInstance().getConf();
		prefix = conf.getPrefix();
		dataTagName = conf.getDataTag();
		namespace = conf.getNamespace();
		setDocument(document);
	}

	private void setDocument(Document document) {
		this.document = document;
		dataNode = (Element) document.getElementsByTagName(
			addPrefixTo(dataTagName)).item(0);
	}

	@Override
	public Element createElement(String tagName) {
		tagName = addPrefixTo(tagName);
		return document.createElementNS(namespace, tagName);
	}

	@Override
	public DataElement createDataElement(String tagName) {
		tagName = addPrefixTo(tagName);
		return new DataElement(
			document.createElementNS(namespace, tagName), true);
	}

	@Override
	public DataElement getDataElement(String tagName) {
		tagName = addPrefixTo(tagName);
		NodeList list = dataNode.getChildNodes();
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
		Message msg = new Message(Header.ADDXMLEDIT, element);
		c.send(msg);
		Message answer = c.waitForAnswer(msg);
		setDocument((Document) answer.getData());
	}

	@Override
	public final String addPrefixTo(String name) {
		assert prefix.endsWith(":");
		int i = name.indexOf(':');
		return prefix + ((i != -1) ? name : name.substring(i + 1));
	}
}
