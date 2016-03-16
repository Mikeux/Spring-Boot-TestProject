package com.mikeux.testproject.models;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.mapping.Collection;
 
public class UserDaoImpl { // implements UserDao {
	private SessionFactory sessionFactory;
 
    public void setSessionFactory(SessionFactory sessionFactory) {
    	this.sessionFactory = sessionFactory;
    }
    
    public Collection loadProductsByCategory(String category) {
        return (Collection) this.sessionFactory.getCurrentSession()
                .createQuery("from test.Product product where product.category=?")
                .setParameter(0, category)
                .list();
    }
    
    /*public void save(User p) {
    	Session session = this.sessionFactory.openSession();
    	Transaction tx = session.beginTransaction();
        session.persist(p);
        tx.commit();
        session.close();
    }*/
 

    public List<User> list() {
        Session session = this.sessionFactory.openSession();
        List<User> personList = session.createQuery("from Person").list();
        session.close();
        return personList;
    }
}
