package com.xxx.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.xxx.admin.bean.Task;
import com.xxx.admin.bean.Tree;
import com.xxx.admin.data.mongo.TaskRepository;
import com.xxx.admin.file.analysis.mongo.BseRepository;

public class MongoTest {
	 
    public static void main(String[] args) {
 
        //ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("classpath:../config/applicationContext.xml");
        ConfigurableApplicationContext context =   new FileSystemXmlApplicationContext("F:/work2space/DataImport/src/main/webapp/WEB-INF/config/applicationContext.xml");

       
        BseRepository repository = context.getBean(TaskRepository.class);
 
        // cleanup collection before insertion
        repository.dropCollection();
 
        // create collection
        //repository.createCollection();
 
        //repository.saveObject(new Tree("1", "Apple Tree", 10));
        repository.createCollection("test");
        repository.saveObject(new Task("1","tableName", "origine", "tags",null,null, "separator","runTime", "startDate", "endDate", "filePath",1111));
 
//        System.out.println("1. " + repository.getAllObjects());
// 
//        repository.saveObject(new Tree("2", "Orange Tree", 3));
// 
//        System.out.println("2. " + repository.getAllObjects());
// 
//        System.out.println("Tree with id 1" + repository.getObject("1"));
// 
//        repository.updateObject("1", "Peach Tree");
// 
//        System.out.println("3. " + repository.getAllObjects());
// 
//        repository.deleteObject("2");
// 
//        System.out.println("4. " + repository.getAllObjects());
    }
   
}
