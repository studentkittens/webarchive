package webarchive.init;

import webarchive.server.Server;

public class Launcher {

	public static void main(String args[]) {
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				Server.getInstance().stop();
				System.out.println("\nServer stopped!\n");
				try {
					Server.getInstance().getThread().join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}));
				

		final Server server = Server.getInstance();
		if(args!=null && args.length>0) {
			server.setConfigPath(args[0]);
		}  
		server.start();
		System.out.println("\nServer started!\n");

		
	}
}
