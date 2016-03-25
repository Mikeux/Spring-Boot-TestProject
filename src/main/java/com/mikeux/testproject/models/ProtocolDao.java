package com.mikeux.testproject.models;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProtocolDao  extends CrudRepository<Protocol, Long> {
  /**
   * This method will find an User instance in the database by its email.
   * Note that this method is not implemented and its working code will be
   * automagically generated from its signature by Spring Data JPA.
   */
	
  public List<Protocol> findByMethodName(String mName);
  
  public Protocol findFirstByMethodNameOrderByIdDesc(String mName);
  //public Protocol findTop1ByMethodNameOrderByIdDesc(String mName);
  //public List<Protocol> findByMethodName(String mName, Pageable pageable);
  //Pageable topTen = new PageRequest(0, 10);
  //List<User> result = repository.findByUsername("Matthews", topTen);
  
  @Query("SELECT p FROM Protocol p WHERE p.methodName='?1' ORDER BY p.id DESC")
  Protocol findByMethodNameLast(String mName);
  
  @Query("SELECT p FROM Protocol p "+
		  " WHERE p.methodName like ?1 AND p.errorMessage is NULL "+
		  " AND (p.fkId IN ?2 AND p.fkId != 0) AND p.id != ?3 "+
		  " AND (minute(current_date()) - minute(p.executionTime)) < 2 "+
		  " ORDER BY p.id DESC")
  public List<Protocol> findByMethodNameNullErrorMessageAndById(String methodName, List<Long> ids, Long prId);
  
  @Query("SELECT p FROM Protocol p WHERE p.methodName like ?1 AND p.errorMessage is NULL ORDER BY p.id DESC")
  public List<Protocol> findByMethodNameNullErrorMessage(String methodName);
  
  
  //@Query("SELECT t.title FROM Todo t where t.id = :id") 
  //Optional<String> findTitleById(@Param("id") Long id);
  
  //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.limit-query-result
}