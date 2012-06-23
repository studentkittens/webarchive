package webarchive.init;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import webarchive.dbaccess.DbConfigHandler;
import webarchive.dbaccess.SqlHandler;
import webarchive.dbaccess.SqliteAccess;
import webarchive.handler.Handlers;
import webarchive.server.FileHandler;
import webarchive.server.LockHandlerImpl;
import webarchive.server.Server;
import webarchive.server.SvConfigHandler;
import webarchive.xml.XmlConf;
import webarchive.xml.XmlMethodFactory;

public class Launcher {

	public static void main(String args[]) {

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				Server.getInstance().stop();
				System.out.println("\nServer stopped!\n");
				try {
					Server.getInstance().getThread().join();
				} catch (InterruptedException ex) {
					Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null,
				ex);
				}
			}
		}));


		final Server server = Server.getInstance();
		
		File configPath = new File(
			(args != null && args.length > 0)
			? args[0]
			: "conf/webarchive.conf.xml"
		);
		if(!configPath.exists()) {
			System.out.println("config file path does not exist: "+configPath.getAbsolutePath());
			System.exit(-1);
		}
		try {
			builtHandlers(configPath);
		} catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException ex) {
			Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null,
				ex);
			System.out.println("error: cannot build Handlers");
			System.exit(-1);
		}
		System.out.println("built handlers");
		server.start();
		System.out.println("\nServer started!\n");

		// TODO start notifier


	}

	private static void builtHandlers(File configPath) throws ParserConfigurationException,
		SAXException, IOException, IllegalArgumentException {
		Handlers.add(new ConfigHandler(configPath));
		Handlers.add(new SvConfigHandler());
		Handlers.add(new DbConfigHandler());
		Handlers.add(new SqlHandler(new SqliteAccess(new File(((DbConfigHandler) Handlers.
			get(DbConfigHandler.class)).getValue("path")))));
		Handlers.add(new XmlConf());
		Handlers.add(new FileHandler());
		final LockHandlerImpl lockHandler = new LockHandlerImpl(InetAddress.getLocalHost(), 42421);
		Handlers.add(lockHandler);
		final XmlMethodFactory xmlMethodFactory = new XmlMethodFactory(lockHandler);
		xmlMethodFactory.setXmlErrorHandler(null); // XmlErrorHandler not used!
		Handlers.add(xmlMethodFactory);

	}
}
