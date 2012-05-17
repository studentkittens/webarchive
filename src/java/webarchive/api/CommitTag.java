
package webarchive.api;

import webarchive.util.DateFormatter;

/**
 *
 * @author ccwelich
 */
public class CommitTag {
	public static char SEPARATOR = '@';
	private int id;
	private Date commitTime;
	private String domain;

	public CommitTag(int id, Date crawlTime, String domain) {
		this.id = id;
		this.commitTime = crawlTime;
		this.domain = domain;
	}

	public int getId() {
		return id;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public String getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		StringBuilder bld = new StringBuilder(DateFormatter.XML_FORMAT.length()+1+domain.length());
		bld.append(domain);
		bld.append(SEPARATOR);
		bld.append(commitTime);
		return bld.toString();
	}
	
	
	
}
