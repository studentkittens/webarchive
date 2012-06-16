package webarchive.api.model;
//TODO tests

import java.io.Serializable;


/**
 * Represents a commitTag. A commitTag is related to a domain-folder in archive
 * and a commitTime. One commitTag can involve at least on File respectively its
 * MetaData, hence multiply MetaData can point to one commitTag-instance.
 * Equality is recognized by the id.
 *
 * @author ccwelich
 */
public class CommitTag implements Serializable {

	private int id;
	private TimeStamp commitTime;
	private String domain;

	/**
	 *
	 * @param id id referenc in database = commitId
	 * @param commitTime = commitTag.commitTime
	 * @param domain = domain.domainName
	 */
	public CommitTag(int id, TimeStamp commitTime, String domain) throws
		IllegalArgumentException {
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CommitTag other = (CommitTag) obj;
		return this.id == other.id;
	}

	@Override
	public int hashCode() {
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
