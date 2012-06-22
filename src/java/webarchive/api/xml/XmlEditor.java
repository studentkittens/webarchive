package webarchive.api.xml;

import org.w3c.dom.Element;
import webarchive.xml.DataElement;

/**
 * XmlEditor is used to read and add DataElements from or to the webarchive xml-Files.
 * DataElements are the uppermost nodes in the data-node of the webarchive xml-file.
 * Existing DataElements may be read by using {@link #addDataElement(webarchive.xml.DataElement) }.
 * New DataElements may be added by using {@link #createDataElement(java.lang.String) } in order to initialize new
 * DataElements. Via {@link #createElement(java.lang.String) } new w3c.dom.Elements may be added to the
 * thus created DataElement (according to xsd-schema definition).
 * Note that existing DataElements are write-protected.
 * @see DataElement
 * @author ccwelich
 */
public interface XmlEditor {

	/**
	 * get a data element from the data subtree by its tagname. The method
	 * returns a write-protected DataElement.
	 *
	 * @param tagName key to find element. 
	 * Note: all tagnames are internally prefixed. See {@link #addPrefixTo(java.lang.String) } if own prefixes are used.
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
	 * @param tagName tagName of element.
	 * Note: all tagnames are internally prefixed. See {@link #addPrefixTo(java.lang.String) } if own prefixes are used.
	 * @return
	 */
	public Element createElement(String tagName);

	/**
	 * creates a new empty DataElement. The element can be filled with
	 * additional org.w3c.dom.Elements-trees. The prefix is automatically added
	 * by prefix()-method.
	 *
	 * @param tagName key to add element.
	 * Note: all tagnames are prefixed, use {@link #addPrefixTo(java.lang.String) }
	 * to add the correct prefix.
	 * @return dataElement whith name "<prefix>tagName"
	 */
	public DataElement createDataElement(String tagName);

	/**
	 * adds a prefix to a given string by the default prefix. If name has already a
	 * prefix, which is terminated by ':', then this prefix will be replaced by
	 * the default prefix. <br/>
	 * Note: use only if own prefixes are needed and add a template prefix at the beginning of the userdefined prefix.
	 * The template prefix will then be overwritten.
	 *
	 * @param name name to addPrefixTo
	 * @return addPrefixTo+name
	 */
	public String addPrefixTo(String name);
	
	
	
	
}