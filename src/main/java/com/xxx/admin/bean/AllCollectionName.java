package com.xxx.admin.bean;

import java.io.Serializable;

public class AllCollectionName implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1014677312348893174L;

	public static final String ALLFILEINFO_COLLECTIONNAME = "allFileInfo";//存所有文件信息的collection表名
	
	public static final String TASKINFO_COLLECTIONNAME = "taskInfo";//存任务信息的collection表名
	
	public static final String FOLDERINFO_COLLECTIONNAME = "folderInfo";//存文件夹信息的collection表名
	
	public static final String MONGOTOSOLR_COLLECTIONNAME = "mongoSolrInfo";//mongo导入到solr中的中间表
	
	public static final String NOREPEAT_COLLECTIONNAME = "noRepeatColls";//所有数据对应的不重复mongo Collection
	
	public static final String SOLR_TASKINFO_COLLECTIONNAME = "solrTaskInfo";//存solr入库任务信息的collection表名
	
	public static final String SOLR_FINISH_TASKINFO_COLLECTIONNAME = "solrFinishTaskInfo";//存solr入库成功的任务信息的collection表名
	
}
