
package webarchive.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * adapter mockup to bybass Client-Singleton
 * @author ccwelich
 */
public class ClientAdapterMockup implements ClientAdapter {
	private XmlHandler backend;

	public ClientAdapterMockup(XmlHandler backend) {
		this.backend = backend;
	}
	
	@Override
	public Document syncDocument(webarchive.api.xml.DataElement element) throws SAXException {
		backend.addDataElement((DataElement)element);
		return backend.getDocument();
	}


}
