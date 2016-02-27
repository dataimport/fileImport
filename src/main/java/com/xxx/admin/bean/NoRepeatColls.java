package com.xxx.admin.bean;
 
import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = AllCollectionName.NOREPEAT_COLLECTIONNAME) 
public class NoRepeatColls  implements Serializable{
 

	/**
	 * 
	 */
	private static final long serialVersionUID = 8258791956189391088L;

	@Id
    private String id;
 
	private String uid;
    private String name;
    private String nameAlias;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameAlias() {
		return nameAlias;
	}

	public void setNameAlias(String nameAlias) {
		this.nameAlias = nameAlias;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	} 
}
