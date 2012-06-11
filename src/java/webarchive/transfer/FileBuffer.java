package webarchive.transfer;



public class FileBuffer {

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

	void finish(MyBAOS baos) {
		 this.data = baos.toByteArray();
	}

}
