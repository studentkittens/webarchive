package webarchive.api;


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
import webarchive.api.model.CommitTagTest;
import webarchive.api.model.MetaDataTest;
import webarchive.api.model.TimeStampTest;
import webarchive.api.select.SelectCommitTagTest;
import webarchive.api.select.SelectMetaByCommitTest;
import webarchive.api.select.SelectMetaDataTest;

/**
 *
 * @author ccwelich
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CommitTagTest.class, MetaDataTest.class, TimeStampTest.class, SelectCommitTagTest.class, SelectMetaByCommitTest.class, SelectMetaDataTest.class})
public class ApiTestSuite {

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
