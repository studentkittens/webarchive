package webarchive.xml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ccwelich
 */
//TODO tests
//TODO finish javadoc
public class DataElement {

	private Element dataElement;
	private boolean canWrite;

	DataElement(Element dataElement, boolean canWriteInto) {// package protected use only
		this.dataElement = dataElement;
		this.canWrite = canWriteInto;
	}

	public Node appendChild(Node node) throws DOMException {
		if (canWrite) {
			return dataElement.appendChild(node);
		} else {
			return null;
	
		}
	}

	boolean canWrite() {
		return canWrite;
	}

	Element getDataElement() { // package protected use only
		return dataElement;
	}

	public String getTextContent() throws DOMException {
		return dataElement.getTextContent();
	}

	public NodeList getChildNodes() {
		return dataElement.getChildNodes();
	}

	public NodeList getElementsByTagName(String string) {
		return dataElement.getElementsByTagName(string);
	}
}
