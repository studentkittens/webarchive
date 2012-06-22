package webarchive.dbaccess;

import webarchive.init.OwnConfigHandler;

public class DbConfigHandler extends OwnConfigHandler {

	@Override
	protected void setNodeList() {
		super.nodeL = cfgHandler.getConfig().getElementsByTagName("db");		
	}
}
