
package webarchive.api;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import webarchive.api.model.CommitTag;
/**
 * Observes changes in webarchive.
 * Implementators of this interface can be added as an observer to a WebarchiveClient.
 * These observers will be notified of changes in the archive in certain time intervals.
 * Changes are provided as a List of CommitTags.
 * @author ccwelich
 * @version 1
 */
public abstract class WebarchiveObserver implements Observer {
	/**
	 * update called in case of changes in webarchive
	 * @param client the broadcasting client
	 * @param changes a list of commitTags
	 * Use commitTags combined with client.select() to get changed html-files.
	 * @see CommitTag
	 */
	
	public abstract void update(WebarchiveClient client, List<CommitTag> changes);

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		this.update((WebarchiveClient)o,(List<CommitTag>)arg);
		
	}
}
