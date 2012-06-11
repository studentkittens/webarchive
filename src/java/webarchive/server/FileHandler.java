package webarchive.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;

public class FileHandler implements IOHandler {

	public static final int BUFFER_SIZE = 4096; 
	
	@Override
	public void lock(FileDescriptor fd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unlock(FileDescriptor fd) {
		// TODO Auto-generated method stub

	}

	@Override
	public FileBuffer read(FileDescriptor fd) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis =null;
		File f = fd.getAbsolutePath();
		if(!f.exists()) {
			//FILENOTFOUND TODO
		}
		byte[]  tmp = new byte[BUFFER_SIZE];
		try {
			bis = new BufferedInputStream(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int bytesRead = 0;
		try {
			while( (bytesRead=bis.read(tmp)) != -1) {
				baos.write(tmp, 0, bytesRead);
				baos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			baos.close();
			bis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new FileBuffer(baos.toByteArray(),fd);
	}

	@Override
	public void write(FileBuffer buf) {
		File f = buf.getFd().getAbsolutePath();
		
		if(f.exists()) {
			//NOOVERWRIGHT-EXCEPTION //TODO
		}
		BufferedOutputStream bos = null; 
		try {
			bos = new BufferedOutputStream(new FileOutputStream(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			bos.write(buf.getData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
