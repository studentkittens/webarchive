package webarchive.xml;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import webarchive.handler.Handler;
import webarchive.handler.Handlers;
import webarchive.init.ConfigHandler;

/**
 * Config class for all Xml-related classes.
 *
 * @author ccwelich
 */
//TODO tests
public class XmlConf extends Handler {

	private AutoValidatingMode autoValidatingMode = AutoValidatingMode.AFTER_UPDATE;
	private String namespace = "http://www.hof-university.de/webarchive";
	private String prefix = "wa";
	private String dataTag = "data";
	private File schemaPath = new File("xml/file.xsd");

	public XmlConf() throws IllegalArgumentException {
		Document dom = ((ConfigHandler) Handlers.get(ConfigHandler.class)).
			getConfig();
		Element xmlRoot = (Element) dom.getElementsByTagName("xml").item(0);
		buildConf(xmlRoot);
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
	 * get the addPrefixTo of all XML-elements according to XML-Metafile.
	 *
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	private void buildConf(Node xmlRoot) throws IllegalArgumentException {
		NodeList items = xmlRoot.getChildNodes();
		for (int i = 0; i < items.getLength(); i++) {
			Node n = items.item(i);
			final String tagName = n.getNodeName();
			String val = n.getTextContent();
			if (val == null || val.equals("")) {
				throw new IllegalArgumentException(
					"value is empty: " + tagName + "=" + val);
			}
			System.out.println(
				"XmlConf::buildConf tagName=" + tagName + ", val=" + val);
			switch (tagName) {
				case "#text":
					break;
				case "autoValidatingMode":
					autoValidatingMode = mode(val);
					break;
				case "namespace":
					namespace = val;
					break;
				case "prefix":
					prefix = val;
					break;
				case "dataTag":
					dataTag = val;
					break;
				case "schemaPath":
					schemaPath = new File(val);
					break;
				default:
					throw new IllegalArgumentException(
						"unsupported attribute: " + tagName + "=" + val);
			}
		}
	}

	private AutoValidatingMode mode(String val) {
		val = val.toUpperCase();
		val = val.replace(' ', '_');
		return AutoValidatingMode.valueOf(val);
	}
}
