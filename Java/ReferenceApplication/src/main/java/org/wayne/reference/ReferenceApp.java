package org.wayne.reference;

import org.hibernate.Session;

import java.util.*;

import org.wayne.reference.domain.Category;
import org.wayne.reference.util.HibernateUtil;

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
        HibernateUtil.getSessionFactory().close();
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