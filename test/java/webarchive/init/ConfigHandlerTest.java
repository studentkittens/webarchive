package webarchive.init;

import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigHandlerTest {

	static final String CONF = 
			"<root>" +
				"<node1>" +
					"<subnode1>sub1</subnode1>"+
					"<subnode2>sub2</subnode2>"+
					"<subnode3>sub3</subnode3>"+
					"<subnode4>sub4</subnode4>"+
				"</node1>\t"+
				"<node2>" +
					"\t<subnode1>sub1</subnode1>"+
					"<subnode2>sub2</subnode2>"+
					"<subnode3>sub3</subnode3>"+
					"<subnode4>sub4</subnode4>"+
				"</node2>"+
					"\n\n<!-- this is a comment -->"+
				"<node3>" +
					"<subnode1>sub1</subnode1>\n"+
					"<subnode2>sub2</subnode2>"+
					"<subnode3>sub3</subnode3>"+
					"<subnode4>sub4</subnode4>"+
				"</node3>\n"+
				"<a><b><c><d><e><f>blub</f></e></d></c></b></a>" +
			"</root>\n\n";
	static final File PATH = new File("TESTCONFIG.CONF.XML");
	ConfigHandler cfg;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if(PATH.exists())
			throw new Exception(PATH.toString()+" exists already!");
		PrintWriter pw = new PrintWriter(PATH);
		pw.write(CONF);
		pw.flush();
		pw.close();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(PATH.exists()) {
			PATH.deleteOnExit();
			PATH.delete();
		}
	}

	@Before
	public void setUp() throws Exception {
		cfg = new ConfigHandler(PATH);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSingleValue() {
		for(int i=1;i<4;i++)
			for(int j=1;j<5;j++)
				assertEquals(cfg.getValue("root.node"+i+".subnode"+j),"sub"+j);
		
	}
	@Test
	public void testGetDeepValue() {
		assertEquals(cfg.getValue("root.a.b.c.d.e.f"),"blub");
	}
	@Test
	public void testGetMissingValue() {
		assertEquals(cfg.getValue("root.a.b.c.d.e.z"),"blub");
	}	
	@Test
	public void testGetParentValue() {
		assertEquals(cfg.getValue("root.a.b.c.d.e.z"),"blub");
		assertEquals(cfg.getValue("root"),"sub1sub2sub3sub4");
	}
	
}
