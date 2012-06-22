package webarchive.init;

import org.w3c.dom.NodeList;

import webarchive.handler.Handler;
import webarchive.server.Server;

public abstract class OwnConfigHandler extends Handler {

	protected ConfigHandler cfgHandler = (ConfigHandler) Server.getInstance().getHandlers().get("ConfigHandler");
	protected NodeList nodeL;
	
	protected abstract void setNodeList();
	public String getValue(String node) {
		setNodeList();
		NodeList tmp = nodeL.item(0).getChildNodes();
		for (int i = 0; i< tmp.getLength();i++) {
			if(tmp.item(i).getNodeName().equals(node)) {
				return tmp.item(i).getTextContent();
			}
		}
		return null;
	}
}
