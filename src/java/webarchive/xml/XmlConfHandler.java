package webarchive.xml;

import java.io.File;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import webarchive.init.ConfigHandler;
import webarchive.server.Server;

/**
 *
 * @author ccwelich
 */
public class XmlConfHandler {

	public XmlConfHandler() {
		ConfigHandler confHandler = Server.getInstance().getHandlers().get("ConfigHandler");
	}

	public XmlConf buildConf() throws IllegalArgumentException {
		Document dom = getConfig();
		Element xmlRoot = (Element) dom.getElementsByTagName("xml").item(0);
		XmlConf conf = new XmlConf();
		NodeList items = xmlRoot.getChildNodes();
		for (int i = 0; i < items.getLength(); i++) {
			Node n = items.item(i);
			String val = n.getTextContent();
			final String tagName = n.getNodeName();
			switch (tagName) {
				case "autoValidatingMode":
					conf.setAutoValidatingMode(mode(val));
					break;
				case "namespace":
					conf.setNamespace(val);
					break;
				case "prefix":
					conf.setPrefix(val);
					break;
				case "dataTag":
					conf.setDataTag(val);
					break;
				case "schemaPath":
					conf.setSchemaPath(new File(val));
					break;
				default:
					throw new IllegalArgumentException(
						"unsupported attribute: " + tagName + "=" + val);
			}
		}
		return conf;
	}

	private AutoValidatingMode mode(String val) {
		val = val.toUpperCase();
		val = val.replace(' ', '_');
		return AutoValidatingMode.valueOf(val);
	}

}
