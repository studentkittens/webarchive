
package webarchive.api;

import java.util.List;

/**
 * Observes changes in webarchive
 * @author ccwelich
 * @version 1
 */
public interface WebarchiveObserver {
	/**
	 * update called in case of changes in webarchive
	 * @param client the broadcasting client
	 * @param commitTagChanges a list of commitTags (see {@link MetaData}).
	 * Use commitTags combined with client.select() to get changed html-files.
	 */
	public void	update(WebarchiveClient client, List<String> commitTagChanges);
}
