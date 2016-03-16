package com.mikeux.testproject.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FolderDao  extends CrudRepository<Folder, Long> {
	
	public Folder findByName(String name);
	
}
