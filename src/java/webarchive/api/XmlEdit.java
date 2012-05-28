package webarchive.api;

import org.w3c.dom.Element;
import webarchive.xml.DataElement;

/**
 * XmlEdit provides methods to find and add data-elements in a webarchive XML-File.
 *
 * @author ccwelich
 */
public interface XmlEdit {

	/**
	 * get a data element from the data subtree by its tagname. The method
	 * returns a write protected DataElement.
	 *
	 * @param tagName key to find element.
	 * @return the element, or null if there was no element with tagName
	 */
	public DataElement getDataElement(String tagName);

	/**
	 * add a DataElement. The method adds an element to first level of the data
	 * node of the XML-file. TagNames are asserted as unique in the first level
	 * of data-Node.
	 *
	 * @param e DataElement to add.
	 * @throws Exception if dublicate was found, e is writeprotected, or the
	 * connection failed.
	 */
	public void addDataElement(DataElement e) throws Exception;

	/**
	 * creates a new empty Element. The prefix is automatically added by
	 * prefix()-method.
	 *
	 * @param tagName
	 * @return
	 */
	public Element createElement(String tagName);

	/**
	 * creates a new empty DataElement. The element can be filled with
	 * additional org.w3c.dom.Elements-trees. The prefix is automatically added
	 * by prefix()-method.
	 *
	 * @param tagName
	 * @return dataElement whith name "<prefix>tagName"
	 */
	public DataElement createDataElement(String tagName);

	/**
	 * adds the webarchive namespace-prefix to name. if name has already a
	 * prefix terminated with ':', it will be replaced.
	 *
	 * @param name
	 * @return prefix + name in the format "<prefix>:<name>"
	 */
	public String prefix(String name);
}