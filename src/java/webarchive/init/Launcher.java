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
		
			System.out.println("\nServer started!\n");
			Server.getInstance().start();
		
	}
}
