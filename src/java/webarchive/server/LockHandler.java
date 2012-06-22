/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.server;

import webarchive.transfer.FileDescriptor;

/**
 *
 * @author ccwelich
 */
public interface LockHandler {

	void checkout(FileDescriptor fd);

	void commit(FileDescriptor fd);

	void lock(FileDescriptor fd);

	void reconnect();

	void unlock(FileDescriptor fd);
	
}
