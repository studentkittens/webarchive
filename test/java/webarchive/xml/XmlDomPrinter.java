
package webarchive.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ccwelich
 */
public class XmlDomPrinter {
	public static void printNodes(String indent, Node n) {
		if(n==null) return;
		System.out.println(indent+n+" type = "+getType(n)+ ", ns="+ n.getNamespaceURI());
		NamedNodeMap attributes = n.getAttributes();
		if(attributes!=null)
		for(int i=0; i<attributes.getLength(); i++) {
			Node att = attributes.item(i);
			System.out.println(indent + att +" type = "+ getType(att)+ ", ns="+ att.getNamespaceURI());
		}
		NodeList children = n.getChildNodes();
		indent += "  ";
		for(int i=0; i<children.getLength(); i++) {
			printNodes(indent, children.item(i));
		}
	}
	private static String getType(Node n) {
		int t = n.getNodeType();
		switch(t) {
			case Document.ATTRIBUTE_NODE: return "ATTRIBUTE_NODE";
			case Document.CDATA_SECTION_NODE: return "CDATA_SECTION_NODE";
			case Document.COMMENT_NODE: return "COMMENT_NODE";
			case Document.DOCUMENT_FRAGMENT_NODE: return "DOCUMENT_FRAGMENT_NODE";
			case Document.DOCUMENT_NODE: return "DOCUMENT_NODE";
			case Document.DOCUMENT_TYPE_NODE: return "DOCUMENT_TYPE_NODE";
			case Document.ELEMENT_NODE: return "ELEMENT_NODE";
			case Document.ENTITY_NODE: return "ENTITY_NODE";
			case Document.NOTATION_NODE: return "NOTATION_NODE";
			case Document.ENTITY_REFERENCE_NODE: return "ENTITY_REFERENCE_NODE";
			case Document.PROCESSING_INSTRUCTION_NODE: return "PROCESSING_INSTRUCTION_NODE";
			case Document.TEXT_NODE: return "TEXT_NODE";
			default: return "error";
		}
	}
}
