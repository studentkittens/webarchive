package webarchive.xml;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DataElement used in client.
 *
 * @see webarchive.api.xml.DataElement for interface details.
 * @author ccwelich
 */
//TODO tests
public class DataElement implements webarchive.api.xml.DataElement {

	private Element dataElement;
	private boolean canWrite;

	/**
	 * package protected use only
	 *
	 * @param dataElement the element to wrap as a dataElement
	 * @param canWriteInto set false if this dataElement is write-protected
	 */
	DataElement(Element dataElement, boolean canWriteInto) {
		assert dataElement != null;
		this.dataElement = dataElement;
		this.canWrite = canWriteInto;
	}

	@Override
	public Node appendChild(Node node) throws DOMException {
		return dataElement.appendChild(node);
	}

	@Override
	public boolean canWrite() {
		return canWrite;
	}

	/**
	 * package protected use only
	 *
	 * @return the wrapped dataElement
	 */
	Element getDataElement() {
		return dataElement;
	}

	@Override
	public NodeList getChildNodes() {
		return dataElement.getChildNodes();
	}

	@Override
	public NodeList getElementsByTagName(String string) {
		return dataElement.getElementsByTagName(string);
	}
}
