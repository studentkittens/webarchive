package webarchive.transfer;

import java.io.File;

import webarchive.api.model.MetaData;

public class FileDescriptor {
	
	private MetaData meta;
	private File file;
	
	public FileDescriptor(MetaData meta, File file) {
		super();
		this.meta = meta;
		this.file = file;
	}
	
	public MetaData getMeta() {
		return meta;
	}
	public void setMeta(MetaData meta) {
		this.meta = meta;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	

}
