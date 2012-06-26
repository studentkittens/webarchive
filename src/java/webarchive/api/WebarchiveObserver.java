
package webarchive.api;

import java.util.List;
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
public interface WebarchiveObserver extends Observer {
	/**
	 * update called in case of changes in webarchive
	 * @param client the broadcasting client
	 * @param changes a list of commitTags
	 * Use commitTags combined with client.select() to get changed html-files.
	 * @see CommitTag
	 */
	public void	update(WebarchiveClient client, List<CommitTag> changes);
}
