package webarchive.api.model;
//TODO tests

/**
 * Represents a commitTag. A commitTag is related to a domain-folder in archive
 * and a commitTime. One commitTag can involve at least on File respectively its
 * MetaData, hence multiply MetaData can point to one commitTag-instance.
 *
 * @author ccwelich
 */
public class CommitTag {

	private int id;
	private TimeStamp commitTime;
	private String domain;

	/**
	 *
	 * @param id id referenc in database = commitId
	 * @param commitTime = commitTag.commitTime
	 * @param domain = domain.domainName
	 */
	public CommitTag(int id, TimeStamp commitTime, String domain) {
		assert id > 0;
		assert commitTime != null;
		assert domain != null && !domain.isEmpty();
		this.id = id;
		this.commitTime = commitTime;
		this.domain = domain;
	}

	/**
	 * The database-id in commitTag-table, referring to commitId-column
	 *
	 * @return database-Id (commitId)
	 */
	public int getId() {
		return id;
	}

	/**
	 * The commitTime.
	 *
	 * @return commitTime
	 */
	public TimeStamp getCommitTime() {
		return commitTime;
	}

	/**
	 * The domain-name, reffering to domainName-column in domain-table.
	 *
	 * @return domain name
	 */
	public String getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		return "CommitTag{" + "id=" + id + ", commitTime=" + commitTime + ", domain=" + domain + '}';
	}
}
