package webarchive.xml;

import java.util.Objects;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DataElement implementation used in client.
 *
 * @see webarchive.api.xml.DataElement for interface details.
 * @author ccwelich
 */
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
	public String toString() {
		return "DataElement{" + "name=" + dataElement.getTagName() + ", canWrite=" + canWrite+'}';
	}

	@Override
	public Node appendChild(Node node) throws DOMException,
		IllegalArgumentException {
		if (!canWrite) {
			throw new IllegalArgumentException("write protected");
		}
		return dataElement.appendChild(node);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DataElement other = (DataElement) obj;
		if (!isEqualNode(other)) {
			return false;
		}
		if (this.canWrite != other.canWrite) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 73 * hash + Objects.hashCode(this.dataElement);
		hash = 73 * hash + (this.canWrite ? 1 : 0);
		return hash;
	}

	
	@Override
	public boolean isEqualNode(DataElement e) {
		if (e == null) {
			return false;
		}
		return dataElement.isEqualNode(e.dataElement);
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
