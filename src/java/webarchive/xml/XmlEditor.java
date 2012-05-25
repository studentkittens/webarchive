package webarchive.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ccwelich
 */
//TODO finish implementation
//TODO tests
//TODO finish javadoc
public class XmlEditor {

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
		for (int i = 0; i < list.getLength(); i++) {
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
	}

	public String prefix(String name) {
		return conf.prefix(name);
	}
}
