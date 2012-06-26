package webarchive.xml;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author ccwelich
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	webarchive.xml.XmlMethodFactoryTest.class,
	webarchive.xml.XmlIOHandlerTest.class,
	webarchive.xml.XmlConfTest.class,
	webarchive.xml.XmlHandlerTest.class, 
	webarchive.xml.DataElementTest.class,
	webarchive.xml.XmlEditorTest.class})
public class XmlTestSuite {

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
}
