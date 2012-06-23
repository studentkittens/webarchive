package webarchive.api.xml;

import java.io.Serializable;

/**
 *
 * @author ccwelich
 */
public class TagName implements Serializable {

	public static final String PREFIX = "wa:";
	public static final String NAMESPACE = "http://www.hof-university.de/webarchive";
	public static final TagName DATA_TAG = new TagName("data");
	private final String name;

	public TagName(String name) {
		assert name != null;
		this.name = addPrefixTo(name);
	}

	public String getName() {
		return name;
	}


	@Override
	public String toString() {
		return name;
	}
	/**
	 * adds a PREFIX to a given string by the default webarchive PREFIX. If name has already a
	 * PREFIX, which is terminated by ':', then this PREFIX will be replaced by
	 * the default PREFIX. <br/>
	 * Note: use only if own prefixes are needed and add a template PREFIX at the beginning of the userdefined PREFIX.
	 * The template PREFIX will then be overwritten. <br/>
	 * Also use this method to add prefixes to Attribute names.
	 *
	 * @param name name to addPrefixTo
	 * @return addPrefixTo+name
	 */
	private String addPrefixTo(String name) {
		int i = name.indexOf(':');
		return PREFIX + ((i > -1) ? name.substring(i + 1) : name);
	}

}
