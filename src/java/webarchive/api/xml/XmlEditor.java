package webarchive.api.xml;

import org.w3c.dom.Element;

/**
 * XmlEditor is used to read and add DataElements from or to the webarchive xml-Files.
 * DataElements are the uppermost nodes in the data-node of the webarchive xml-file.
 * Existing DataElements may be read by using getDataElement().
 * New DataElements may be added by using createDataElement() in order to initialize new
 * DataElements. Via createElement() new w3c.dom.Elements may be added to the
 * thus created DataElement (according to xsd-schema definition).
 * Note that existing DataElements are write-protected. <br/>
 * All changes have to match with the file.xsd schema.
 * @see DataElement
 * @see TagName
 * @author ccwelich
 */
public interface XmlEditor {

	/**
	 * get a data element from the data subtree by its tagname. The method
	 * returns a write-protected DataElement.
	 *
	 * @param tagName key to find element. 
	 * @return the element, or null if there was no element with tagName
	 */
	public DataElement getDataElement(TagName tagName);

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
	 * creates a new empty Element. 
	 *
	 * @param tagName tagName of the new element.
	 * @return new Element with tagName
	 */
	public Element createElement(TagName tagName);

	/**
	 * creates a new empty DataElement. The element can be filled with
	 * additional org.w3c.dom.Elements-trees. The prefix is automatically added
	 * by prefix()-method.
	 *
	 * @param tagName tagName of the new element, also key to add element.
	 * @return dataElement whith name "<prefix>tagName"
	 */
	public DataElement createDataElement(TagName tagName);

	
	
	
	
	
}