package webarchive.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import webarchive.connection.*;
import webarchive.init.*;

/**
 * This TestSuite MUST be run with assertions disabled. 
 * Otherwise many tests will will due to assert not null.
 * 
 * @author Schneider
 *
 */

@RunWith(Suite.class)
@SuiteClasses({ FileHandlerTest.class, LockHandlerTest.class, ConfigHandlerTest.class, ConnectionHandlerTest.class })
public class ServerTestSuit {

}
