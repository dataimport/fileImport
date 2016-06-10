package com.xxx.admin.service;


import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticSearchManager {
	private static Logger log=Logger.getLogger(ElasticSearchManager.class);
	
	
	private final static String HOST = "localhost";// 端口
	private final static int PORT = 9300;// 端口
	private static Client  client = null;

	private static TransportClient transportClient;

	private ElasticSearchManager() { }

	static {
		initDBPrompties();
	}

	public static Client getClient() {
		if(client==null){
			initDBPrompties();
		}
		return client;
	}
	
	/**
	 * 初始化连接池
	 */
	private static void initDBPrompties() {
		// 其他参数根据实际情况进行添加
		try {
			
			transportClient = new TransportClient();
			client = transportClient.addTransportAddress(new InetSocketTransportAddress(HOST, PORT));
			
		} catch (Exception e) {
			log.error("Elastic Search 连接建立失败！！！！", e);
		}

	}
}

