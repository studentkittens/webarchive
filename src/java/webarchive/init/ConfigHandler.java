package webarchive.init;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import webarchive.handler.Handler;

/**
 * The Class ConfigHandler creates and represents the Configuration Document from a XML-File passed as a parameter.
 * 
 * @author Schneider
 */
public class ConfigHandler extends Handler {

	private Document config;
	
	private Node rootNode;
	
	private File configPath;
	
	/**
	 * Instantiates a new config handler.
	 *
	 * @param configPath the config path
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ConfigHandler(File configPath) throws ParserConfigurationException, SAXException, IOException {
		this.configPath = configPath;
		rebuild();
	}
	
	/**
	 * Rebuild.
	 *
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void rebuild() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder=dbf.newDocumentBuilder();
		setConfig(builder.parse(configPath));
		rootNode = config.getChildNodes().item(0);
	}

	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	public Document getConfig() {
		return config;
	}

	/**
	 * Sets the config-document
	 *
	 * @param config the new config-document
	 */
	protected void setConfig(Document config) {
		this.config = config;
	}
	
	/**
	 * Gets the value of the XML-Element described by the nodePath.
	 * The nodePath would look like this:
	 * webarchive.server.port
	 * This would return the String-representation of 42 in the following xml-file:
	 * <webarchive><server><port>42</port></server></webarchive>
	 *
	 * @param nodePath the node path
	 * @return the value
	 */
	public String getValue(String nodePath) {
		String[] path = nodePath.split("\\.");
		NodeList children = rootNode.getChildNodes();
		for(int j = 1;j<path.length;j++) {
			for(int i = 0; i< children.getLength();i++) {
				if(children.item(i).getNodeName().equals(path[j])) {
					children = children.item(i).getChildNodes();
					break;
				}
			}
		}
		return children.item(0).getTextContent();
	}
	
}
