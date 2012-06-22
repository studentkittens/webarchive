package webarchive.handler;

import java.util.HashMap;
import java.util.Map;

public class Handlers {
	
	private static Map<Class<? extends Handler>,Handler> handlers = new HashMap<>();
	
	public static synchronized void add(Handler h) {
		assert h != null;
		Handler pre = handlers.put(h.getClass(), h);
		assert pre==null;
	}
	
	public static synchronized Handler get(Class<? extends Handler> name) {
		return handlers.get(name);
	}

	public static void clear() {
		handlers = new HashMap<>();
	}
		
	
}
