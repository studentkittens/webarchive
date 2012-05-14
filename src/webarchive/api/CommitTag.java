
package webarchive.api;

import java.util.Date;
import webarchive.util.DateFormatter;

/**
 *
 * @author ccwelich
 */
class CommitTag {
	public static char SEPARATOR = '@';
	private Date crawlTime;
	private String domain;

	public CommitTag(Date crawlTime, String domain) {
		this.crawlTime = crawlTime;
		this.domain = domain;
	}

	public Date getCrawlTime() {
		return crawlTime;
	}

	public String getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		StringBuilder bld = new StringBuilder(DateFormatter.XML_FORMAT.length()+1+domain.length());
		bld.append(domain);
		bld.append(SEPARATOR);
		bld.append(DateFormatter.format(crawlTime));
		return bld.toString();
	}
	
	
	
}
