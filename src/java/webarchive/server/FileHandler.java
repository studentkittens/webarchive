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
import java.util.logging.Level;

import com.sun.istack.internal.logging.Logger;

import webarchive.api.model.MetaData;
import webarchive.handler.Handler;
import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;
/**
 * Reads and writes from and to the archive using FileIn and FileOutputstreams.
 * @author Schneider
 *
 */
public class FileHandler extends Handler {

	public static final int BUFFER_SIZE = 4096; 
	/**
	 * 
	 * @param fd FileDescriptor that contains the File-Object to the File to be read 
	 * @return a FileBuffer with the content of the read File in form of byte[]
	 * @throws IOException
	 */
	public FileBuffer read(FileDescriptor fd) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis =null;
		File f = fd.getAbsolutePath();
		if(!f.exists()) {
			throw new FileNotFoundException(f.toString()+"not found!");
		}
		byte[]  tmp = new byte[BUFFER_SIZE];
		bis = new BufferedInputStream(new FileInputStream(f));
		
		int bytesRead = 0;
		while( (bytesRead=bis.read(tmp)) != -1) {
			baos.write(tmp, 0, bytesRead);
			baos.flush();
		}
	
		baos.close();
		bis.close();
		
		return new FileBuffer(baos.toByteArray(),fd);
	}

	/**
	 * Writes a FileBuffer to the archive.
	 * @param buf FileBuffer that contains the data and a FileDescriptor
	 * @throws Exception
	 */
	public void write(FileBuffer buf) throws Exception {
		File f = buf.getFd().getAbsolutePath();
		if(f.exists()) {
			Logger.getLogger(getClass()).log(Level.INFO, "Overwriting "+f.toString()+" not permitted!");
		}
		BufferedOutputStream bos = null; 
		bos = new BufferedOutputStream(new FileOutputStream(f));
		bos.write(buf.getData());
		bos.close();
	}
	/**
	 * Lists all Files in the directory and sub-directories from meta.getPath().
	 * Ignores hidden Files and Folders, that start with '.', such as ".git"
	 * @param meta Metadata pointing to target Folder
	 * @return a List of Files in the target Folder
	 */
	public List<File> getFileTree(MetaData meta) {
		File root = meta.getPath().getParentFile();
		List<File> list = new ArrayList<File>();
		addSubDirectories(root,list,meta.getPath().getParent().length());
		
		return list;
	}
	
	private void addSubDirectories(File current, List<File> list,int off) {
		
		if(current.isDirectory()) {
			if(current.getName().toCharArray()[0] == '.'){
					return;
				}
			File[] subD = current.listFiles();
			for(int i = 0; i<subD.length; i++) {
				addSubDirectories(subD[i],list,off);
			}
		} else {
			if(current.getName().toCharArray()[0] == '.'){
				return;
			}
			list.add(new File(current.toString().substring(off+1)));
		}
		
	}
	
	
}
