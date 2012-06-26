package webarchive.dbaccess;

import webarchive.init.OwnConfigHandler;
/**
 * retrieves settings from config-file.
 * @author eddy
 */
public class DbConfigHandler extends OwnConfigHandler {

	@Override
	protected void setNodeList() {
		super.nodeL = cfgHandler.getConfig().getElementsByTagName("db");		
	}
}
