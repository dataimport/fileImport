package com.xxx.admin.bean.base;

import java.io.Serializable;

public abstract class BaseTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5193521874045600482L;
	
	public static final int TASK_STATUS_SOLR_WAITING = -100; // -100 solr初始状态，等待mongo任务完成后，变为0
	public static final int TASK_STATUS_WAITING = 0; //0 未执行
	public static final int TASK_STATUS_RUNNING = 1; // 1 执行中 
	public static final int TASK_STATUS_SUCCESS = 2; //  2 已完成
	public static final int TASK_STATUS_FAILED = -2; // -2 失败
	public static final int TASK_STATUS_EXCEPTION = -200; // -200 执行过程中有异常发生
	
	private String uid;
	private String tableName;
	private String tableNameAlias;
	private String origin;
	private String tags;
	private String[] columnName;
	private String[] columnNameTag;
	private Integer[] columnIndex;
	private String separator;
	private String runTime;
	private String startDate="";
	private String endDate="";
	private String filePath;
	private String fileName;
	private String fileCode;
	private long fileSize;
	private String leftTime;
	private long totalCount= 0l;
	private int taskStatus;  // 0 未执行  1 执行中 2 已完成  -2 失败
	private String timeUse;//耗时;
	private int runNum;//当前插入的行数
	private String  cleanOrAppend;  //入库方式是追加还是删除重新导入
	private int  beginLineNum; //从文件第几行继续开始导入
	private String createUser="lxc_admin"; //任务的创建者
	private boolean firstLineIgnore; //首行是否忽略
	private String[] dataType;//数据类型
		
	
	public String getTableNameAlias() {
		return tableNameAlias;
	}

	public void setTableNameAlias(String tableNameAlias) {
		this.tableNameAlias = tableNameAlias;
	}


	public boolean getFirstLineIgnore() {
		return firstLineIgnore;
	}
	public void setFirstLineIgnore(boolean firstLineIgnore) {
		this.firstLineIgnore = firstLineIgnore;
	}

	public String getCleanOrAppend() {
		return cleanOrAppend;
	}

	public void setCleanOrAppend(String cleanOrAppend) {
		this.cleanOrAppend = cleanOrAppend;
	}

	public int getBeginLineNum() {
		return beginLineNum;
	}
	public void setBeginLineNum(int beginLineNum) {
		this.beginLineNum = beginLineNum;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getTimeUse() {
		return timeUse;
	}
	public void setTimeUse(String timeUse) {
		this.timeUse = timeUse;
	}
		public int getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getLeftTime() {
		return leftTime;
	}
	public void setLeftTime(String leftTime) {
		this.leftTime = leftTime;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String[] getColumnName() {
		return columnName;
	}
	public void setColumnName(String[] columnName) {
		this.columnName = columnName;
	}
	
	public String[] getColumnNameTag() {
		return columnNameTag;
	}

	public void setColumnNameTag(String[] columnNameTag) {
		this.columnNameTag = columnNameTag;
	}


	public Integer[] getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(Integer[] columnIndex) {
		this.columnIndex = columnIndex;
	}
	public String getSeparator() {
		if(" ".equals(separator)){
			separator="\\s+";
		}
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public int getRunNum() {
		return runNum;
	}
	public void setRunNum(int runNum) {
		this.runNum = runNum;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

	public String getFileCode() {
		return fileCode;
	}


	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}


	public String[] getDataType() {
		return dataType;
	}

	public void setDataType(String[] dataType) {
		this.dataType = dataType;
	}

	public BaseTask(){
		  
	  }

	public BaseTask(String uid, String tableName, String tableNameAlias, String origin, String tags,
			String[] columnName, String[] columnNameTag, Integer[] columnIndex, String separator, String runTime,
			String startDate, String endDate, String filePath, String fileName, String fileCode, long fileSize,
			String leftTime, long totalCount, int taskStatus, String timeUse, int runNum, String cleanOrAppend,
			int beginLineNum, String createUser, boolean firstLineIgnore, String[] dataType) {
		super();
		this.uid = uid;
		this.tableName = tableName;
		this.tableNameAlias = tableNameAlias;
		this.origin = origin;
		this.tags = tags;
		this.columnName = columnName;
		this.columnNameTag = columnNameTag;
		this.columnIndex = columnIndex;
		this.separator = separator;
		this.runTime = runTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileCode = fileCode;
		this.fileSize = fileSize;
		this.leftTime = leftTime;
		this.totalCount = totalCount;
		this.taskStatus = taskStatus;
		this.timeUse = timeUse;
		this.runNum = runNum;
		this.cleanOrAppend = cleanOrAppend;
		this.beginLineNum = beginLineNum;
		this.createUser = createUser;
		this.firstLineIgnore = firstLineIgnore;
		this.dataType = dataType;
	}
	
}
