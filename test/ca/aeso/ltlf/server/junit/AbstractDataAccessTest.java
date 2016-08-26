package ca.aeso.ltlf.server.junit;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public abstract class AbstractDataAccessTest extends AbstractTransactionalDataSourceSpringContextTests
{
	/**
	 * Reference the Spring configuration files for the test case.
	 */
	protected String[] getConfigLocations()
	{
		return new String[]{ "applicationContext.xml", "datasourceContext-junit.xml" };
	}


}