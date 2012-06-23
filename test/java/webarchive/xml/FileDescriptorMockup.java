
package webarchive.xml;

import java.io.File;
import webarchive.api.model.MetaData;
import webarchive.transfer.FileDescriptor;

/**
 *
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
