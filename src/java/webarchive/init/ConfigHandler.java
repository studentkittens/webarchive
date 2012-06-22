package webarchive.init;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import webarchive.handler.Handler;

public class ConfigHandler extends Handler {

	private Document config;
	private Node rootNode;
	private static final String configPath="/path/to/webarchive.conf.xml";
	public ConfigHandler() {
		rebuild();
	}
	
	protected void rebuild() {
		DocumentBuilder builder=null;
		try {
			 builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			setConfig(builder.parse(configPath));
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rootNode = config.getChildNodes().item(0);
	}

	public Document getConfig() {
		return config;
	}

	protected void setConfig(Document config) {
		this.config = config;
	}
	
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
