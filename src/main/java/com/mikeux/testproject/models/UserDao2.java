package com.mikeux.testproject.models;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



//http://blog.netgloo.com/2014/10/06/spring-boot-data-access-with-jpa-hibernate-and-mysql/

@Repository
@Transactional
public class UserDao2 {
	 /**
	   * Save the user in the database.
	   */
	  public void create(User user) {
		  entityManager.persist(user);
	    return;
	  }
	  
	  /**
	   * Delete the user from the database.
	   */
	  public void delete(User user) {
	    if (entityManager.contains(user))
	      entityManager.remove(user);
	    else
	      entityManager.remove(entityManager.merge(user));
	    return;
	  }
	  
	  /**
	   * Return all the users stored in the database.
	   */
	  @SuppressWarnings("unchecked")
	  public List getAll() {
	    return entityManager.createQuery("from User").getResultList();
	  }
	  
	  /**
	   * Return the user having the passed email.
	   */
	  public User getByEmail(String email) {
	    return (User) entityManager.createQuery(
	        "from User where email = :email")
	        .setParameter("email", email)
	        .getSingleResult();
	  }

	  /**
	   * Return the user having the passed id.
	   */
	  public User getById(long id) {
	    return entityManager.find(User.class, id);
	  }

	  /**
	   * Update the passed user in the database.
	   */
	  public void update(User user) {
	    entityManager.merge(user);
	    return;
	  }

	  // Private fields
	  
	  // An EntityManager will be automatically injected from entityManagerFactory
	  // setup on DatabaseConfig class.
	  @PersistenceContext
	  private EntityManager entityManager;
}
