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
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.transaction.Transaction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Search 
{
	
	
	
	private CommandLine cmd;

    private DefaultTableModel model;

    private TableModelListener modelListener;

    private static Logger log = LoggerFactory.getLogger(Search.class);

    private static String[] glossaryEntryFields = {
            "word",
            "definition"};
    
    private EntityManagerFactory emf;

    private static final String HBM2DDL = "hbm2ddl";
    private static final String PERSISTENCE_UNIT = "reference";
	
	public Search(String[] args){
	
		// create Options object
        Options options = new Options();
        options.addOption("h", HBM2DDL, true, "run schema generation");

        CommandLineParser parser = new PosixParser();
        try {
            cmd = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException pe) {
            new HibernateException("Unable to parse command line.", pe);
        }

        initHibernate();
	}
	
	private void initHibernate() {
        Ejb3Configuration config = new Ejb3Configuration();
        String hbm2ddl = cmd.getOptionValue("h");
        hbm2ddl = "create-drop";

        if (hbm2ddl != null) {
            log.info("Setting hibernate.hbm2ddl.auto to" + hbm2ddl);
            config.setProperty("hibernate.hbm2ddl.auto", hbm2ddl);
        } else {
            log.info("No schema generation.");
        }

        config.configure(PERSISTENCE_UNIT, new HashMap());
        emf = config.buildEntityManagerFactory();
    }
	
	 public void index() {
	        EntityManager em = emf.createEntityManager();
	        em.getTransaction().begin();
	        FullTextSession ftSession = org.hibernate.search.Search.getFullTextSession((Session) em.getDelegate());
	        List results = ftSession.createCriteria(Glossary.class).list();
	        for (Object obj : results) {
	            ftSession.index(obj);
	        }
	        em.getTransaction().commit();
	        em.close();

	    }

	    public void purge() {
	        EntityManager em = emf.createEntityManager();
	        em.getTransaction().begin();
	        FullTextSession ftSession = org.hibernate.search.Search.getFullTextSession((Session) em.getDelegate());
	        ftSession.purgeAll(Glossary.class);
	        em.getTransaction().commit();
	        em.close();
	    }
	    
	    public List<Glossary> search(String searchQuery) throws ParseException {
	        Query query = searchQuery(searchQuery);

	        List<Glossary> items = query.getResultList();

	        for (Glossary p : items) {
	            log.info("title: " + p.getTitle());
	        }
	        return items;
	    }
	    
	    private Query searchQuery(String searchQuery) throws ParseException {
	        //lucene part
	        Map<String, Float> boostPerField = new HashMap<String, Float>(4);
	      
	        boostPerField.put("word", (float) 3);
	        boostPerField.put("definition", (float) .5);

	        QueryParser parser = new MultiFieldQueryParser(glossaryEntryFields, new StandardAnalyzer(), boostPerField);

	        org.apache.lucene.search.Query luceneQuery;
	        luceneQuery = parser.parse(searchQuery);

	        //Hibernate Search
	        EntityManager em = emf.createEntityManager();
	        
	        
	       
	        FullTextEntityManager ftEm =
	                org.hibernate.search.jpa.Search.getFullTextEntityManager(
	                        (EntityManager) em);
	        final FullTextQuery query = ftEm.createFullTextQuery(luceneQuery, GlossaryEntry.class);

	        return query;
	    }
	    
	    public void CreateTestData(){
	    	
	    	
	    	Glossary g = new Glossary();
		    g.setTitle("test 1");
		    
		    GlossaryEntry ge = new GlossaryEntry();
		    
		    Set<GlossaryEntry> gel = new HashSet<GlossaryEntry>();
		    
		    ge.setWord("test 1");
		    ge.setDefinition("this is the definition for test 1");
		    
		    gel.add(ge);
		    
		    g.setGlossaryEntries(gel);

	    	
	    	// Non-managed environment idiom
	    	EntityManager em = emf.createEntityManager();
	    	EntityTransaction tx = null;
	    	try {
	    	    tx = em.getTransaction();
	    	    tx.begin();

			 
			    em.persist(g);
			   
			    
	    	    tx.commit();
	    	}
	    	catch (RuntimeException e) {
	    	    if ( tx != null && tx.isActive() ) tx.rollback();
	    	    throw e; // or display error message
	    	}
	    	finally {
	    	    em.close();
	    	}
	    	
	    }
}