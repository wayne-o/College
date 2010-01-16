package org.wayne.reference.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.search.FullTextSession;
import org.wayne.reference.app.model.GlossaryEntry;
import org.wayne.reference.app.model.Glossary;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Hello world!
 *
 */
public class App 
{
  


    public static void main( String[] args )
    {
    	 System.out.println("Hello World!"); // Display the string.
		Search search = new Search(args);
		search.index();
		
		search.CreateTestData();
		
		try {
			List<Glossary> prods = search.search("chakespur~");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Hello World!"); // Display the string.
       
    }
	
	
}
