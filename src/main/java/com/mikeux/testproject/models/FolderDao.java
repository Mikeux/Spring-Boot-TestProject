package com.mikeux.testproject.models;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FolderDao  extends CrudRepository<Folder, Long> {
	
	public Folder findByName(String name);
	public List<Folder> findByParentFolder(Folder folder);
}
