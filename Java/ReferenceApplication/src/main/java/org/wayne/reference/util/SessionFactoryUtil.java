package org.wayne.reference.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class SessionFactoryUtil {
	private static SessionFactory factory;
	
	private SessionFactoryUtil() {
	}
	
	static{
		factory = new AnnotationConfiguration().configure().buildSessionFactory();
	}
	
	public static SessionFactory getFactory(){
		return factory;
	}
	
}
