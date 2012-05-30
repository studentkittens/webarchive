/* 
 * Simple Notifier, which is written like the documentation told.
 * Test to do.
 */

package webarchive.notifier;

import java.util.Date;

public class observerble extends Thread {

	private long mIntervall;
	private Date lastSearch;
	
	
	public observerble(long intervall){
		
		mIntervall = intervall;	
		lastSearch=new Date(0);
	}
	
public void run() {
		while(true){
			
			//#############//
			// start routine
			new notifier(lastSearch);
			
			//#############//
			// set Date
				long a;
				a=System.currentTimeMillis();
				lastSearch=new Date (a);
				System.out.println(lastSearch);
				
			//#############//
			// sleep (mIntervall)
				try {
				      sleep(mIntervall);
				    }
				    catch(InterruptedException e) {
				    }
				
				
		}
	}
		
}