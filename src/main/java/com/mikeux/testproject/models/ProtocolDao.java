package com.mikeux.testproject.models;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProtocolDao  extends CrudRepository<Protocol, Long> {

  public List<Protocol> findByMethodName(String mName);
  
  public Protocol findFirstByMethodNameOrderByIdDesc(String mName);

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

}