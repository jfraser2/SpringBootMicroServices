package junit.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import functional.junit.RegistrationTests;
import nonFunctional.junit.BuildJwtToken;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	RegistrationTests.class,
	BuildJwtToken.class
})
public class TestSuiteOne
{
	  // the class remains empty,
	  // used only as a holder for the above annotations

}
