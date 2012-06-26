package webarchive.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * Central container for all handlers, factories, which are globally used as
 * single instances.
 *
 * @author ccwelich
 */
public class Handlers {

	private static Map<Class<? extends Handler>, Handler> handlers = new HashMap<>();

	/**
	 * adds a Handler to the handlers map. The handler is accessable by its
	 * class-type. The handlers key is asserted as unique.
	 *
	 * @param handler handler to add
	 */
	public static void add(Handler handler) {
		assert handler != null;
		add(handler.getClass(), handler);
	}

	/**
	 * adds a Handler to the handlers map. The handler is accessable by the
	 * given class-type key. The handlers key is asserted as unique. 	 
	 * @param key
	 * @param handler
	 */
	public static void add(Class<? extends Handler> key, Handler handler) {
		assert handler != null;
		assert key != null;
		Handler pre = handlers.put(key, handler);
		assert pre == null;
	}

	/**
	 * get a Handler
	 * @param <T> used for casting
	 * @param key key to find handler
	 * @return the associated handler to key, or null if there is no handler for key.
	 */
	public static synchronized <T extends Handler> T get(Class<T> key) {
		return (T) handlers.get(key);
	}

	/**
	 * removes all handlers and resets the map.
	 */
	public static void clear() {
		handlers = new HashMap<>();
	}
}
