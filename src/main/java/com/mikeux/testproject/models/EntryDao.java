package com.mikeux.testproject.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EntryDao extends CrudRepository<Entry, Long> {

}
