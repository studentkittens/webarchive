package webarchive.api.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A wrapper class for a date like the ones contained in the webarchive database
 * as well as in the XML-Metadata. The class maps between java.util.Date and the
 * String in TimeStamp.XML_Format.
 * The mapping is done lazily when a getter is called and the associated value is null.
 *
 * @author ccwelich
 */
public class TimeStamp implements Serializable {

	/**
	 * XML and database date-time formatstring
	 */
	public static final String XML_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	/**
	 * static DateFormat-object. used to format XML-date-string to date and vice
	 * versa.
	 */
	public static final DateFormat XML_FORMATTER = new SimpleDateFormat(
		XML_FORMAT);
	private java.util.Date date;
	private String xmlFormat;

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
		final TimeStamp other = (TimeStamp) obj;

		return getXmlFormat().equals(other.getXmlFormat());
	}

	@Override
	public int hashCode() {
		return getXmlFormat().hashCode();
	}

	/**
	 * create TimeStamp by a given Date.
	 *
	 * @param date asserted not null
	 */
	public TimeStamp(java.util.Date date) {
		assert date != null;
		this.date = date;
		
	}

	/**
	 * create TimeStamp by a given date-String in XML_FORMAT
	 *
	 * @param date date-String
	 */
	public TimeStamp(String date) {
		assert date != null;
		this.xmlFormat = date;
	}

	@Override
	public String toString() {
		ensureXmlFormat();
		return "TimeStamp{" + xmlFormat + '}';
	}

	/**
	 * the date-time as
	 *
	 * @see java.util.Date
	 * @return date
	 * @throws ParseException if the format of date does not match XML_FORMAT
	 */
	public java.util.Date getDate() throws ParseException {
		ensureDate();
		return date;
	}

	/**
	 * the date-time as String in XML_FORMAT
	 *
	 * @return date
	 */
	public String getXmlFormat() {
		ensureXmlFormat();
		return xmlFormat;
	}

	private void ensureXmlFormat() {
		if(xmlFormat==null)
		synchronized (XML_FORMATTER) {
			this.xmlFormat = XML_FORMATTER.format(date);
		}
	}
	

	private void ensureDate() throws ParseException {
		if(date==null)
		synchronized (XML_FORMATTER) {
			this.date = XML_FORMATTER.parse(xmlFormat);
		}
	}
}
