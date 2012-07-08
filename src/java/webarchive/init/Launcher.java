package webarchive.init;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import webarchive.dbaccess.SqlHandler;
import webarchive.dbaccess.SqliteAccess;
import webarchive.handler.Handlers;
import webarchive.notifier.Notifier;
import webarchive.server.*;
import webarchive.transfer.FileDescriptor;
import webarchive.xml.XmlConf;

/**
 * The Class Launcher is responsible for starting the whole project and shutting it down softly.
 * 'Softly' means, right before the JVM terminates it will try to close all open Connections and stop running threads that should not just be killed.
 * 
 * This way the Python-Part of the webarchive can just start the server like any other Java-Application and stop it by simply killing the started process.
 *
 * @author ccwelich,eschneider
 */
public class Launcher {

	public static void main(String args[]) {
		Handlers col = new Handlers();
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
				buildHandlers(configPath,col);
			} catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException ex) {
				Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null,
					ex);
				System.out.println("error: cannot build Handlers");
				System.exit(-1);
			}		
		System.out.println("built handlers");
		try {
			buildLogger(col.get(ConfigHandler.class));
		} catch (IOException e) {
			System.err.println("error: could not initialize the logger subsystem!");
			System.err.println(e);
		}
		
		Server.init(col); //call once

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

		
		Server.getInstance().start();
		System.out.println("\nServer started!\n");

		// TODO start notifier
		String absPathDb = FileDescriptor.root+"/"+((ConfigHandler) col.get(ConfigHandler.class)).getValue("webarchive.db.path");
		SqlHandler sql  = (new SqlHandler(new SqliteAccess(new File(absPathDb))));
		Integer interval = new Integer(col.get(ConfigHandler.class).getValue("webarchive.server.notify.interval"));
		 Notifier notifier = new Notifier(interval*2000,sql);// TODO intervall umrechnung
		 notifier.start();
		 System.out.println("\nNotifier started!\n");
		


	}

	/**
	 * Built handlers.
	 *
	 * @param configPath the config path
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the sAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	private static void buildHandlers(File configPath, Handlers col) throws ParserConfigurationException,
		SAXException, IOException, IllegalArgumentException {
		final ConfigHandler configHandler = new ConfigHandler(configPath);
		col.add(configHandler);
		col.add(new XmlConf(configHandler));
		col.add(new webarchive.server.FileHandler());
		FileDescriptor.root = configHandler.getValue("webarchive.general.root");

	}
	private static void buildLogger(ConfigHandler cfgH) throws IOException {
		Logger logger = Logger.getLogger("webarchive");
		Handler handler = null;
		String archiveRoot = cfgH.getValue("webarchive.general.root");
		createEnvironment(archiveRoot);
		try {
			
			handler = new FileHandler(archiveRoot+"/logs/java/webarchive.log");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Formatter formatter = new SimpleFormatter();
		handler.setFormatter(formatter);
		logger.addHandler(handler);
	}
	private static void createEnvironment(String archiveRoot) {
		File dir = new File(archiveRoot+"/logs/java");
		if(!dir.exists()) {
			dir.mkdir();
		}
	}
}
