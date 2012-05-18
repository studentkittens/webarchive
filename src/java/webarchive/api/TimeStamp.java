package webarchive.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author ccwelich
 */
public class TimeStamp {

	public static final String XML_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	public static final DateFormat XML_FORMATTER = new SimpleDateFormat(XML_FORMAT);
	private java.util.Date date;
	private String xmlFormat;

	public TimeStamp(java.util.Date date) {
		this.date = date;
		this.xmlFormat = XML_FORMATTER.format(date);
	}

	public TimeStamp(String xmlFormat) throws ParseException {
		this.xmlFormat = xmlFormat;
		this.date = XML_FORMATTER.parse(xmlFormat);

	}

	@Override
	public String toString() {
		return xmlFormat;
	}

	public java.util.Date getDate() {
		return date;
	}

	public String getXmlFormat() {
		return xmlFormat;
	}
}
