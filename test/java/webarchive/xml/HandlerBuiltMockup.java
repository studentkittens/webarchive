
package webarchive.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import webarchive.handler.Handlers;
import webarchive.init.ConfigHandler;
import webarchive.server.LockHandlerMockup;

/**
 *
 * @author ccwelich
 */
public class HandlerBuiltMockup {
	public static void builtHandlers() throws ParserConfigurationException,
		SAXException, IOException, IllegalArgumentException {
		Handlers.add(new ConfigHandler(new File("test/java/webarchive/xml/mockup.conf.xml")));
		Handlers.add(new XmlConf());
		final LockHandlerMockup lockHandler = new LockHandlerMockup();
		Handlers.add(lockHandler);
		Handlers.add(new XmlMethodFactory(lockHandler));
	}
}
