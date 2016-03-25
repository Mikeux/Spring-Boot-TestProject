package com.mikeux.testproject.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.*;
import org.springframework.transaction.annotation.Transactional;

//@Transactional
public interface UserDao extends CrudRepository<User, Long> {

  /**
   * This method will find an User instance in the database by its email.
   * Note that this method is not implemented and its working code will be
   * automagically generated from its signature by Spring Data JPA.
   */
  public User findByName(String name);
 
  Page<User> findAll(Pageable pageable);
  
}
