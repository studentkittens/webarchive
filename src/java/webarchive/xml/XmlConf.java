package webarchive.xml;

import java.io.File;
import java.net.URL;

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
	//private String schemaDef = "http://www.w3.org/2001/XMLSchema";

//	public String getSchemaDef() {
//		return schemaDef;
//	}

	public File getSchemaPath() {
		return schemaPath;
	}
//TODO constructor: overwrite all values != null
	public XmlConf() {
	}

	public String getNamespace() {
		return namespace;
	}

	public String getPrefix() {
		return prefix;
	}

	public String prefix(String name) {
		int i = name.indexOf(':');
		return prefix + ((i != -1) ? name : name.substring(i + 1));
	}

	String getDataTag() {
		return prefix(dataTag);
	}
}
