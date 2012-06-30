package webarchive.transfer;

import java.io.File;
import java.io.Serializable;

import webarchive.api.model.MetaData;

public class FileDescriptor implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2153013948915864227L;
	
	private MetaData meta;
	private File file;
	public static String root;
	
	public FileDescriptor(MetaData meta, File file) {
		super();
		this.meta = meta;
		this.file = file;
	}
	
	public FileDescriptor(File file) {
		this(null,file);
	}
	
	public MetaData getMetaData() {
		return meta;
	}
	public void setMeta(MetaData meta) {
		this.meta = meta;
	}
	public File getAbsolutePath() {
		String tmp = meta.getPath().toString();
		return new File(tmp.substring(0,tmp.length()-5),file.getName());
	}
	public void setFile(File file) {
		this.file = file;
	}
	public File getFile() {
		return file;
	}
	

}
