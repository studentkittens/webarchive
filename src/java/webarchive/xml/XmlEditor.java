package webarchive.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

	@Override
	public String addPrefixTo(String name) {
		return conf.addPrefixTo(name);
	}
}
