package webarchive.xml;

import java.io.File;
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

	/**
	 * create XmlConf with default values
	 */
	public XmlConf() {
	}

	/**
	 * sets XmlHandler AutoValidatingMode. default: AFTER_UPDATE
	 *
	 * @see AutoValidatingMode
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
		return prefix(dataTag);
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
	 * set the prefix of all XML-elements according to XML-Metafile. default:
	 * "wa:"
	 *
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		assert prefix != null;
		assert !prefix.equals("");
		this.prefix = (prefix.endsWith(":")) ? prefix : prefix + ':';
	}

	/**
	 * get the prefix of all XML-elements according to XML-Metafile.
	 *
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * prefixes a given string by the default prefix. If name has already a
	 * prefix, which is terminated by ':', then this prefix will be replaced by
	 * the default prefix.
	 *
	 * @param name name to prefix
	 * @return prefix+name
	 */
	public String prefix(String name) {
		assert prefix.endsWith(":");
		int i = name.indexOf(':');
		return prefix + ((i != -1) ? name : name.substring(i + 1));
	}
}
