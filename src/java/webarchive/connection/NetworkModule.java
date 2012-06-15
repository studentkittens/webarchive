package webarchive.connection;

import webarchive.handler.HandlerCollection;

public interface NetworkModule {

	public void removeConnection(Connection c);
	public HandlerCollection getHandlers();
	
}
