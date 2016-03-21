package com.mikeux.testproject.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LabelDao  extends CrudRepository<Label, Long> {
	public Label findById(long id);
	
}
