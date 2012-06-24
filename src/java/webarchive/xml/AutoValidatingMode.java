package webarchive.xml;

/**
 * the auto modi for validating the xml documents.
 * Determines the time when Dom-documents will be validated
 *
 * @author ccwelich
 */
public enum AutoValidatingMode {

	/**
	 * documents will never be validated.
	 */
	NEVER,
	/**
	 * documents will be validated after each change and when loaded.
	 */
	ALWAYS,
	/**
	 * documents will be validated after each changed.
	 */
	AFTER_UPDATE,
	/**
	 * documents will be validated after each built of the dom.
	 */
	AFTER_BUILT_DOM;
}
