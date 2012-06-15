package webarchive.xml;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 * Writes formatted org.w3c.dom.documents into an outputstream.
 * 
 * @author ccwelich
 */
//TODO javadoc
//TODO formatted output
//TODO tests
public class XmlDomWriter {

	private Transformer transformer;
	private StreamResult streamResult;
	private final OutputFormat format;
	private final XMLSerializer serializer;
	public XmlDomWriter(File destination) throws FileNotFoundException, TransformerConfigurationException {
		this(new BufferedOutputStream(new FileOutputStream(destination)));
	}
	public XmlDomWriter(OutputStream out) throws
		TransformerConfigurationException {
//		TransformerFactory transformerFactory = TransformerFactory.newInstance();
//		transformer = transformerFactory.newTransformer();
//		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		format = new OutputFormat("xml","utf-8",true);
		format.setIndent(4);
		format.setLineWidth(20);
		//format.setPreserveSpace(true);
		serializer = new XMLSerializer(out, format);
		streamResult = new StreamResult(out);

	}

	public void write(Document document) throws IOException  {
//		DOMSource source = new DOMSource(document);
//		transformer.transform(source, streamResult);
		serializer.serialize(document);
	}
}
