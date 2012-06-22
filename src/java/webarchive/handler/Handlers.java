package webarchive.handler;

import java.util.HashMap;
import java.util.Map;

public class Handlers {
	
	private static Map<Class<? extends Handler>,Handler> handlers;
	
	public Handlers() {
		this.handlers = new HashMap<>();
	}
	
	public static synchronized void add(Handler h) {
		handlers.put(h.getClass(), h);
	}
	
	public static synchronized Handler get(Class<? extends Handler> name) {
		return handlers.get(name);
	}
		
	
}
