package org.wayne.reference;



import java.util.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import org.wayne.reference.domain.Category;
import org.wayne.reference.util.HibernateUtil;
import org.wayne.reference.util.SessionFactoryUtil;

public class ReferenceApp {

    public static void main(String[] args) {
        ReferenceApp mgr = new ReferenceApp();

        if (args[0].equals("store")) {
            mgr.createAndStoreCategory("My Category", new Date());
        }

		else if (args[0].equals("list")) {
            List Categorys = mgr.listCategorys();
            for (int i = 0; i < Categorys.size(); i++) {
                Category theCategory = (Category) Categorys.get(i);
                System.out.println(
                        "Category: " + theCategory.getTitle() + " Time: " + theCategory.getDate()
                );
            }
        }
		else if (args[0].equals("search")) {
            List Categorys = mgr.listFuzzyCategories();
            for (int i = 0; i < Categorys.size(); i++) {
                Category theCategory = (Category) Categorys.get(i);
                System.out.println(
                        "Category: " + theCategory.getTitle() + " Time: " + theCategory.getDate()
                );
            }
        }
        HibernateUtil.getSessionFactory().close();
    }
	
	private static void createSomeCategories() {
		Session  session = SessionFactoryUtil.getFactory().getCurrentSession();
		session.beginTransaction();
		session.save(new Category("1"));
		session.save(new Category("2"));
		session.save(new Category("3"));
		
		session.getTransaction().commit();
	}


	private List listFuzzyCategories(){
	
		Session session = SessionFactoryUtil.getFactory().getCurrentSession();

	
		//		create a full text session
		FullTextSession fSession = Search.getFullTextSession(session);
		fSession.beginTransaction();
		
		//		create a luceneQuery with a parser
		QueryParser parser = new QueryParser("title", new StandardAnalyzer());
		/*Query lucenceQuery = null;
		
		try {
			lucenceQuery = parser.parse("content:hibernate");
			
		} catch (ParseException e) {
			throw new RuntimeException("Cannot search with query string",e);
		}
		//		execute the query
		List<Category> articles = fSession.createFullTextQuery(lucenceQuery, Category.class).list();
		for (Category cat : articles) {
			System.out.println(cat);
		}
		fSession.getTransaction().commit();
		*/
		
		return null;
	}
	
	
	
     private List listCategorys() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Category").list();
        session.getTransaction().commit();
        return result;
    }

    private void createAndStoreCategory(String title, Date theDate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Category theCategory = new Category();
        theCategory.setTitle(title);
        theCategory.setDate(theDate);
        session.save(theCategory);

        session.getTransaction().commit();
    }

}