package webarchive.server;

import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;

public interface IOHandler {

	public void lock(final FileDescriptor fd);
	public void unlock(final FileDescriptor fd);
	
	public FileBuffer read(final FileDescriptor fd);
	public void write(final FileBuffer buf);
	
	
}
