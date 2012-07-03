/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.api.xml;

import java.io.Serializable;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DataElement in xml. DataElements are the uppermost
 * nodes in the data-node of the webarchive xml-file. New org.w3c.dom.Elements
 * may be added as children, but DataElement can only be stored back if it is not
 * write-protected, which is if canWrite() returns true.
 * Equality is checked by the content and by writeprotection.
 * @see XmlEditor
 * @author ccwelich
 */
public interface DataElement extends Serializable {

	/**
	 * @see org.w3c.dom.Node#appendChild(org.w3c.dom.Node)
	 */
	public Node appendChild(Node node) throws DOMException;

	/**
	 * check if DataElement is write protected
	 *
	 * @return true if DataElement is not write-protected, else false
	 */
	public boolean canWrite();

	/**
	 * @see org.w3c.dom.Node#getChildNodes()
	 */
	public NodeList getChildNodes();

	/**
	 * @see org.w3c.dom.Element#getElementsByTagName(java.lang.String)
	 */
	public NodeList getElementsByTagName(TagName tagName);

	/**
	 * @see org.w3c.dom.Node#isEqualNode(org.w3c.dom.Node) 
	 */
	public boolean isEqualNode(DataElement other);
	
}
