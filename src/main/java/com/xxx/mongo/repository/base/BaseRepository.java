package com.xxx.mongo.repository.base;

import java.util.List;
import com.xxx.core.exception.MongoDBException;
 
public interface BaseRepository<T> {
 
    public List<T> getAllObjects()throws MongoDBException;
        
    public void saveObject(T object)throws MongoDBException;
    
    public void saveObject(T object,String collectonName)throws MongoDBException;
 
    public T getObjectById(String uid)throws MongoDBException;
  
    public boolean deleteObjectById(String id)throws MongoDBException;
    
    public boolean deleteObject(T object)throws MongoDBException;
 
    public void createCollection()throws MongoDBException;
    
    public void createCollection(String collectionName)throws MongoDBException;
    
    public void dropCollection()throws MongoDBException;
          
}