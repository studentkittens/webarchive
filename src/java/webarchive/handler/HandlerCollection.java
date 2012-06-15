package webarchive.handler;

import java.util.HashMap;
import java.util.Map;

public class HandlerCollection {
	
	private Map<String,Handler> handlers;
	
	public HandlerCollection() {
		this.handlers = new HashMap<String,Handler>();
	}
	
	public void add(Handler h) {
		handlers.put(h.getName(), h);
	}
	
	public Handler get(String name) {
		return handlers.get(name);
	}
		
	
}
