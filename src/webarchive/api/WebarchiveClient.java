package webarchive.api;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Interface to webarchive. provides functions to select metadata from the
 * database, getting and adding files to html-archive-folder or reading or
 * extending XML-Metadata. Each html-file has its own html-archive-folder, which
 * contains at minimum the html itself, XML-Metadata. The folder is extendable
 * by arbitrary files.
 *
 * @author ccwelich
 * @version 1
 */
public interface WebarchiveClient {

	/**
	 * selects metadata objects from database
	 *
	 * @param whereClause minimal sql-syntax WHERE clause in , omitted if
	 * null<br /> example: "url LIKE 'www.heise.de'"
	 * @param orderByClause minimal sql-syntax ORDER BY clause, ommitted if
	 * null<br /> examble: "lastCommitTime ASC"
	 * @return a list selected of MetaData-objects
	 * @throws Exception 
	 */
	public List<MetaData> select(String whereClause, String orderByClause) throws Exception;

	/**
	 * get a file list of all files of a html-archive-folder
	 *
	 * @param meta key to archive-folder
	 * @return a recursive file list of all file of the archive folder
	 * identified by the given metadata
	 * @throws Exception
	 */
	public List<File> getFileList(MetaData meta) throws Exception;

	/**
	 * get a input stream of a file of a html-archive-folder
	 *
	 * @param meta key to archive-folder
	 * @param relativePath relative path of file inside archive-folder
	 * @return inputstream of byte data
	 * @throws Exception
	 */
	public InputStream getInputStream(MetaData meta, File relativePath) throws Exception;

	/**
	 * add new files to the html-archive-folder. Existing files cannot be
	 * overwritten with this method.
	 *
	 * @param meta key to archive-folder
	 * @param relativePath relative path and name of file inside archive-folder
	 * @return output stream to write data into archive file
	 * @throws Exception
	 */
	public OutputStream getOutputStream(MetaData meta, File relativePath) throws Exception;

	/**
	 * get XML-Nodes from XML-Metadata-file by tagname. This method works only
	 * on extended XML-analysis-metadata nodes.
	 *
	 * @param meta key to XML-Metadata
	 * @param tagName tagname of an element
	 * @return String of a found elements content
	 * @throws Exception
	 */
	public String getXMLData(MetaData meta, String tagName) throws Exception;

	/**
	 * add new XML-Nodes to XML-Metadata-file using a new tagname. Existing tags
	 * cannot be overwritten with this method.
	 *
	 * @param meta
	 * @param tagName
	 * @param content
	 * @throws Exception
	 */
	public void addXMLData(MetaData meta, String tagName, String content) throws Exception;

	/**
	 * @see java.util.Observable
	 */
	public void addObserver(WebarchiveObserver o);

	/**
	 * @see java.util.Observable
	 */
	public int countObservers();

	/**
	 * @see java.util.Observable
	 */
	public void deleteObserver(WebarchiveObserver o);

	/**
	 * @see java.util.Observable
	 */
	public void deleteObservers();
}
