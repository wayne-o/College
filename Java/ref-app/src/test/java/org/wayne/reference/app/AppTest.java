package org.wayne.reference.app;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.compass.core.util.Assert;
import org.wayne.reference.app.model.Glossary;
import org.wayne.reference.app.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	 System.out.println("Hello World!"); // Display the string.
    	 String[] args={};
 		Search search = new Search(args);
 		search.index();
 		
 		List<Glossary> prods = null;
 		
 		try {
 			prods = search.search("chakespur~");
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		
 		Assert.notEmpty(prods);
 		
 		System.out.println("Hello World!"); // Display the string.
    }
}
