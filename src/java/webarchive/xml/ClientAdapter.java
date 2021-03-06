package webarchive.xml;

import java.io.Serializable;
import org.w3c.dom.Document;

/**
 * Usage: bypass Client-Singleton in XmlEditor-unittests.
 * @author ccwelich
 */
interface ClientAdapter extends Serializable {

	Document syncDocument(webarchive.api.xml.DataElement element) throws Exception;
	
}
