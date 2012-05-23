package webarchive.api;

import org.w3c.dom.Element;
//TODO implementation by c2 
/**
 * XmlEdit provides methods to find and data-elements in a webarchive XML-File.
 * There can also be added new Elements to the data-node.
 *
 * @author ccwelich
 */
public interface XmlEdit {

	/**
	 * get an element by its tagname. The method iterates over the first level
	 * of the data node and returnes a node if the element-name equals tagName.
	 *
	 * @param tagName key to find element.
	 * @return the element, or null if there was no element with tagName
	 */
	public Element getElement(String tagName);

	/**
	 * add a element by a tagname. The method adds an element to the data node
	 * of the XML-file. TagNames assert unique in the first level of data-Node.
	 *
	 * @param tagName key to add element
	 * @param content body of the new element. Note that the enclosing
	 * element-tags will be generated automatically.
	 * @throws Exception if dublicate was found, or the connection failed.
	 */
	public void addElement(String tagName, String content) throws Exception;
}
