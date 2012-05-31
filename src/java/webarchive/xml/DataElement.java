package webarchive.xml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * java representation of data-element in xml. New org.w3c.dom.Elements may be
 * added as children, but DataElement can only be stored if is not
 * write-protected, which is if canWrite() returns true. 
 * @see XmlEditor
 *
 * @author ccwelich
 */
//TODO tests
public class DataElement {

	private Element dataElement;
	private boolean canWrite;

	DataElement(Element dataElement, boolean canWriteInto) {// package protected use only
		assert dataElement != null;
		this.dataElement = dataElement;
		this.canWrite = canWriteInto;
	}

	/**
	 * @see org.w3c.dom.Node#appendChild(org.w3c.dom.Node) 
	 */
	public Node appendChild(Node node) throws DOMException {
		return dataElement.appendChild(node);
	}

	public boolean canWrite() {
		return canWrite;
	}

	Element getDataElement() { // package protected use only
		return dataElement;
	}

	/**
	 * @see org.w3c.dom.Node#getChildNodes() 
	 */
	public NodeList getChildNodes() {
		return dataElement.getChildNodes();
	}

	/**
	 * @see org.w3c.dom.Element#getElementsByTagName(java.lang.String) 
	 */
	public NodeList getElementsByTagName(String string) {
		return dataElement.getElementsByTagName(string);
	}
}
