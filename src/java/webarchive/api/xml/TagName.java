package webarchive.api.xml;

import java.io.Serializable;

/**
 * Represents a tagName in the webarchive XML-data file. All tags and attributes
 * and have a prefix which binds them to the global webarchive namespace. <br/>
 * This class ensures, that all added elements have the correct prefixed name.
 * While it is necessary for the creation of DataElements and Elements ({@link webarchive.api.xml.XmlEditor}),
 * it is also recommended to use it for subelements like attributes.
 *
 * @author ccwelich
 */
public class TagName implements Serializable {

	/**
	 * the webarchive XML global prefix
	 */
	public static final String PREFIX = "wa:";
	/**
	 * the webarchive global XML-namespace
	 */
	public static final String NAMESPACE = "http://www.hof-university.de/webarchive";
	public static final TagName DATA_TAG = new TagName("data");
	private final String absName;

	/**
	 * Adds the PREFIX to a given name by the default webarchive PREFIX. If the
	 * name has already a prefix, which is terminated by ':', then this prefix
	 * will be replaced by the default PREFIX. <br/> Note: If own prefixes are
	 * needful add a template prefix at the beginning of the userdefined PREFIX. 
	 * The template PREFIX will then be overwritten.
	 * Example:
	 * <code>
	 *		TagName t = new TagName("X:myPrefix:myName");
	 *		System.out.println("t = " + t); // t = "wa:myPrefix:myName"
	 * </code> <br/> 
	 *
	 * @param name a tagName, asserted 
	 * @throws NullPointerException if name is null
	 */
	public TagName(String name) throws NullPointerException {
		assert name != null;
		this.absName = addPrefixTo(name);
	}
	/**
	 * the absoluteName. 
	 * Similar to toString()
	 * @return PREFIX+name 
	 */
	public String getAbsoluteName() {
		return absName;

	}

	@Override
	public String toString() {
		return absName;
	}

	private String addPrefixTo(String name) {
		int i = name.indexOf(':');
		return PREFIX + ((i > -1) ? name.substring(i + 1) : name);
	}

}
