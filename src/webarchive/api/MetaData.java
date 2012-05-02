package webarchive.api;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Basic MetaData interface in use with webarchive. It contains metadata of an
 * associated html file. It is also used as a key-object to read or extend the
 * archives data.
 *
 * @author ccwelich
 * @version 1
 */
public interface MetaData {

	/**
	 * get the version commitTag.
	 * The commitTag is useful to get older versions from the archives underlying versioning.
	 * The commitTag has following syntax:
	 * "&lt;domain-url&gt;@&lt;timestamp&gt;" <br /> &lt;timestamp&gt; =
	 * "yyyy-mm-dd hh:mm:ss" <br /> example: "www.heise.de@2012-04-23 21:04:42"
	 *
	 * @return version commit tag
	 */
	public String getCommitTag();

	/**
	 * get the time of the last commit as a {@link java.util.Date}
	 *
	 * @return commit time
	 */
	public Date getLastCommitTime();

	/**
	 * get the relative path of the html-archive-folder in the archive
	 *
	 * @return path
	 */
	public File getPath();

	/**
	 * get the origin url
	 *
	 * @return origin url
	 */
	public URL getUrl();

	/**
	 * get the title of the website
	 *
	 * @return title
	 */
	public String getTitle();
}
