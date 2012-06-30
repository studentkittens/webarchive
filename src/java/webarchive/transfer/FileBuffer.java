package webarchive.transfer;

import java.io.Serializable;



public class FileBuffer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3598419521913474363L;
	private FileDescriptor fd;
	private byte[] data;
	
	public FileBuffer(FileDescriptor fd) {
		this(null,fd);
	}
	
	public FileBuffer(byte[] data) {
		this(data,null);
	}
	
	public FileBuffer(byte[] data, FileDescriptor fd) {
		this.fd = fd;
		this.data=data;
	}
	
	public FileDescriptor getFd() {
		return fd;
	}
	public byte[] getData() {
		return data;
	}

	void finish(BAOS baos) {
		 this.data = baos.toByteArray();
	}

}
