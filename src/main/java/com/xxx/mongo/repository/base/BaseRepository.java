package com.xxx.mongo.repository.base;

import java.util.List;
 
public interface BaseRepository<T> {
 
    public List<T> getAllObjects();
        
    public void saveObject(T object);
    
    public void saveObject(T object,String collectonName);
 
    public T getObjectById(String uid);
  
    public boolean deleteObjectById(String id);
    
    public boolean deleteObject(T object);
 
    public void createCollection();
    
    public void createCollection(String collectionName);
 
    public void dropCollection();
          
}