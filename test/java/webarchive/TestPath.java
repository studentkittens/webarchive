package webarchive;

import java.io.File;
import java.util.Properties;
import java.util.Set;


/**
 *
 * @author ccwelich
 */
public class TestPath {
	public static void main(String[] args) {
		Properties props = System.getProperties();
		Set keys = props.keySet();
		for(Object key : keys) {
			System.out.println(
				"key = " + key+
				", value = " + props.getProperty((String)key)
			);
			
		}
		String classpath = props.getProperty("java.class.path");
		System.out.println("classpath = " + classpath);
		File f = new File("test");
		System.out.println("f = " + f.getAbsolutePath());
	}
}
