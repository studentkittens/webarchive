package webarchive.api.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//TODO tests

/**
 * A wrapper class for a date like the ones contained in the webarchive database
 * as well as in the XML-Metadata. The class incorporates a date as
 * java.util.Date and as String in TimeStamp.XML_Format.
 *
 * @author ccwelich
 */
public class TimeStamp {

	/**
	 * XML and database date-time formatstring
	 */
	public static final String XML_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	/**
	 * static DateFormat-object. used to format XML-date-string to date and vice
	 * versa.
	 */
	public static final DateFormat XML_FORMATTER = new SimpleDateFormat(XML_FORMAT);
	private java.util.Date date;
	private String xmlFormat;

	/**
	 * create TimeStamp by a given Date.
	 *
	 * @param date assert not null
	 */
	public TimeStamp(java.util.Date date) {
		assert date != null;
		this.date = date;
		this.xmlFormat = XML_FORMATTER.format(date);
	}

	/**
	 * create TimeStamp by a given date-String in XML_FORMAT
	 *
	 * @param date date-String
	 * @throws ParseException if the format of date does not match XML_FORMAT
	 */
	public TimeStamp(String date) throws ParseException {
		assert date != null;
		this.xmlFormat = date;
		this.date = XML_FORMATTER.parse(date);

	}

	@Override
	public String toString() {
		return "TimeStamp{" + date + '}';
	}

	

	/**
	 * the date-time as
	 *
	 * @see java.util.Date
	 * @return date
	 */
	public java.util.Date getDate() {
		return date;
	}

	/**
	 * the date-time as String in XML_FORMAT
	 *
	 * @return date
	 */
	public String getXmlFormat() {
		return xmlFormat;
	}
}
