package webarchive.xml;

import java.io.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author ccwelich
 */
//TODO javadoc
//TODO formatted output
//TODO tests
public class XmlDomWriter {

	private Transformer transformer;
	private StreamResult streamResult;
	public XmlDomWriter(File destination) throws FileNotFoundException, TransformerConfigurationException {
		this(new BufferedOutputStream(new FileOutputStream(destination)));
	}
	public XmlDomWriter(OutputStream out) throws
		TransformerConfigurationException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformer = transformerFactory.newTransformer();
		streamResult = new StreamResult(out);

	}

	public void write(Document document) throws TransformerException {
		DOMSource source = new DOMSource(document);
		transformer.transform(source, streamResult);
	}
}
