package webarchive.api.model;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
//TODO tests

/**
 * Basic MetaData class in use with webarchive. It contains metadata of an
 * associated file, regarding its origin in the web and position in the archive.
 * The inner state is related to certain version by the commitTag. It is also
 * used as a key-object to read or extend the archives data. Equality is
 * recognized by the url and the commitTag.
 *
 * @author ccwelich
 * @version 2
 */
public class MetaData implements Serializable {

	private String url;
	private String mimeType;
	private String title;
	private File path;
	private TimeStamp createTime;
	private CommitTag commitTag;

	/**
	 * main constructor
	 *
	 * @param url origin url, asserted as not null
	 * @param mimeType the mimeType of the file, asserted as not null
	 * @param title generic title, depending on the mimeType of the file and
	 * whether the origin file has a title. Asserted as either null or not
	 * empty.
	 * @param path the relative path inside the archive, asserted not null.
	 * @param createTime the create time in archive, asserted not null.
	 * @param commitTag the relating commitTag, asserted not null.
	 */
	public MetaData(String url, String mimeType, String title, File path,
		TimeStamp createTime, CommitTag commitTag) {
		assert url != null;
		assert mimeType != null;
		assert path != null;
		assert createTime != null;
		assert commitTag != null;
		assert title == null || !title.isEmpty();
		this.url = url;
		this.mimeType = mimeType;
		this.title = title;
		this.path = path;
		this.createTime = createTime;
		this.commitTag = commitTag;
	}

	/**
	 * Get the version commitTag. Each file is has a certain version, referenced
	 * by the commitTag.
	 *
	 * @return the version commit tag
	 */
	public CommitTag getCommitTag() {
		return commitTag;
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
		final MetaData other = (MetaData) obj;
		return this.url.equals(other.url) && this.commitTag.equals(
			other.commitTag);
	}

	@Override
	public int hashCode() {
		return 41 * Objects.hashCode(this.url) + Objects.hashCode(this.commitTag);
	}

	/**
	 * get the time when the file has been created as a java.util.TimeStamp
	 *
	 * @return commit time
	 */
	public TimeStamp getCreateTime() {
		return createTime;
	}

	@Override
	public String toString() {
		return "MetaData{" + "url=" + url + ", mimeType=" 
			+ mimeType + ", title=" + title + ", path=" 
			+ path + ", createTime=" + createTime + ", commitTag=" + commitTag + '}';
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
	 * get the origin url in the web.
	 *
	 * @return origin url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * the MIME-type of the file.
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
