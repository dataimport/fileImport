package com.xxx.admin.data.mongo;

import java.util.List;

import com.mongodb.WriteResult;
 
public interface Repository<T> {
 
    public List<T> getAllObjects();
    
    public List<T> getObjectsByRunTime(String date);
    
    public List<T> getObjectsByStatus(int status);
 
    public void saveObject(T object);
    
    public void saveObject(T object,String collectonName);
 
    public T getObjectByUid(String uid);
 
    public WriteResult updateObject(String id, String name);
 
    public void deleteObject(String id);
 
    public void createCollection();
    
    public void createCollection(String collectionName);
 
    public void dropCollection();
    
    public List<T> getObjectsByFilePath(String filePath);
      
}