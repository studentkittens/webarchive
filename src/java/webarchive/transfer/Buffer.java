package webarchive.transfer;



public class Buffer {

	private FileDescriptor fd;
	private byte[] data;
	
	public Buffer(FileDescriptor fd) {
		this.fd = fd;
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
