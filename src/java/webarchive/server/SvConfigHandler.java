package webarchive.server;

import webarchive.init.OwnConfigHandler;

public class SvConfigHandler extends OwnConfigHandler {

	@Override
	protected void setNodeList() {
		super.nodeL = cfgHandler.getConfig().getElementsByTagName("server");		
	}
}
