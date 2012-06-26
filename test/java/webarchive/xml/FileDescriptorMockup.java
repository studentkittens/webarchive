
package webarchive.xml;

import java.io.File;
import webarchive.transfer.FileDescriptor;

/**
 * used to simplify tests
 * @author ccwelich
 */
public class FileDescriptorMockup extends FileDescriptor {
	File file;
	public FileDescriptorMockup(File file) {
		super(null,file);
		this.file = file;
	}

	@Override
	public File getAbsolutePath() {
		return file;
	}
	

}
