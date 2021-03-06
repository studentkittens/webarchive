package webarchive.xml;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import webarchive.handler.Handler;
import webarchive.init.ConfigHandler;
import webarchive.server.Server;

/**
 * Config class for all Xml-related classes. Extracts config-values from xml
 * node in config-file.
 *
 * @author ccwelich
 */
public class XmlConf extends Handler {

	private AutoValidatingMode autoValidatingMode = AutoValidatingMode.AFTER_UPDATE;
	private File schemaPath=null;

	/**
	 * Default constructor
	 *
	 * @throws IllegalArgumentException if there are illegal values in config
	 * file
	 */
	public XmlConf(ConfigHandler conf) throws IllegalArgumentException {
		Document confDom = conf.getConfig();
		Element xmlRoot = (Element) confDom.getElementsByTagName("xml").item(0);
		buildConf(xmlRoot, conf);
	}

	/**
	 * get XmlHandler AutoValidatingMode. determines the timing of XML-Dom
	 * validation.
	 *
	 * @see AutoValidatingMode
	 * @return the current autovalidating mode
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

	void setSchemaPath(File schemaPath) {
		this.schemaPath = schemaPath;
	}

	private void buildConf(Node xmlRoot, ConfigHandler conf) throws IllegalArgumentException {
		if(xmlRoot==null) return;
		NodeList items = xmlRoot.getChildNodes();
		for (int i = 0; i < items.getLength(); i++) {
			Node n = items.item(i);
			final String tagName = n.getNodeName();
			String val = n.getTextContent();
			if (val == null || val.equals("")) {
				throw new IllegalArgumentException(
					"value is empty: " + tagName + "=" + val);
			}
			switch (tagName) {
				case "#text":
					break;
				case "autoValidatingMode":
					autoValidatingMode = mode(val);
					break;
				case "schemaPath":
					schemaPath = new File(val);
					if(!schemaPath.isAbsolute()) {
						this.schemaPath = new File(conf.getValue("webarchive.general.root"),val);
					}
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
