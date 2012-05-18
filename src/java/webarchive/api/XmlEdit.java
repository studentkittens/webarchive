
package webarchive.api;

import org.w3c.dom.Element;

/**
 *
 * @author ccwelich
 */
public interface XmlEdit {
	public Element getElement(String tagName);
	public void addElement(String tagName, String content);
}
