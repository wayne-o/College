package org.wayne.reference;



import java.util.Date;
import java.util.List;
import org.hibernate.Transaction;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.wayne.reference.domain.Category;
import org.wayne.reference.util.HibernateUtil;


public class ReferenceApp {

    public static void main(String[] args) {
     
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
 
        Category object0 = new Category();
        Category object1 = new Category();
        session.save(object0);
        session.save(object1);
 
        transaction.commit();
 
        System.out.println("Object 0");
        System.out.println("Generated ID is: " + object0.getId());
     
 
        System.out.println("Object 1");
        System.out.println("Generated ID is: " + object1.getId());
      
    }
}