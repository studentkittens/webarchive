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
import webarchive.api.ApiTestSuite;
import webarchive.dbaccess.DbAccessTestSuite;

/**
 *
 * @author ccwelich
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ApiTestSuite.class, DbAccessTestSuite.class})
public class TestSuite {

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
