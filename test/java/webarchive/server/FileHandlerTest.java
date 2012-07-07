package webarchive.server;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import webarchive.api.model.MetaData;
import webarchive.transfer.FileBuffer;
import webarchive.transfer.FileDescriptor;

/**
 * The FileHandlerTest creates a temporary folder and file structure on the hdd.
 * Then it reads, writes and lists from or to the structure.
 * After it finished testing, the structure is removed again.
 * Assertions must be disabled for this test.
 * 
 * @author Schneider
 *
 */


public class FileHandlerTest {
	
	static File rootFolder,subFolder,rootFile,subFile;
	public static final String LOREM = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
	
	static FileHandler fio;
	
	
	@AfterClass
	public static void tearDown() throws Exception {
		if(rootFolder.exists()) {
			subFile.delete();
			subFolder.delete();
			rootFile.delete();
			rootFolder.delete();
			
		}
		assertFalse(rootFolder.exists());
	}
	
	@BeforeClass
	public static void SetUp() throws Exception {
		fio = new FileHandler();
		rootFolder = new File("FileHandlerTest");
		rootFolder.deleteOnExit();
		rootFolder.mkdir();
		assertTrue( rootFolder.exists());

		subFolder = new File(rootFolder,"Folderbla");
		subFolder.mkdir();

		subFile = new File(subFolder,"SubFile");
		subFile.createNewFile();

		rootFile = new File(rootFolder,"rootFile");
		rootFile.createNewFile();
		assertTrue( subFolder.exists());
		assertTrue( subFile.exists());
		assertTrue( rootFile.exists());
		PrintWriter pw = null;
		pw = new PrintWriter(new FileOutputStream(rootFile));
		pw.write(LOREM);
		pw.flush();
		pw.close();
		
	}
	FileBuffer buf;
	@Test
	public void testRead() throws Exception {
		buf = null;
		buf = fio.read(new FileDescriptor(new MetaData(null, null, null, new File(rootFolder.getAbsolutePath()+"/data"),
					null, null),new File(rootFile.getName())));
		assertEquals(new String(buf.getData()),LOREM);
			
	}
	
	@Test(expected = Exception.class)
	public void testOverWrite() throws Exception {
		if(buf==null) testRead();
		FileBuffer tmp = new FileBuffer("BLAAAA".getBytes(),buf.getFd());
		fio.write(tmp);
	}
	@Test
	public void testWrite() throws Exception {
		
		FileDescriptor fd = new FileDescriptor(new MetaData(null, null, null, new File(rootFolder.getAbsolutePath()+"/data"),
				null, null),new File(rootFile.getName()+"ADDTITION"));
		FileBuffer tmp = new FileBuffer("BLAAAA".getBytes(),fd);
		fio.write(tmp);
		Scanner scan = new Scanner(new File(rootFolder,fd.getFile().toString()));
		assertEquals("BLAAAA",scan.nextLine());
		scan.close();
		new File(rootFolder,fd.getFile().toString()).delete();
	}
	


	@Test
	public void testGetFileTree() {
		List<File> l = fio.getFileTree(new MetaData(null, null, null, new File(rootFolder.getAbsoluteFile()+"/data"),
				null, null));
		assertTrue(l.get(1).getName().equals(subFile.getName()) || l.get(0).getName().equals(subFile.getName()));
		assertTrue(l.get(1).getName().equals(rootFile.getName()) || l.get(0).getName().equals(rootFile.getName()));
		assertTrue(!rootFile.getName().equals(subFile.getName()));
		assertTrue(l.size()==2);
	}

}
