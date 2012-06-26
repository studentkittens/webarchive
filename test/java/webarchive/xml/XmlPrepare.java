package webarchive.xml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import webarchive.handler.Handlers;
import webarchive.init.ConfigHandler;
import webarchive.server.LockHandlerMockup;

/**
 * builts TestHandlers
 *
 * @author ccwelich
 */
public class XmlPrepare {

	public static final File XML_BACKUP = new File("test/xml/example.backup.xml"),
		XML_TARGET = new File("test/xml/example.xml"),
		XML_EXPECTED = new File("test/xml/example.expected.xml");

	public static void builtHandlers() throws ParserConfigurationException,
		SAXException, IOException, IllegalArgumentException {
		Handlers.add(new ConfigHandler(new File(
			"test/java/webarchive/xml/mockup.conf.xml")));
		Handlers.add(new XmlConf());
		final LockHandlerMockup lockHandler = new LockHandlerMockup();
		Handlers.add(lockHandler);
		Handlers.add(new XmlMethodFactory(lockHandler));
	}

	static void restoreFiles() {
		try {
			if (Files.size(XML_BACKUP.toPath()) != Files.size(
				XML_TARGET.toPath())) {

				Files.copy(XML_BACKUP.toPath(), XML_TARGET.toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			}
		} catch (IOException ex) {
			Logger.getLogger(XmlPrepare.class.getName()).
				log(Level.SEVERE, null, ex);
		}
	}

	static void killHandlers() {
		Handlers.clear();
	}

}
