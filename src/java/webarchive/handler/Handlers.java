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
	
	public static synchronized  <T extends Handler> T get(Class<T> name) {
		return (T) handlers.get(name);
	}

	public static void clear() {
		handlers = new HashMap<>();
	}
		
	
}
