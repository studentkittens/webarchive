package webarchive.api;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import webarchive.api.model.MetaData;
import webarchive.api.select.Select;
import webarchive.api.xml.XmlEditor;
//TODO implementation by Eddy

/**
 * Interface to webarchive. provides functions to select data from the database,
 * getting and adding files to html-archive-folder or reading or extending
 * XML-Metadata. Each html-file has its own html-archive-folder, which contains
 * at minimum the html itself, XML-Metadata. The folder is extendable by
 * arbitrary files.
 *
 * @author ccwelich
 * @version 1
 */
public interface WebarchiveClient {

	/**
	 * triggers select-statements with given Select-objects.
	 *
	 * @param select a select-object
	 * @return a List of selected objects, type is relating to Select-object.
	 * E.g. the result of SelectMetaData is castable to List<lt/>MetaData<gt/>.
	 * @throws Exception if syntax of Selects were wrong or connection failed.
	 */
	public List select(Select select) throws Exception;

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
	 * get an input stream of a file of a html-archive-folder in order to read
	 * the files content.
	 *
	 * @param meta key to archive-folder
	 * @param file relative path and name of file inside archive-folder
	 * @return inputstream of byte data
	 * @throws Exception
	 */
	public InputStream getInputStream(MetaData meta, File file) throws Exception;

	/**
	 * add new files to a buffer, which will be send when closing the stream.
	 * Existing files cannot be overwritten with this method.
	 *
	 * @param meta key to archive-folder
	 * @param file relative path and name of file inside archive-folder
	 * @return output stream to write data into archive file if request was
	 * successful, otherwise null
	 * @throws Exception
	 */
	public OutputStream getOutputStream(MetaData meta, File file) throws Exception;

	/**
	 * fetches a XML-file from the server and returns an XMLEdit object, which
	 * can read or add an element from or to the XML-file
	 *
	 * @param meta key to XML-Metadata
	 * @return XMLEdit XML-editor for further operations.
	 * @throws Exception
	 */
	public XmlEditor getXMLEdit(MetaData meta) throws Exception;

	/**
	 * @throws Exception 
	 * @see java.util.Observable
	 */
	public void addObserver(WebarchiveObserver o) throws Exception;

	/**
	 * @throws Exception 
	 * @see java.util.Observable
	 */
	public int countObservers();

	/**
	 * @see java.util.Observable
	 */
	public void deleteObserver(WebarchiveObserver o) throws Exception;

	/**
	 * @see java.util.Observable
	 */
	public void deleteObservers();
}
