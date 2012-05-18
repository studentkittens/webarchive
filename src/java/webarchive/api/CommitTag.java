
package webarchive.api;

import webarchive.util.DateFormatter;

/**
 *
 * @author ccwelich
 */
public class CommitTag {
	private int id;
	private TimeStamp commitTime;
	private String domain;

	public CommitTag(int id, TimeStamp commitTime, String domain) {
		this.id = id;
		this.commitTime = commitTime;
		this.domain = domain;
	}

	public int getId() {
		return id;
	}

	public TimeStamp getCommitTime() {
		return commitTime;
	}

	public String getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		return "CommitTag{" + "id=" + id + ", commitTime=" + commitTime + ", domain=" + domain + '}';
	}

	
	
}
