package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestLoginClient.class,TestLoginServer.class,TestVisitingReportsClient.class,TestVisitingReportsServer.class})
public class AllTests {

}
