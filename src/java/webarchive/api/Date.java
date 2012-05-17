package webarchive.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author ccwelich
 */
public class Date {

	public static final String XML_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private static DateFormat df = new SimpleDateFormat(XML_FORMAT);
	private java.util.Date date;
	private String xmlFormat;

	public Date(java.util.Date date) {
		this.date = date;
		this.xmlFormat = df.format(date);
	}

	public Date(String xmlFormat) throws ParseException {
		this.xmlFormat = xmlFormat;
		this.date = df.parse(xmlFormat);

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
