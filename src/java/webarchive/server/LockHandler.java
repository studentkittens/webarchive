/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webarchive.server;

import webarchive.handler.Handler;
import webarchive.transfer.FileDescriptor;

/**
 *
 * @author ccwelich
 */
public abstract class LockHandler extends Handler {

	public abstract void checkout(FileDescriptor fd);

	public abstract void commit(FileDescriptor fd);

	public abstract void lock(FileDescriptor fd);

	public abstract void reconnect();

	public abstract void unlock(FileDescriptor fd);

	public abstract void checkoutMaster();
	
}
