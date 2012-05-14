package webarchive.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ccwelich
 */
public class DateFormatter {
	public static final String XML_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	private static DateFormat df = new SimpleDateFormat(XML_FORMAT);

	public static String format(Date d) {
		return df.format(d);
	}
	
	public static Date parse(String dateStr) throws ParseException {
		return df.parse(dateStr);
	}
}
