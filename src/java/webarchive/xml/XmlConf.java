package webarchive.xml;

import java.io.File;
import java.net.URL;
import webarchive.xml.XmlHandler.AutoValidatingMode;

/**
 *
 * @author ccwelich
 */
//TODO tests
//TODO javadoc
public class XmlConf {

	private String namespace = "http://www.hof-university.de/webarchive";
	private String prefix = "wa:";
	private String dataTag = "data";
	private File schemaPath = new File("xml/file.xsd");
	private XmlHandler.AutoValidatingMode autoValidatingMode = XmlHandler.AutoValidatingMode.AFTER_UPDATE;

	public XmlConf() {
	}

	public void setAutoValidatingMode(AutoValidatingMode autoValidatingMode) {
		assert autoValidatingMode != null;
		this.autoValidatingMode = autoValidatingMode;
	}

	public AutoValidatingMode getAutoValidatingMode() {
		return autoValidatingMode;
	}

	public File getSchemaPath() {
		return schemaPath;
	}

	public void setSchemaPath(File schemaPath) {
		assert schemaPath != null;
		this.schemaPath = schemaPath;
	}

	public void setDataTag(String dataTag) {
		assert dataTag != null;
		this.dataTag = dataTag;
	}

	String getDataTag() {
		return prefix(dataTag);
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		assert namespace != null;
		this.namespace = namespace;
	}

	public void setPrefix(String prefix) {
		assert prefix != null;
		assert !prefix.equals("");
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public String prefix(String name) {
		int i = name.indexOf(':');
		return prefix + ((i != -1) ? name : name.substring(i + 1));
	}
}
