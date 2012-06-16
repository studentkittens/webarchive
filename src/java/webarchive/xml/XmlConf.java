package webarchive.xml;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import webarchive.xml.XmlHandler.AutoValidatingMode;

/**
 * Config class for all Xml-related classes.
 *
 * @author ccwelich
 */
//TODO tests
public class XmlConf {

	private String namespace = "http://www.hof-university.de/webarchive";
	private String prefix = "wa:";
	private String dataTag = "data";
	private File schemaPath = new File("xml/file.xsd");
	private XmlHandler.AutoValidatingMode autoValidatingMode = XmlHandler.AutoValidatingMode.AFTER_UPDATE;
	private final DocumentBuilderFactory documentBuilderFactory;
	private ErrorHandler xmlErrorHandler;
	private XmlValidator xmlValidator;

	/**
	 * @return the default DocumentBuilderFactory
	 */
	public DocumentBuilderFactory getDocumentBuilderFactory() {
		return documentBuilderFactory;
	}

	/**
	 * create XmlConf with default values
	 */
	public XmlConf() throws SAXException {
		// init DocumentBuilderFactory
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setIgnoringComments(true);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		documentBuilderFactory.setExpandEntityReferences(true);
		documentBuilderFactory.setNamespaceAware(true);
		// init workers
		xmlErrorHandler = new XmlErrorHandler();
		xmlValidator = new XmlValidator(this, xmlErrorHandler);
	}

	public ErrorHandler getXmlErrorHandler() {
		return xmlErrorHandler;
	}

	public XmlValidator getXmlValidator() {
		return xmlValidator;
	}

	/**
	 * sets XmlHandler AutoValidatingMode. default: AFTER_UPDATE
	 *
	 * @see XmlConf.AutoValidatingMode
	 * @param autoValidatingMode
	 */
	public void setAutoValidatingMode(AutoValidatingMode autoValidatingMode) {
		assert autoValidatingMode != null;
		this.autoValidatingMode = autoValidatingMode;
	}

	/**
	 * get XmlHandler AutoValidatingMode.
	 *
	 * @see AutoValidatingMode
	 * @return the current autovalidating mode
	 * @param autoValidatingMode
	 */
	public AutoValidatingMode getAutoValidatingMode() {
		return autoValidatingMode;
	}

	/**
	 * get schema path. the schema is used for XML-Metafile validation.
	 *
	 * @return path of schema-file
	 */
	public File getSchemaPath() {
		return schemaPath;
	}

	/**
	 * set schema path. default: xml/file.xsd the schema is used for
	 * XML-Metafile validation.
	 *
	 * @param schemaPath
	 */
	public void setSchemaPath(File schemaPath) {
		assert schemaPath != null;
		this.schemaPath = schemaPath;
	}

	/**
	 * set the name of the data element according to XML-Metafile. default: data
	 *
	 * @param dataTag
	 */
	public void setDataTag(String dataTag) {
		assert dataTag != null;
		this.dataTag = dataTag;
	}

	/**
	 * get the name of the data element according to XML-Metafile.
	 *
	 * @return data-element name
	 */
	String getDataTag() {
		return addPrefixTo(dataTag);
	}

	/**
	 * get the namespace of all XML-elements according to XML-Metafile.
	 *
	 * @return namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * set the namespace of all XML-elements according to XML-Metafile. default:
	 * http://www.hof-university.de/webarchive
	 *
	 * @return namespace
	 */
	public void setNamespace(String namespace) {
		assert namespace != null;
		this.namespace = namespace;
	}

	/**
	 * set the addPrefixTo of all XML-elements according to XML-Metafile. default:
	 * "wa:"
	 *
	 * @param addPrefixTo
	 */
	public void setPrefix(String prefix) {
		assert prefix != null;
		assert !prefix.equals("");
		this.prefix = (prefix.endsWith(":")) ? prefix : prefix + ':';
	}

	/**
	 * get the addPrefixTo of all XML-elements according to XML-Metafile.
	 *
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * adds a prefix to a given string by the default prefix. If name has already a
	 * prefix, which is terminated by ':', then this prefix will be replaced by
	 * the default prefix.
	 *
	 * @param name name to addPrefixTo
	 * @return addPrefixTo+name
	 */
	public String addPrefixTo(String name) {
		assert prefix.endsWith(":");
		int i = name.indexOf(':');
		return prefix + ((i != -1) ? name : name.substring(i + 1));
	}
}
