package com.mikeux.testproject.models;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FolderDao  extends CrudRepository<Folder, Long> {
	
	public Folder findByName(String name);
	public List<Folder> findByParentFolder(Folder folder);
	
	//@Query("DELETE FROM Folder f WHERE f.id IN ?1")
	//public void deleteByIds(List<Long> ids);
	public void deleteByIdIn(List<Long> ids);
	
	public void deleteById(Long id);
	
	public List<Folder> findByParentFolderId(Long folderId);
	
}
