package webarchive.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import webarchive.api.model.MetaData;
import webarchive.handler.Handler;
import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;

public class FileHandler extends Handler {

	public static final int BUFFER_SIZE = 4096; 
	
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

	public void write(FileBuffer buf) {
		File f = buf.getFd().getAbsolutePath();
		
		if(f.exists()) {
			//NOOVERWRITE-EXCEPTION //TODO
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

	public List<File> getFileTree(MetaData meta) {
		File root = meta.getPath();
		List<File> list = new ArrayList<File>();
		addSubDirectories(root,list);
		
		return list;
	}
	private void addSubDirectories(File current, List<File> list) {
		if(current.isDirectory()) {
			File[] subD = current.listFiles();
			for(int i = 0; i<subD.length; i++) {
				addSubDirectories(subD[i],list);
			}
		} else {
			list.add(current);
		}
		
	}
	
}