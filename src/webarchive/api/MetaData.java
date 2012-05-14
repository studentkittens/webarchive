package webarchive.api;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Basic MetaData class in use with webarchive. It contains metadata of an
 * associated file, regarding its origin an position in the archive. It is also
 * used as a key-object to read or extend the archives data.
 *
 * @author ccwelich
 * @version 2
 */
public class MetaData {

	private URL url;
	private String mimeType;
	private String title;
	private File path;
	private Date createTime;
	private CommitTag commitTag;

	/**
	 * get the version commitTag. The commitTag is useful to get older versions
	 * from the archives underlying versioning. The commitTag has following
	 * syntax: "&lt;domain-url&gt;@&lt;timestamp&gt;" <br /> &lt;timestamp&gt; =
	 * "yyyy-mm-dd hh:mm:ss" <br /> example: "www.heise.de@2012-04-23 21:04:42"
	 *
	 * @return version commit tag
	 */
	public CommitTag getCommitTag() {
		return commitTag;
	}

	public MetaData(URL url, String mimeType, String title, File path, Date createTime, CommitTag commitTag) {
		this.url = url;
		this.mimeType = mimeType;
		this.title = title;
		this.path = path;
		this.createTime = createTime;
		this.commitTag = commitTag;
	}

	/**
	 * get the time of the last commit as a {@link java.util.Date}
	 *
	 * @return commit time
	 */
	public Date getCrawlTime() {
		return commitTag.getCrawlTime();
	}

	/**
	 * get the time when the file has been created as a {@link java.util.Date}
	 *
	 * @return commit time
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * get the relative path of the archive-folder in the archive
	 *
	 * @return path
	 */
	public File getPath() {
		return path;
	}

	/**
	 * get the origin url
	 *
	 * @return origin url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * get the domain of the file
	 *
	 * @return domain-name
	 */
	public String getDomain() {
		return commitTag.getDomain();
	}

	/**
	 * the MIME-type of the file
	 *
	 * @return the MIME-type
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * get the title of the website
	 *
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
}
