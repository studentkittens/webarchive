package webarchive.xml;

import java.io.File;
import webarchive.handler.Handler;

/**
 * Config class for all Xml-related classes.
 *
 * @author ccwelich
 */
//TODO tests
public class XmlConf extends Handler {
	private AutoValidatingMode autoValidatingMode = AutoValidatingMode.AFTER_UPDATE;
	private String namespace = "http://www.hof-university.de/webarchive";
	private String prefix = "wa:";
	private String dataTag = "data";
	private File schemaPath = new File("xml/file.xsd");

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
		return dataTag;
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

	

}
