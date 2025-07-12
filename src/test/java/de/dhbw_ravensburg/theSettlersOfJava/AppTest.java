package de.dhbw_ravensburg.theSettlersOfJava;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for the App class.
 *
 * This class is a placeholder for testing and uses the JUnit 3 framework.
 */
public class AppTest 
    extends TestCase
{
	/**
     * Constructs a test case with the given name.
     *
     * @param testName the name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * Returns the suite of tests to be run.
     *
     * @return the test suite
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * A basic dummy test that always passes.
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
