package com.xxx.admin.data.base;

import java.net.UnknownHostException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

public class MongoDBFactory {

	private static final Logger log = Logger.getLogger(MongoDBFactory.class);

	private static ResourceBundle rb = null;
	private static Mongo mongo = null;
	private static DB db = null;
	private static String cfg = "mongoDB";// 指向src/mongoDB.properties
	private static Integer connectionsPerHost;
	private static Integer threadsAllowedToBlockForConnectionMultiplier;
	private static Integer maxWaitTime;
	private static Integer connectTimeout;
	private static Integer socketTimeout;
	public static String dbName;
	private static String dbHost;
	private static Integer dbPort;

	public MongoDBFactory() {
		// System.out.println("Initialize MongoDB Connection.");
		rb = ResourceBundle.getBundle(cfg);
		connectionsPerHost = Integer.valueOf(rb.getString("connectionsPerHost"));
		threadsAllowedToBlockForConnectionMultiplier = Integer.valueOf(rb.getString("threadsAllowedToBlockForConnectionMultiplier"));
		maxWaitTime = Integer.valueOf(rb.getString("maxWaitTime"));
		connectTimeout = Integer.valueOf(rb.getString("connectTimeout"));
		socketTimeout = Integer.valueOf(rb.getString("socketTimeout"));
		dbName = rb.getString("dbName");
		dbHost = rb.getString("dbHost");
		dbPort = Integer.valueOf(rb.getString("dbPort"));
	}

	/**
	 * 直接返回mongo，这样mongo可以处理close
	 * 
	 * 不必处理mongo的close
	 * 
	 * */
	public static Mongo getConnection() {
		if (rb == null) {
			new MongoDBFactory();
		}
		try {
			if (mongo == null) {
				try {
					ServerAddress serverAddress = new ServerAddress(dbHost, dbPort);
					MongoOptions options = new MongoOptions();
					options.autoConnectRetry = true;
					options.connectionsPerHost = connectionsPerHost;
					options.threadsAllowedToBlockForConnectionMultiplier=threadsAllowedToBlockForConnectionMultiplier;
					options.socketTimeout = socketTimeout;
					options.maxWaitTime = maxWaitTime;
					options.connectTimeout = connectTimeout;
					options.socketKeepAlive = true;
					options.slaveOk = false;
			
					mongo = new Mongo(serverAddress, options);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("get MongoDBConnection Error" + e1.getMessage());
				}

			}
		} catch (MongoException e) {
			e.printStackTrace();
		}

		return mongo;
	}

	// 直接返回db

	public static DB getDB() {
		if (mongo == null) {
			mongo = getConnection();
		}
		try {
			if (db == null) {
				db = mongo.getDB(dbName);
			}
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return db;
	}

}